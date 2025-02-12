package com.recovery.managementsystem.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private CustomLoginSuccessHandler customLoginSuccessHandler; 

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> {
            auth.antMatchers(
                "/login/**",
                "/register/**",
                "/success/**",
                "/reset-password/**",
                "/forgot-password/**",
                "/css/**",
                "/js/**",
                "/lib/**",
                "/images/**",
                "/webjars/**"
            ).permitAll();
            auth.antMatchers("/dashboard/**").hasRole("USER");
            auth.antMatchers("/admin/**").hasRole("ADMIN");
            auth.anyRequest().authenticated();
        });

        http.csrf(AbstractHttpConfigurer::disable);

        http.formLogin(form -> {
            form.loginPage("/login")
                .successHandler(customLoginSuccessHandler) // Use the custom success handler
                .failureForwardUrl("/login?error=true")
                .usernameParameter("employeeId")
                .passwordParameter("password");
        });

        http.logout(logoutForm -> {
            logoutForm.logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true");
        });

        http.sessionManagement(session -> {
            session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
            session.maximumSessions(1);
        });

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(customUserDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
