package com.experiencers.server.smj.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class JwtSecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthTokenProvider jwtAuthTokenProvider;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(jwtUserDetailsService);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs",
                "/swagger-resources/**",
                "/swagger-ui.html",
                "/webjars/**",
                "/static/**",
                "/**/favicon.ico",
                "/h2-console/**",
                "/webjars/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/api/auth/**","/admin/**","/auth/kakao/**","/test").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtAuthTokenProvider), UsernamePasswordAuthenticationFilter.class);

    }
}
