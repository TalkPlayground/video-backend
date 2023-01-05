package com.playground.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playground.service.impl.MemberServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    AuthEntryPoint authEntryPoint;
    @Autowired
    MemberServiceImpl memberService;
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(memberService).passwordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().headers().frameOptions().disable();
        http.authorizeRequests().antMatchers("/v1/user/login", "/v1/user/register", "/v1/user/otp/verify", "/v1/user/email/verify",
                        "/v1/user/session/join", "/v1/user/session/store", "/v1/user/session/transcript/files", "/v1/user/session/recording","/v1/user/session/frontend/loggers","/v1/user/transcripts/delete/statusChange","/v1/user/airtableCL/errorLog")
                .permitAll();
        http.authorizeRequests().anyRequest().authenticated();
        http.exceptionHandling().authenticationEntryPoint(authEntryPoint);
        http.addFilterAfter(new PerRequestFilter(jwtTokenUtil, handlerExceptionResolver, memberService), UsernamePasswordAuthenticationFilter.class);
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/swagger-ui.html/**", "/webjars/**", "/v2/**", "/swagger-resources/**");
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}