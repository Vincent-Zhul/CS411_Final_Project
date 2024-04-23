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
                .csrf().disable() // 禁用CSRF
                .authorizeRequests()
                .antMatchers("/", "/register").permitAll() // 允许所有用户访问主界面和注册页面
                .antMatchers("/user/**").authenticated() // "/user/**" 所有访问该路径的用户都需要认证
                .antMatchers("/admin/**").hasRole("ADMIN") // 只有ADMIN角色的用户才可以访问/admin/**路径
                .anyRequest().authenticated() // 其他所有请求都需要认证
                .and()
                .formLogin()
                .loginPage("/login")
                .failureUrl("/login?error=true") // 登录失败后重定向到登录页面，并附带错误参数
                .successHandler((request, response, authentication) -> {
                    Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
                    if (roles.contains("ROLE_ADMIN")) {
                        response.sendRedirect("/admin/dashboard"); // 管理员重定向到管理员仪表板
                    } else if (roles.contains("ROLE_USER")) {
                        response.sendRedirect("/user/dashboard"); // 普通用户重定向到用户仪表板
                    } else {
                        response.sendRedirect("/"); // 无特定角色，重定向到主页
                    }
                })
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout") // 设置触发登出操作的URL
                .logoutSuccessUrl("/") // 登出成功后重定向到主页面
                .invalidateHttpSession(true) // 使当前会话无效
                .clearAuthentication(true) // 清除认证信息
                .deleteCookies("JSESSIONID") // 删除JSESSIONID cookie
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
    
    public int getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            String username = ((UserDetails) authentication.getPrincipal()).getUsername();
            UserDAO userDAO = null;
            return userDAO.findUserIdByUsername(username);
        }
        return -1;  // 用户未登录或无法获取用户信息
    }

    @SuppressWarnings("deprecation")
    public PasswordEncoder passwordEncoder() {
        // 使用NoOpPasswordEncoder，这表示不对密码进行加密处理
        return NoOpPasswordEncoder.getInstance();
    }
}