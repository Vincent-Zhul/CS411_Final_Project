package com.example.cs411_final_project.config;

import com.example.cs411_final_project.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Set;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Prohibit CSRF
                .authorizeRequests()
                .antMatchers("/", "/register").permitAll() // Allow all users to access
                .antMatchers("/user/**").authenticated() // All users accessing this path require authentication
                .antMatchers("/admin/**").hasRole("ADMIN") // Admin authority
                .anyRequest().authenticated() // All other requests require authentication
                .and()
                .formLogin()
                .loginPage("/login")
                .failureUrl("/login?error=true") // Redirect to the login page with error parameters after login failure
                .successHandler((request, response, authentication) -> {
                    Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
                    if (roles.contains("ROLE_ADMIN")) {
                        response.sendRedirect("/admin/dashboard"); // Administrator redirects to administrator dashboard
                    } else if (roles.contains("ROLE_USER")) {
                        response.sendRedirect("/user/dashboard"); // Redirect regular users to user dashboard
                    } else {
                        response.sendRedirect("/"); // No specific role, redirect to homepage
                    }
                })
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout") // Set the URL that triggers the logout operation
                .logoutSuccessUrl("/") // Redirect to the main page after successful login
                .invalidateHttpSession(true) // Invalidate the current session
                .clearAuthentication(true) // Clear authentication information
                .deleteCookies("JSESSIONID") // Delete cookies
                .permitAll();
    }

    @Bean
    public SimpleUrlAuthenticationFailureHandler customFailureHandler() {
        return new SimpleUrlAuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws ServletException, IOException {
                String errorMessage = "SOMETHING WRONG WITH THE USERNAME OR PASSWORD, PLEASE RE-CHECK";
                if (exception.getMessage().equalsIgnoreCase("User is disabled")) {
                    errorMessage = "THIS USER DOES NOT HAVE ADMINISTRATOR RIGHTS, PLEASE CHECK THE USERNAME";
                }
                setDefaultFailureUrl("/login?error=" + errorMessage);
                super.onAuthenticationFailure(request, response, exception);
            }
        };
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource)
                .passwordEncoder(passwordEncoder())  // Support plaintext password verification
                .usersByUsernameQuery("select username, password, 'true' as enabled from User where username=?")
                .authoritiesByUsernameQuery("select username, role from Authority where username=?");
    }

    public int getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            String username = ((UserDetails) authentication.getPrincipal()).getUsername();
            UserDAO userDAO = null;
            return userDAO.findUserIdByUsername(username);
        }
        return -1;  // User not logged in or unable to obtain user information
    }

    @SuppressWarnings("deprecation")
    public PasswordEncoder passwordEncoder() {
        // Do not encrypt passwords
        return NoOpPasswordEncoder.getInstance();
    }
}