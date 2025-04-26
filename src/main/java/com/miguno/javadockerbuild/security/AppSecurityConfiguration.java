package com.miguno.javadockerbuild.security;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/** Secures the endpoints of this application. */
@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
public class AppSecurityConfiguration {

  @Value("${app.spring-boot-admin.role.user.name}")
  private String roleUserName;

  @Value("${app.spring-boot-admin.role.user.password}")
  private String roleUserPassword;

  @Value("${app.spring-boot-admin.role.admin.name}")
  private String roleAdminName;

  @Value("${app.spring-boot-admin.role.admin.password}")
  private String roleAdminPassword;

  public AppSecurityConfiguration(SecurityProperties security) {}

  /**
   * Applies security policies such as authentication requirements to endpoints.
   *
   * @param http Supplied by Spring.
   * @return The applications' security filter chain.
   * @throws Exception Unclear when that happens.
   */
  @Bean
  protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    SavedRequestAwareAuthenticationSuccessHandler successHandler =
        new SavedRequestAwareAuthenticationSuccessHandler();
    successHandler.setTargetUrlParameter("redirectTo");
    successHandler.setDefaultTargetUrl("/");

    // NOTE: In this project, the Spring Boot Admin server and client are colocated in the same
    //       application for demonstration purposes. In production, you would typically not do that
    //       and instead separate the code and functionality. See the recommendations of Spring Boot
    //       Admin at https://docs.spring-boot-admin.com/current/faq.html.
    //       The effect of this colocation is that this application contains endpoints for both
    //       server and client, and the authorization settings below also apply to both: if you
    //       permit access to a URL in the "for the server" section you also permit access for the
    //       client, and vice versa. Again, this would be different in production where the server
    //       and the clients would be separate applications and processes.
    http.authorizeHttpRequests(
            (authorizeRequests) ->
                authorizeRequests
                    .requestMatchers(
                        new AntPathRequestMatcher("/"),
                        // Permit public access to this app's example endpoint at `/welcome`.
                        new AntPathRequestMatcher("/welcome/**"),
                        // Permit public access to Swagger.
                        new AntPathRequestMatcher("/swagger-ui.html"),
                        new AntPathRequestMatcher("/v3/api-docs"),
                        // Permit public access to a subset of actuator endpoints.
                        new AntPathRequestMatcher("/actuator/health"),
                        new AntPathRequestMatcher("/actuator/info"),
                        new AntPathRequestMatcher("/actuator/prometheus"))
                    .permitAll()
                    // All other requests must be authenticated.
                    .anyRequest()
                    .authenticated())
        // Enables HTTP Basic Authentication support.
        .httpBasic(Customizer.withDefaults());

    // Enables CSRF-Protection using cookies.
    http.addFilterAfter(new CustomCsrfFilter(), BasicAuthenticationFilter.class)
        .csrf(
            (csrf) ->
                csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                    .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()));

    http.rememberMe(
        (rememberMe) -> rememberMe.key(UUID.randomUUID().toString()).tokenValiditySeconds(1209600));

    return http.build();
  }

  /** Required to provide UserDetailsService for "remember functionality". */
  @Bean
  public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
    // NOTE: HTTP Basic Authentication itself is not recommended for production.
    UserDetails user =
        User.withUsername(roleUserName)
            .password(passwordEncoder.encode(roleUserPassword))
            .roles("USER")
            .build();
    UserDetails admin =
        User.withUsername(roleAdminName)
            .password(passwordEncoder.encode(roleAdminPassword))
            .roles("ADMIN", "USER")
            .build();
    return new InMemoryUserDetailsManager(user, admin);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
