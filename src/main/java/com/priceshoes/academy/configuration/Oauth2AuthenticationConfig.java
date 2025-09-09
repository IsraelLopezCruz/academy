package com.priceshoes.academy.configuration;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;

import java.util.Collection;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class Oauth2AuthenticationConfig {


    @NonNull
    private OAuthIssuersConfig oAuthIssuersConfig;

    private static Converter<Jwt, ? extends AbstractAuthenticationToken> grantedAuthoritiesExtractor(@NonNull Channel channel) {
        var jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("cognito:groups");
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        var channelAppenderConverter = new ChannelAppenderConverter(jwtGrantedAuthoritiesConverter, channel);
        var jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(channelAppenderConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    public JwtIssuerAuthenticationManagerResolver getJwtIssuerAuthenticationManagerResolver() {
        var issuersConfig = oAuthIssuersConfig.getIssuersChannel()
                .entrySet().stream()
                .map(issuerConfig -> new MTTrustedIssuerJwtAuthenticationManagerResolver.TrustedIssuerConfig(issuerConfig.getKey(), grantedAuthoritiesExtractor(issuerConfig.getValue())))
                .collect(Collectors.toSet());
        var mtResolver = new MTTrustedIssuerJwtAuthenticationManagerResolver(issuersConfig);
        return new JwtIssuerAuthenticationManagerResolver(mtResolver);
    }

    /**
     * This allows to prevent or allow the usage of certain api for an specific channel only.
     * Using for instance in a controller.
     *
     * @PreAuthorize("hasRole('PRICESHOES')")
     */
    @Value
    static class ChannelAppenderConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
        @NonNull
        Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter;
        @NonNull
        Channel channel;

        @Nullable
        @Override
        public Collection<GrantedAuthority> convert(Jwt source) {
            var authorities = jwtGrantedAuthoritiesConverter.convert(source);
            authorities.add(new SimpleGrantedAuthority("ROLE_" + channel));
            return authorities;
        }
    }

}


