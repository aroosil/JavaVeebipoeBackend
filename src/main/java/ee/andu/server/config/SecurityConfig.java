package ee.andu.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable)
//                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(HttpMethod.GET,"/public-persons").permitAll()
//                        .requestMatchers(HttpMethod.GET,"/person/*").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/products").permitAll()
//                        .requestMatchers(HttpMethod.GET,"/categories").permitAll()
//                        .requestMatchers(HttpMethod.POST,"/login").permitAll()
//                        .requestMatchers(HttpMethod.POST,"/signup").permitAll()
//                        .requestMatchers(HttpMethod.GET,"/products/*").permitAll()
//                        .requestMatchers(HttpMethod.GET,"/parcelmachines").permitAll()
//                        .requestMatchers(HttpMethod.POST,"/products").hasAuthority("ADMIN")
//                        .requestMatchers(HttpMethod.DELETE,"/products").hasAuthority("ADMIN")
//                        .requestMatchers(HttpMethod.PUT,"/products").hasAuthority("ADMIN")
//                        .requestMatchers(HttpMethod.POST,"/categories").hasAuthority("ADMIN")
//                        .requestMatchers(HttpMethod.DELETE,"/categories").hasAuthority("ADMIN")
//                        .requestMatchers(HttpMethod.GET,"/persons").hasAuthority("SUPERADMIN")
//                        .requestMatchers(HttpMethod.PATCH,"/change-admin").hasAuthority("SUPERADMIN")
//                        .anyRequest().authenticated())
//                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
//        return http.build();
//    }
//
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowedOrigins(List.of(frontendUrl)); // RestControllerist võin ära võtta @CrossOrigin
//        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH"));
//        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
//        config.setAllowCredentials(true);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", config);
//        return source;
//    }
}
