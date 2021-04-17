package com.hanker.hankertaco.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
            .authorizeRequests()
                .antMatchers("/design", "/orders")
                    .access("hasRole('ROLE_USER')")
                .antMatchers("/","/**").permitAll()
                .antMatchers("/h2-console/**").permitAll()
            .and()
                .formLogin()
                .loginPage("/login")
            .and()
                .logout()
                .logoutSuccessUrl("/")
            .and()
                .csrf()
                .ignoringAntMatchers("/h2-console/**").disable();
    }

//    @Override
//    public void configure(AuthenticationManagerBuilder auth) throws Exception{
//        auth.inMemoryAuthentication()
//                .withUser("user1")
//                .password("{noop}password1")
//                .authorities("ROLE_USER")
//                .and()
//                .withUser("user2")
//                .password("{noop}password2")
//                .authorities("ROLE_USER");
//    }
    // Security 무시하기
    public void configure(WebSecurity web)throws Exception{
        web.ignoring().antMatchers("/h2-console/**");
    }

    // JDBC 기반의 사용자 스토어 인증
//    @Override
//    public void configure(AuthenticationManagerBuilder auth) throws Exception{
//        auth.jdbcAuthentication()
//                .dataSource(dataSource)
//                .usersByUsernameQuery(
//                        "select username, password, enabled from users " +
//                                "where username=?"
//                )
//                .authoritiesByUsernameQuery(
//                        "select username, authority from authorities " +
//                                "where username = ?"
//                )
//                .passwordEncoder(new NoEncodingPasswordEncoder());
//    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(encoder());
    }

}
