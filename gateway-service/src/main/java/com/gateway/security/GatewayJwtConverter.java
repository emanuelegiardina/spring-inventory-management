package com.gateway.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;
// CLASSE CHE MAPPA IL JWT IN GRANTEDAUTHORITY (UTILIZZATO DA SPRING SECURITY )
@Component
public class GatewayJwtConverter implements Converter<Jwt, Mono<AbstractAuthenticationToken>> {

    @Override
    public Mono<AbstractAuthenticationToken> convert(Jwt jwt) {

        Collection<GrantedAuthority> authorities = new HashSet<>();

        // SCOPES -> SCOPE_xxx
        String scopes = jwt.getClaimAsString("scope");
        if (scopes != null) {
            Arrays.stream(scopes.split(" "))
                    .map(s -> new SimpleGrantedAuthority("SCOPE_" + s))
                    .forEach(authorities::add);
        }

        // REALM ROLES -> ROLE_xxx
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess != null) {
            List<String> roles = (List<String>) realmAccess.get("roles");
            if (roles != null) {
                roles.stream()
                        .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                        .forEach(authorities::add);
            }
        }

        // CLIENT ROLES
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        if (resourceAccess != null) {
            resourceAccess.forEach((client, access) -> {
                Map<String, Object> accessMap = (Map<String, Object>) access;
                List<String> clientRoles = (List<String>) accessMap.get("roles");
                if (clientRoles != null) {
                    clientRoles.stream()
                            .map(r -> new SimpleGrantedAuthority(client + ":" + r))
                            .forEach(authorities::add);
                }
            });
        }

        return Mono.just(new JwtAuthenticationToken(jwt, authorities));
    }
}
