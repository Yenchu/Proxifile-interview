package idv.demo.backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import idv.demo.backend.security.TokenAuthenticationProvider;
import idv.demo.backend.security.UnAuthEntryPoint;
import idv.demo.backend.web.AuthFilter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Log4j2
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired private ObjectMapper objectMapper;

  @Bean
  public AuthFilter authFilter() {

    return new AuthFilter();
  }

  @Bean
  public UnAuthEntryPoint unAuthEntryPoint() {

    return new UnAuthEntryPoint(objectMapper);
  }

  @Bean
  public AuthenticationProvider authProvider() {

    return new TokenAuthenticationProvider();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {

    auth.authenticationProvider(authProvider());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http.csrf()
        .disable()
        .exceptionHandling()
        .authenticationEntryPoint(unAuthEntryPoint())
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .headers()
        .frameOptions()
        .sameOrigin()
        .and()
        .authorizeRequests()
        .requestMatchers(EndpointRequest.toAnyEndpoint())
        .permitAll()
        .antMatchers(HttpMethod.OPTIONS, "/**")
        .permitAll()
        .antMatchers("/", "/docs/**", "/favicon.ico")
        .permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .addFilterBefore(authFilter(), UsernamePasswordAuthenticationFilter.class);
  }
}
