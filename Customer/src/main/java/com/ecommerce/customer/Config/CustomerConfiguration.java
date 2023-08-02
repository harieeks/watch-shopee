package com.ecommerce.customer.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class CustomerConfiguration {

     @Bean
    public UserDetailsService userDetailsService(){
         return new CustomerServiceConfig();
     }

     @Bean
    public BCryptPasswordEncoder passwordEncoder(){
         return new BCryptPasswordEncoder();
     }

     @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

         AuthenticationManagerBuilder authenticationManagerBuilder=
                 http.getSharedObject(AuthenticationManagerBuilder.class);

         authenticationManagerBuilder
                 .userDetailsService(userDetailsService())
                 .passwordEncoder(passwordEncoder());
         AuthenticationManager authenticationManager= authenticationManagerBuilder.build();

         http
                 .authorizeHttpRequests(authorize -> authorize
                         .requestMatchers("/static/**","/css/**","/fonts/**","/images/Banner-image/**","/js/**","/vendor/**","/images/**","/product-view/**","/index/**","/register")
                         .permitAll()
                         .requestMatchers("/shop/**")
                         .hasAuthority("CUSTOMER")
                         .anyRequest()
                         .authenticated()
                 )
                 .formLogin((form)-> form
                         .loginPage("/login")
                         .loginProcessingUrl("/do-login")
                         .defaultSuccessUrl("/index")
                         .permitAll()
                 )
                 .sessionManagement((session)->session
                         .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                         .invalidSessionUrl("/login")
                         .maximumSessions(1)
                         .maxSessionsPreventsLogin(false)
                 )
                 .logout((logout)->logout
                         .deleteCookies("JSESSIONID")
                         .invalidateHttpSession(true)
                         .clearAuthentication(true)
                         .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                         .logoutSuccessUrl("/login?logout")
                         .permitAll()
                 )
                 .authenticationManager(authenticationManager);
         return http.build();
     }
}
