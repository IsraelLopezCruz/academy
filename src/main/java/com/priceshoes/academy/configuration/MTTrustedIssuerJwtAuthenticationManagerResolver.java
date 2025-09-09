package com.priceshoes.academy.configuration;

import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

@Slf4j
public class MTTrustedIssuerJwtAuthenticationManagerResolver implements AuthenticationManagerResolver<String> {
    private final Map<String, AuthenticationManager> authenticationManagers = new ConcurrentHashMap<>();
    private final Set<TrustedIssuerConfig> trustedIssuersConfig;


    @Value
    static class TrustedIssuerConfig implements Predicate<String> {
        String issuer;
        Converter<Jwt, ? extends AbstractAuthenticationToken> converter;

        @Override
        public boolean test(@NonNull String testedIssuer) {
            return StringUtils.containsIgnoreCase(testedIssuer, issuer);
        }
    }

    MTTrustedIssuerJwtAuthenticationManagerResolver(@NonNull Set<TrustedIssuerConfig> issuersConfig) {
        this.trustedIssuersConfig = issuersConfig;
    }

    public AuthenticationManager resolve(@NonNull String issuer) {
        var issuerConfOpt = trustedIssuersConfig.stream().filter(t -> t.test(issuer)).findFirst();

        if (issuerConfOpt.isPresent()) {
            var issuerConf = issuerConfOpt.get();
            var authenticationManager = this.authenticationManagers.computeIfAbsent(issuer, k -> {
                log.debug("Constructing AuthenticationManager");
                var jwtDecoder = JwtDecoders.fromIssuerLocation(issuer);
                var provider = new JwtAuthenticationProvider(jwtDecoder);
                Optional.ofNullable(issuerConf.getConverter()).ifPresent(provider::setJwtAuthenticationConverter);
                return provider::authenticate;
            });
            log.debug("Resolved AuthenticationManager for issuer '{}'", issuer);
            return authenticationManager;
        }

        log.error("Did not resolve AuthenticationManager since issuer is not trusted, issuer='{}'", issuer);
        return null;
    }
}