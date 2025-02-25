package main.java.pfe.mandomati.rhms.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;


@Configuration
public class SecurityConfig {

    private static final String[] AUTH_WHITELIST = {
        "/auth/login" // Allow unrestricted access to the auth endpoints
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(request -> new CorsConfiguration(corsFilter())))
                .csrf(csrf -> csrf.disable()) // Désactiver CSRF pour API stateless
                .authorizeHttpRequests(req -> req
                        .requestMatchers(AUTH_WHITELIST).permitAll() // Autoriser l'accès au login
                        //.requestMatchers("/api/user/**").hasAnyRole("ADMIN", "ROOT", "RH")
                        //.requestMatchers("/api/register").hasAnyRole("ADMIN", "ROOT", "RH") // Restreindre l'accès à l'inscription
                        .anyRequest().authenticated() // Protéger tous les autres endpoints
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // API stateless
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                )
                .build();
    }
    
    private CorsConfiguration corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:3000"); // Adjust allowed origins as needed
        config.addAllowedOrigin("http://localhost:5173");
        config.addAllowedOrigin("http://84.247.189.97:8443");
        config.addAllowedOrigin("https://auth-web-peach.vercel.app"); // Add your frontend origin
        config.addAllowedOriginPattern("*"); // Allow all origins
        config.addAllowedHeader("*");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("DELETE");
        return config;
    }
}
