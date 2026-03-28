package ee.andu.server.config;

import ee.andu.server.service.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {
    @Value("${frontend.url}")
    private String frontendUrl;

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(HttpMethod.GET,"/public-persons").permitAll()
//                        .requestMatchers(HttpMethod.GET,"/person/*").permitAll()
                        .requestMatchers(HttpMethod.GET,"/categories").permitAll()
                        .requestMatchers(HttpMethod.POST,"/login").permitAll()
                        .requestMatchers(HttpMethod.POST,"/signup").permitAll()
                        .requestMatchers(HttpMethod.GET, "/products").permitAll()
                        .requestMatchers(HttpMethod.GET,"/products/*").permitAll()
                        .requestMatchers(HttpMethod.GET,"/parcel-machines").permitAll()

                        .requestMatchers(HttpMethod.POST,"/products").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/products").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/products").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/categories").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/categories").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/admin-products").hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.GET,"/persons").hasAuthority("SUPERADMIN")
//                        .requestMatchers(HttpMethod.PATCH,"/change-admin").hasAuthority("SUPERADMIN")

                        .anyRequest().authenticated()) // GET orders, POST orders, GET check-payment
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(frontendUrl)); // RestControllerist võin ära võtta @CrossOrigin
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
