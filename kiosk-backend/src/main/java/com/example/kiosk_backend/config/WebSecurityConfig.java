package com.example.kiosk_backend.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.example.kiosk_backend.jwt.JwtAuthenticationFilter;
import com.example.kiosk_backend.service.UserDetailService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

        private final UserDetailService userService;
        private final JwtAuthenticationFilter jwtAuthenticationFilter;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .cors().and()
                                .csrf().disable()
                                .authorizeHttpRequests(auth -> auth
                                                // 공개 엔드포인트
                                                .requestMatchers("/api/admin/login").permitAll()
                                                .requestMatchers("/login", "/signup", "/user", "/h2-console/**")
                                                .permitAll()
                                                // 결제 및 주문 API 공개
                                                .requestMatchers(HttpMethod.POST, "/api/orders").permitAll()
                                                .requestMatchers(HttpMethod.POST, "/api/payment/ready",
                                                                "/api/payment/approve")
                                                .permitAll()
                                                // 관리자 API 보호
                                                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                                                // 나머지 API 인증 필요
                                                .requestMatchers("/api/**", "/articles").authenticated()
                                                .anyRequest().permitAll())
                                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                                .exceptionHandling(ex -> ex
                                                .authenticationEntryPoint((req, res, ex1) -> res
                                                                .sendError(HttpServletResponse.SC_UNAUTHORIZED)))
                                .headers(headers -> headers.frameOptions().disable());

                return http.build();
        }

        // 개발용 CORS 설정
        @Bean
        public CorsFilter corsFilter() {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(List.of(
                                "http://localhost:5173",
                                "http://kiosktest.shop",
                                "https://kiosktest.shop",
                                "http://3.38.6.220:8081"));
                config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                config.setAllowedHeaders(List.of("*"));
                config.setAllowCredentials(true);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", config);
                return new CorsFilter(source);
        }

        @Bean
        public BCryptPasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
                return http.getSharedObject(AuthenticationManagerBuilder.class)
                                .userDetailsService(userService)
                                .passwordEncoder(passwordEncoder())
                                .and().build();
        }
}
