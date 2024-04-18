package com.example.cs411_final_project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/user/register").permitAll() // 允许所有用户访问主界面和注册页面
                .antMatchers("/admin/**").hasRole("ADMIN") // 只有ADMIN角色的用户才可以访问/admin/**路径
                .anyRequest().authenticated() // 其他所有请求都需要认证
                .and()
                .formLogin()
                .loginPage("/login")
                .failureUrl("/login?error=true") // 登录失败后重定向到登录页面，并附带错误参数
                .defaultSuccessUrl("/", true) // 登录成功后跳转到主界面
                .permitAll()
                .and()
                .logout()
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
                .passwordEncoder(passwordEncoder())  // 使用NoOpPasswordEncoder以支持明文密码验证
                .usersByUsernameQuery("select username, password, 'true' as enabled from User where username=?")
                .authoritiesByUsernameQuery("select username, authority from Authorities where username=?");
    }

    @SuppressWarnings("deprecation")
    public PasswordEncoder passwordEncoder() {
        // 使用NoOpPasswordEncoder，这表示不对密码进行加密处理
        return NoOpPasswordEncoder.getInstance();
    }
}

