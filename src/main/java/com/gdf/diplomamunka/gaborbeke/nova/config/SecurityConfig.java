package com.gdf.diplomamunka.gaborbeke.nova.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


  @Autowired
  private UserDetailsService userDetailsService;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    // require all requests to be authenticated except for the resources and registration
        http.authorizeRequests()
            .antMatchers("/javax.faces.resource/**").permitAll()
                .antMatchers("/registration").permitAll()
                .anyRequest().authenticated();

    // login
        http.formLogin()
            .loginPage("/login.xhtml")
            .permitAll()
            .successForwardUrl("/home.xhtml")
            .failureUrl("/login.xhtml?error=true");

    // logout
        http.logout()
            .logoutSuccessUrl("/login.xhtml");

    // not needed as JSF 2.2 is implicitly protected against CSRF
    http.csrf().disable();
  }

  @Autowired
  void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth .userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
  }

  @Bean
  public UserDetailsService userDetailsService() {
    return super.userDetailsService();
  }
}
