package com.example.passin.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;


    public WebSecurityConfiguration(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    @Bean
    public JwtRequestFilter jwtRequestFilter() {
        return new JwtRequestFilter();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers(HttpMethod.OPTIONS, "/**")
                .antMatchers("/app/**/*.{js,html}")
                .antMatchers("/i18n/**")
                .antMatchers("/content/**")
                .antMatchers("/swagger-ui/index.html")
                .antMatchers("/test/**");
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.cors().and().csrf().disable().headers().and()
                .authorizeRequests()
                .antMatchers("/api/users/register","/api/users/login","/api/users/token-validate").permitAll()
                .antMatchers("/").hasAnyAuthority("ADMIN")
                .antMatchers("**/save").hasAnyAuthority("ADMIN")
                .antMatchers("**/edit").hasAnyAuthority("ADMIN")
                .antMatchers("**/delete").hasAnyAuthority("ADMIN")
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        httpSecurity.addFilterBefore(jwtRequestFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
