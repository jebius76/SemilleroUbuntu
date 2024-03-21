package com.semillero.ubuntu.config;

import com.semillero.ubuntu.security.JwtAuthorizationFilter;
import com.semillero.ubuntu.security.JwtUtil;
import com.semillero.ubuntu.security.OAuth2SuccessHandler;
import com.semillero.ubuntu.service.OAuth2UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * Application security configuration.
 * @Configuration is used to indicate that contains configuration methods to
 * build de application context.
 * @EnablaWebSecurity activate the web security functionality and lets do
 * the configuration.
 *
 */
@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {
    private JwtUtil jwtUtil;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtAuthorizationFilter jwtAuthorizationFilter;
    @Autowired
    private OAuth2UserService oAuth2UserService;
    @Autowired
    private PasswordEncoderConfig passwordEncoder;
    @Autowired
    private OAuth2SuccessHandler oAuth2SuccessHandler;

    /**
     * Http request security filter.
     * This method defines the security filter used by Jwt to protect
     * and authorize the several resources.
     *
     * @param http      Object used to configure the HTTP security.
     * @param authenticationManager   Object used to authenticate the requests.
     * @return  The configured security filter.
     * @throws Exception If there is any error configuring the filter.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {

        RequestMatcher publicUrls = new OrRequestMatcher(
                new AntPathRequestMatcher("/api/v1/hello/**"),
                new AntPathRequestMatcher("/api/v1/auth/**")
        );

        RequestMatcher adminUrls = new OrRequestMatcher(
                new AntPathRequestMatcher("/api/v1/admin/**")
        );

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    auth
                            .requestMatchers("/api/v1/auth/**").permitAll()
                            .requestMatchers("/api/v1/hello/**").permitAll()
                            .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                            .anyRequest().authenticated();
                        })
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(oauth -> oauth
                        .defaultSuccessUrl("/api/v1/auth/loggedIn",true)
                        .loginPage("/api/v1/auth/forbidden")
                        .failureUrl("/api/v1/auth/loginFailure")
                        .userInfoEndpoint(infoEndpoint -> infoEndpoint
                                .userService(oAuth2UserService)))

                .logout(logout -> logout
                        .logoutUrl("/api/v1/auth/logout")
                        .deleteCookies("AuthorizedUser")
                        .logoutSuccessUrl("/api/v1/auth/loggedOut")
                        .invalidateHttpSession(true)
                )
                .build();
    }

    /**
     * Authentication DAO provider configuration.
     *
     * @param userDetailsService Object used to obtain the AuthenticationManagerBuilder
     * @param passwordEncoder Password encrypt object used to verify the passwords.
     * @return  Configured DaoAuthenticationProvider
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) throws Exception {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }

    /**
     * Authentication administrator configuration.
     *
     * @param httpSecurity Object used to obtain the AuthenticationManagerBuilder
     * @return  Configured AuthenticationManager
     * @throws Exception If there is any error configuring the AuthenticationManager.
     */
    @Bean
    AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(authenticationProvider(userDetailsService, passwordEncoder.passwordEncoder())).build();
    }
}
