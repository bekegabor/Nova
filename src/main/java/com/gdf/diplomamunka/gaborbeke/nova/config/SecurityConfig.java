package com.gdf.diplomamunka.gaborbeke.nova.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static com.sun.xml.internal.ws.encoding.SOAPBindingCodec.UTF8_ENCODING;

@Configuration
@EnableWebSecurity
@Component
public class SecurityConfig extends WebSecurityConfigurerAdapter {


  @Autowired
  private UserDetailsService userDetailsService;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean("authenticationManager")
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Bean
  public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
    StrictHttpFirewall firewall = new StrictHttpFirewall();
    firewall.setAllowUrlEncodedSlash(true);
    firewall.setAllowSemicolon(true);
    return firewall;
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    super.configure(web);
    // @formatter:off
    web.httpFirewall(allowUrlEncodedSlashHttpFirewall());
  }

  @Bean
  public AuthenticationSuccessHandler myAuthenticationSuccessHandler(){
    return new CustomSimpleUrlAuthenticationSuccessHandler();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    // require all requests to be authenticated except for the resources and registration
        http.authorizeRequests()
            .antMatchers("/javax.faces.resource/**").permitAll()
                .antMatchers("/registration").permitAll()
                .antMatchers("/admin/**").hasAnyAuthority("ADMIN")
                .antMatchers("/employee/**").hasAnyAuthority("EMPLOYEE")
                .antMatchers("/user/**").hasAnyAuthority("USER")
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().accessDeniedPage("/error/accessdenied/accessDenied.xhtml");

    // login
        http.formLogin()
            .loginPage("/login.xhtml")
            .permitAll()
            .successHandler(myAuthenticationSuccessHandler())
            .failureUrl("/login.xhtml?error=true");

    // logout
        http.logout()
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .logoutSuccessUrl("/login.xhtml");


    // not needed as JSF 2.2 is implicitly protected against CSRF
    http.csrf().disable();

    //Firefox deny loading pdf preview so need to turn off X-Frame-Options
    http.headers().frameOptions().disable();

  }

  @Autowired
  void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
  }

  @Bean
  public UserDetailsService userDetailsService() {
    return super.userDetailsService();
  }
}
