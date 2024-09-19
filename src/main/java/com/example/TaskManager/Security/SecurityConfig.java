package com.example.TaskManager.Security;

import com.example.TaskManager.Repository.AppUserRepository;
import com.example.TaskManager.Service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private AppUserService appUserService;

    @Bean
    public UserDetailsService userDetailsService(){
        return appUserService;
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(appUserService);
        provider.setPasswordEncoder(passwordEncoder());
        /*Encoding the input password because while registration, password is encrypted*/
        return provider;
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .formLogin(httpForm->{

                    httpForm
                        .loginPage("/login").permitAll()
                            .defaultSuccessUrl("/dashboard", true);  // Redirect to tasks after login

                })
                    .authorizeHttpRequests(registry->{
                        registry.requestMatchers("/show/tasks","/req/signup","/","/register","/css/**", "/js/**").permitAll();
                        registry.anyRequest().authenticated();
                    })

                    .build();
    }
}


//Authentication is done when users sign in only, and by default spring security web authenticates all of requests, so
//we need to make an exception when a user register, hence we need to make all requests acceptable when user register

/*This line specifies that the login page will be accessible at the URL /login. All users (authenticated or not) will be
 able to access this page (permitAll()).
Spring Security will automatically display a login form at this URL unless you create a custom one.*/

/*
registry.requestMatchers("/req/register").permitAll();
* Public Access to Registration:
* This line allows all users to access the URL /req/register without authentication (i.e., anyone can register).
*
 */

/*
registry.anyRequest().authenticated();
Authentication for All Other URLs: This line ensures that any other URL in the application will require
the user to be authenticated (logged in) to access it.
 */
