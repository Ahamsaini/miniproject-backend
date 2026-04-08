package com.mainApp.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthenticationFilter;
        private final UserDetailsService userDetailsService;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                // CORS must be configured first
                                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                                .csrf(csrf -> csrf.disable())
                                .authorizeHttpRequests(auth -> auth
                                                // Explicitly permit OPTIONS requests for CORS preflight
                                                .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**")
                                                .permitAll()

                                                // Public endpoints - no authentication required
                                                .requestMatchers(
                                                                "/auth/**",
                                                                "/courses/all",
                                                                "/swagger-ui/**",
                                                                "/v3/api-docs/**",
                                                                "/webjars/**")
                                                .permitAll()

                                                // Student endpoints
                                                .requestMatchers(
                                                                "/students/{id}/attendance/**",
                                                                "/students/{id}/subjects")
                                                .hasAnyRole("STUDENT", "ADMIN", "TEACHER", "HOD")

                                                // Allow students to view their own profile
                                                .requestMatchers(org.springframework.http.HttpMethod.GET, "/students/*")
                                                .hasAnyRole("STUDENT", "ADMIN", "TEACHER", "HOD")

                                                // Allow teachers to view student lists (for attendance, etc.)
                                                .requestMatchers(org.springframework.http.HttpMethod.GET, "/students")
                                                .hasAnyRole("ADMIN", "TEACHER", "HOD")

                                                .requestMatchers("/students/**").hasAnyRole("ADMIN", "HOD")

                                                // Teacher endpoints - code generation and finalization
                                                .requestMatchers(
                                                                "/labs/sessions/*/attendance/manual",
                                                                "/labs/sessions/*/code",
                                                                "/labs/sessions/*/finalize")
                                                .hasAnyRole("TEACHER", "ADMIN", "HOD")

                                                // Student endpoints - allow reading sessions and submitting attendance
                                                .requestMatchers(org.springframework.http.HttpMethod.GET,
                                                                "/labs/sessions/**")
                                                .hasAnyRole("STUDENT", "ADMIN", "TEACHER", "HOD")

                                                .requestMatchers(org.springframework.http.HttpMethod.POST,
                                                                "/labs/sessions/attendance/mark")
                                                .hasAnyRole("STUDENT", "ADMIN", "TEACHER", "HOD")

                                                // Lab endpoints
                                                .requestMatchers(org.springframework.http.HttpMethod.GET, "/labs",
                                                                "/labs/**")
                                                .hasAnyRole("ADMIN", "TEACHER", "STUDENT", "HOD")
                                                .requestMatchers("/labs/**")
                                                .hasAnyRole("ADMIN", "TEACHER", "HOD")

                                                // Teacher endpoints
                                                .requestMatchers(org.springframework.http.HttpMethod.GET, "/teachers",
                                                                "/teachers/**")
                                                .hasAnyRole("ADMIN", "TEACHER", "HOD")

                                                // Dashboard endpoints
                                                .requestMatchers("/dashboard/**", "/courses/dashboard/**")
                                                .hasAnyRole("ADMIN", "TEACHER", "STUDENT", "HOD")

                                                // Courses and subjects - allow all authenticated users to read
                                                .requestMatchers("/courses/**", "/subjects/**")
                                                .hasAnyRole("ADMIN", "TEACHER", "STUDENT", "HOD")

                                                // All other requests require authentication
                                                .anyRequest().authenticated())
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authenticationProvider(authenticationProvider())
                                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        @Bean
        public AuthenticationProvider authenticationProvider() {
                DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);

                authProvider.setPasswordEncoder(passwordEncoder());
                return authProvider;
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
                return config.getAuthenticationManager();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();

                // Use setAllowedOriginPatterns instead of setAllowedOrigins when
                // allowCredentials is true
                configuration.setAllowedOriginPatterns(List.of(
                                "http://localhost:3000",
                                "http://localhost:5173", // Vite default port
                                "http://localhost:8080",
                                "http://192.168.1.6",
                                "http://103.57.178.66",
                                "http://*.trycloudflare.com",
                                "https://*.trycloudflare.com",
                                "http://*.cloudflare.com",
                                "https://*.cloudflare.com",
                                "https://api.aham-sug-lab.work.gd",
                                "http://api.aham-sug-lab.work.gd",
                                "https://web.aham-sug-lab.work.gd",
                                "http://web.aham-sug-lab.work.gd",
                                "https://miniproject-frountend-zxir.vercel.app"));

                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                configuration.setAllowedHeaders(List.of("*")); // Allow all headers
                configuration.setExposedHeaders(List.of("Authorization"));
                configuration.setAllowCredentials(true);
                configuration.setMaxAge(3600L);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }
}