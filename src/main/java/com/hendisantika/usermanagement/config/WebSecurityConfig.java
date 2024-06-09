package com.hendisantika.usermanagement.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Created by IntelliJ IDEA.
 * Project : user-management
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 22/03/20
 * Time: 10.08
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final String[] PUBLIC_LINK = new String[]{
            "/include/**", "/css/**", "/icons/**", "/img/**", "/js/**", "/layer/**", "/static/**"
    };

    private final PasswordEncoder bCryptPasswordEncoder;

    private final UserDetailsService userDetailsService;

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userDetailsService);
        auth.setPasswordEncoder(bCryptPasswordEncoder);
        return auth;
    }


    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> req
                                .requestMatchers(PUBLIC_LINK).permitAll()
                                .requestMatchers("/", "/index", "/signup").permitAll()
                .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
                .loginPage("/login")
                .permitAll()
                .defaultSuccessUrl("/userForm")
                .failureUrl("/login?error=true")
                .usernameParameter("username")
                .passwordParameter("password")

                )
                .logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .logoutSuccessUrl("/login?logout")
                                .permitAll()
                );

//        http
//                .authorizeHttpRequests(authorize ->
//                        authorize
//                                .requestMatchers(PUBLIC_LINK).permitAll()
//                                .requestMatchers("/", "/index", "/signup").permitAll()
//                                .anyRequest().authenticated()
//                )
//
//                formLogin()
//                .loginPage("/login")
//                .permitAll()
//                .defaultSuccessUrl("/userForm")
//                .failureUrl("/login?error=true")
//                .usernameParameter("username")
//                .passwordParameter("password")
//        return http.build();
//        http.csrf().disable()
//                .authorizeHttpRequests((authorize) ->
//                        authorize.anyRequest().authenticated()
//                ).formLogin(
//                        form -> form
//                                .loginPage("/login")
//                                .loginProcessingUrl("/login")
//                                .defaultSuccessUrl("/welcome")
//                                .permitAll()
//                ).logout(
//                        logout -> logout
//                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//                                .permitAll()
//                );
        return http.build();
    }
}
