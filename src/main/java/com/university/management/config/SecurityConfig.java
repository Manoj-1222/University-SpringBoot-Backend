package com.university.management.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.university.management.security.CustomUserDetailsService;
import com.university.management.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    
    @Autowired
    private CustomUserDetailsService userDetailsService;
    
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    
    @Bean
    public SecurityFilterChain filterChain(@NonNull HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Public endpoints - authentication not required
                .requestMatchers("/auth/login", "/auth/register", "/auth/student-login").permitAll()
                .requestMatchers("/admin/auth/login", "/admin/auth/register").permitAll()
                .requestMatchers("/public/**").permitAll()
                .requestMatchers("/applications/submit").permitAll()
                .requestMatchers("/university/basic-info").permitAll()
                .requestMatchers("/health").permitAll()
                .requestMatchers("/").permitAll()  // Allow access to API root
                .requestMatchers("/actuator/health").permitAll()
                
                // Course endpoints - public read access for browsing courses
                .requestMatchers(HttpMethod.GET, "/courses/available").permitAll()
                .requestMatchers(HttpMethod.GET, "/courses/active").permitAll()
                .requestMatchers(HttpMethod.GET, "/courses/department/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/courses/program/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/courses/{id}").permitAll()
                
                // Protected auth endpoints - require authentication
                .requestMatchers("/auth/me", "/auth/change-password", "/auth/logout").authenticated()
                .requestMatchers("/admin/auth/me").hasAnyRole("STAFF_ADMIN", "SUPER_ADMIN")
                
                // Student endpoints - aligned with controller permissions
                .requestMatchers(HttpMethod.GET, "/students/me").hasRole("STUDENT")
                .requestMatchers(HttpMethod.PUT, "/students/me").hasRole("STUDENT")
                .requestMatchers(HttpMethod.GET, "/students/**").hasAnyRole("STAFF_ADMIN", "SUPER_ADMIN")
                .requestMatchers(HttpMethod.POST, "/students").hasRole("SUPER_ADMIN")
                .requestMatchers(HttpMethod.POST, "/students/register").hasAnyRole("STAFF_ADMIN", "SUPER_ADMIN")
                .requestMatchers(HttpMethod.PUT, "/students/*/academic").hasRole("STAFF_ADMIN")
                .requestMatchers(HttpMethod.PUT, "/students/**").hasRole("SUPER_ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/students/**").hasRole("SUPER_ADMIN")
                
                // Application/Admission endpoints - public submit, protected admin access
                .requestMatchers(HttpMethod.POST, "/applications/submit").permitAll()
                .requestMatchers(HttpMethod.GET, "/applications/**").hasAnyRole("STAFF_ADMIN", "SUPER_ADMIN")
                .requestMatchers(HttpMethod.POST, "/applications/**").hasAnyRole("STAFF_ADMIN", "SUPER_ADMIN")
                .requestMatchers(HttpMethod.PUT, "/applications/**").hasAnyRole("STAFF_ADMIN", "SUPER_ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/applications/**").hasRole("SUPER_ADMIN")
                
                .requestMatchers(HttpMethod.GET, "/admissions/**").hasAnyRole("STAFF_ADMIN", "SUPER_ADMIN")
                .requestMatchers(HttpMethod.POST, "/admissions/**").hasAnyRole("STAFF_ADMIN", "SUPER_ADMIN")
                .requestMatchers(HttpMethod.PUT, "/admissions/**").hasAnyRole("STAFF_ADMIN", "SUPER_ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/admissions/**").hasRole("SUPER_ADMIN")
                
                // Admin endpoints - Must come AFTER /admin/auth/** to avoid conflicts  
                .requestMatchers(HttpMethod.GET, "/admin/students/**", "/admin/courses/**").hasAnyRole("STAFF_ADMIN", "SUPER_ADMIN")
                .requestMatchers(HttpMethod.POST, "/admin/students/**", "/admin/courses/**").hasAnyRole("STAFF_ADMIN", "SUPER_ADMIN")
                .requestMatchers(HttpMethod.PUT, "/admin/students/**", "/admin/courses/**").hasAnyRole("STAFF_ADMIN", "SUPER_ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/admin/students/**", "/admin/courses/**").hasRole("SUPER_ADMIN")
                
                // University endpoints - public read, admin write
                .requestMatchers(HttpMethod.GET, "/university/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/university/**").hasRole("SUPER_ADMIN")
                .requestMatchers(HttpMethod.PUT, "/university/**").hasRole("SUPER_ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/university/**").hasRole("SUPER_ADMIN")
                
                // All other requests must be authenticated
                .anyRequest().authenticated()
            )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
    
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    
    @Bean
    public AuthenticationManager authenticationManager(@NonNull AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:5173",
            "http://localhost:5174", 
            "http://localhost:5175",
            "http://localhost:3000",
            "https://university-frontend-new.vercel.app"
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
