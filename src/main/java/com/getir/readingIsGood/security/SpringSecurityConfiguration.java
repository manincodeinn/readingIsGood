package com.getir.readingIsGood.security;

import com.getir.readingIsGood.model.exception.ReadingIsGoodException;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@SecurityScheme(
        type = SecuritySchemeType.HTTP,
        name = "basicAuth",
        scheme = "basic")
public class SpringSecurityConfiguration {

    private static final String[] AUTH_WHITE_LIST = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-resources/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(auth -> {
            try {
                auth.requestMatchers(AUTH_WHITE_LIST).permitAll()
                        .anyRequest().authenticated().and().httpBasic();
            } catch (Exception e) {
                throw new ReadingIsGoodException("Error occurred while authenticating.", e);
            }
        });

        httpSecurity.formLogin(Customizer.withDefaults());

        httpSecurity.csrf().disable();

        return httpSecurity.build();
    }

}
