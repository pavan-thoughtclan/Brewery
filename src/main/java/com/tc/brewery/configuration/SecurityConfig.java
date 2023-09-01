package com.tc.brewery.configuration;

import com.tc.brewery.Jwt.JwtAuthenticationEntryPoint;
import com.tc.brewery.Jwt.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    @Autowired
    private JwtAuthenticationEntryPoint point;
    @Autowired
    private JwtAuthenticationFilter filter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/userpage",
                                "/adminpage",
                                "/userdetails/{userId}",
                                "/userwithaddress/{userId}",
                                "/vendor",
                                "/add_address/{userId}",
                                "/beerratings/{beerId}/{userId}",
                                "/get_cart/{userId}",
                                "/add_cart/{userId}",
                                "/beers/categories",
                                "/beers",
                                "/beers/categories/{category}",
                                "/beers/categories/{category}/{beerId}",
                                "/beers/{beerId}",
                                "/beers/Highrated",
                                "/beers/Moderaterated",
                                "/address/{user_id}",
                                "/foodratings/{foodId}/{userId}",
                                "/foodcategories/{category}",
                                "/foodcategories/{category}/{foodId}",
                                "/foods/{fodId}",
                                "/foodcategories",
                                "/list_all_carts",
                                "/update_cart_status/{cartId}",
                                "/get_current_cart/{userId}",
                                "/create_food"
                        ).authenticated()
                        .requestMatchers(
                                "/auth/login/passcode-login",
                                "/auth/login/otp-login1",
                                "/auth/login/otp-login2",
                                "/forgot-password",
                                "/verify-otp",
                                "/registration",
                                "/set-new-password",
                                "/registration",
                                "/welcome"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex.authenticationEntryPoint(point))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}