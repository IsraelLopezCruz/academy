package com.priceshoes.academy.configuration;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
@ConfigurationProperties(prefix = "oauth-authentication")
public class OAuthIssuersConfig {

    @NonNull
    private final Map<Channel, String> issuers;

    public Set<String> getIssuers() {
        return new HashSet<>(issuers.values());
    }

    public Map<String, Channel> getIssuersChannel() {
        return issuers.entrySet()
                .stream()
                .collect(Collectors.toUnmodifiableMap(e -> e.getValue().toLowerCase(Locale.ROOT), Map.Entry::getKey, (a, b) -> b));
    }

    public String getIssuer(@NonNull Channel channel) {
        return issuers.get(channel);
    }


}

