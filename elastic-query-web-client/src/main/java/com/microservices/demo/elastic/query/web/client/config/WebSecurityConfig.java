package com.microservices.demo.elastic.query.web.client.config;

import com.microservices.demo.config.ElasticUserConfigData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final ElasticUserConfigData userConfigData;

    public WebSecurityConfig(ElasticUserConfigData userConfigData) {
        this.userConfigData = userConfigData;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/**").hasRole("USER")
                .anyRequest()
                .fullyAuthenticated();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser(userConfigData.getUser())
                .password(passwordEncoder().encode(userConfigData.getPassword()))
                .roles(userConfigData.getRoles());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
