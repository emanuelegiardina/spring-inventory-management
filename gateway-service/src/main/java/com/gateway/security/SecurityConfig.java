package com.gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .authorizeExchange(exchanges -> exchanges
            
                .pathMatchers("/actuator/**").permitAll()
                .anyExchange().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2.jwt())  // <----- VALIDAZIONE JWT CONFIGURAZIONE TRAMITE APPLICATION yml
           
         /*    .oauth2ResourceServer(oauth2 -> oauth2      // <----- CONVERTER JWT 
                .jwt(jwt -> jwt
                        .jwtAuthenticationConverter(new GatewayJwtConverter())
                )
            )*/
            .formLogin(form -> form.disable())  // disabilita login HTML
            .httpBasic(basic -> basic.disable()); // opzionale, disabilita anche basic auth

        return http.build();
    }
}
