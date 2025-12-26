package com.order.security;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.core.convert.converter.Converter;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

public class KeycloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Set<GrantedAuthority> authorities = new HashSet<>();

        // SCOPES -> SCOPE_xxx
        String scopes = jwt.getClaimAsString("scope");
        if (scopes != null && !scopes.isEmpty()) {
            Arrays.stream(scopes.split(" "))
                  .map(s -> new SimpleGrantedAuthority("SCOPE_" + s))
                  .forEach(authorities::add);
        }

        // REALM ROLES -> ROLE_xxx
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess != null && realmAccess.get("roles") instanceof List<?>) {
            List<String> roles = (List<String>) realmAccess.get("roles");
            roles.stream()
                 .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                 .forEach(authorities::add);
        }

        // CLIENT ROLES -> client:role
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

        return authorities;
    }
}