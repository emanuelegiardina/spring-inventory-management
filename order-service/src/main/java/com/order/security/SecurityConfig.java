package com.order.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                //  requestMatchers per actuator
                .requestMatchers(
                    "/actuator/**",
                    "/error","/favicon.ico"
                ).permitAll()
                .anyRequest().authenticated()
            )
            
            .formLogin(form -> form.disable())  // disabilita login HTML
            .httpBasic(basic -> basic.disable()) //  disabilita  basic auth
            .oauth2ResourceServer(oauth2 -> oauth2.jwt());  // <-----validazione JWT con keycloack
         //  .oauth2ResourceServer(oauth2 -> oauth2
        //.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))); // <-- mappa jwt in grantedauthorites
       
           return http.build();
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter()); // converter
        return converter;
    }
}
