package com.student.lms.config;

import com.student.lms.security.CustomUserDetailsService;
import com.student.lms.security.JWTAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final JWTAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> {})
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
               // .requestMatchers("/api/v1/auth/**").permitAll() // Public Registration
            		// Allow only ADMIN + SUPER_ADMIN to register users
            		.requestMatchers(HttpMethod.POST, "/api/v1/auth/register").hasAuthority("USER_CREATE")
            		// Other auth endpoints are still public
            		.requestMatchers(
            		        "/api/v1/auth/login",
            		        "/api/v1/auth/verify-email",
            		        "/api/v1/auth/resend-token",
            		        "/api/v1/auth/forgot-password",
            		        "/api/v1/auth/reset-password"
            		).permitAll()

                .requestMatchers("/api/v1/courses/**").permitAll() // Public Course Access
                .requestMatchers(
                        "/v3/api-docs/**",
                        "/swagger-ui.html",
                        "/swagger-ui/**"
                ).permitAll()
                .requestMatchers("/api/v1/users").hasAuthority("USER_VIEW")
             // User Management Permissions
//                .requestMatchers("/api/v1/users/**").hasAnyAuthority(
//                        "USER_VIEW",
//                        "USER_EDIT",
//                        "USER_EDIT_SELF",
//                        "USER_DELETE",
//                        "USER_CREATE",
//                        "ROLE_ASSIGN",
//                        "USER_VIEW_SELF",
//                        "USER_VIEW_TEAM",
//                        "USER_VIEW_ENROLLED"
//                    )
//                // Role Management
//                .requestMatchers("/api/v1/roles/**").hasAuthority("ROLE_VIEW")
//                .requestMatchers("/api/v1/users/**/role").hasAuthority("ROLE_ASSIGN")
//
//                // View Users List
//                .requestMatchers("/api/v1/users").hasAnyAuthority(
//                        "USER_VIEW",
//                        "USER_VIEW_TEAM",
//                        "USER_VIEW_ENROLLED"
//                )
                
                // GET USERS
                .requestMatchers(HttpMethod.GET, "/api/v1/users/**").hasAnyAuthority(
                        "USER_VIEW", "USER_VIEW_TEAM", "USER_VIEW_ENROLLED", "USER_VIEW_SELF"
                )

                // CREATE USER
                .requestMatchers(HttpMethod.POST, "/api/v1/users/**").hasAuthority("USER_CREATE")

                // UPDATE USER
                .requestMatchers(HttpMethod.PUT, "/api/v1/users/**").hasAnyAuthority(
                        "USER_EDIT", "USER_EDIT_SELF"
                )

                // DELETE USER
                .requestMatchers(HttpMethod.DELETE, "/api/v1/users/**").hasAuthority("USER_DELETE")

                // Role Assignment
                .requestMatchers("/api/v1/users/**/role").hasAuthority("ROLE_ASSIGN")

                .anyRequest().authenticated() // All other requests need authentication
            );

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
