package org.ovida.auth.infra.springsecurity;

import lombok.Data;
import org.ovida.auth.app.component.TokenCryptoEngine;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Data
@Configuration
@EnableWebSecurity
@ConfigurationProperties("app.infra.springsecurity")
public class SpringSecurityConfig {

  // WARNING: for demonstration purposes only, please inject the key from env var in production
  private String jwtKey = "ugw0WKDpKNSEEtFq1RYsOmLh5T162UVN88eDMhFpCOYgZ/3sQ6oV48mCZOieHu5LFU8oIk3NZGNCdtJpYF5p1w==";

  @Bean
  public SecurityFilterChain securityFilterChain(
    HttpSecurity httpSecurity,
    AuthenticationEntryPoint authenticationEntryPoint,
    AccessDeniedHandler accessDeniedHandler,
    TokenCryptoEngine tokenCryptoEngine) throws Exception {
    httpSecurity
      .csrf(AbstractHttpConfigurer::disable)
      .cors(AbstractHttpConfigurer::disable)
      .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .exceptionHandling(eh -> eh
        .authenticationEntryPoint(authenticationEntryPoint)
        .accessDeniedHandler(accessDeniedHandler))
      .addFilterBefore(new AuthTokenFilter(tokenCryptoEngine), UsernamePasswordAuthenticationFilter.class)
      .authorizeHttpRequests(authorizeRequests -> authorizeRequests
        .requestMatchers("/v?/tokens").permitAll()
        .requestMatchers(HttpMethod.GET, "/v?/accounts").hasAuthority("READ_ACCOUNT")
        .requestMatchers(HttpMethod.PATCH, "/v?/roles").hasAuthority("WRITE_ROLE")
        .anyRequest().authenticated());

    return httpSecurity.build();
  }
}