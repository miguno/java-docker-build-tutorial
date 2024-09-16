package com.miguno.javadockerbuild.security;

import java.util.UUID;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.servlet.DispatcherType;
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

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.POST;

/** Secures the endpoints of this application. */
@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
public class AppSecurityConfiguration {

  private final AdminServerProperties adminServer;

  @Value("${app.spring-boot-admin.role.user.name}")
  private String roleUserName;

  @Value("${app.spring-boot-admin.role.user.password}")
  private String roleUserPassword;

  @Value("${app.spring-boot-admin.role.admin.name}")
  private String roleAdminName;

  @Value("${app.spring-boot-admin.role.admin.password}")
  private String roleAdminPassword;

  @SuppressFBWarnings("EI_EXPOSE_REP2")
  public AppSecurityConfiguration(AdminServerProperties adminServer, SecurityProperties security) {
    this.adminServer = adminServer;
  }

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
    successHandler.setDefaultTargetUrl(this.adminServer.path("/"));

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
                    //// For the Spring Boot Admin server.
                    .requestMatchers(
                        // Permit public access to all static assets.
                        new AntPathRequestMatcher(this.adminServer.path("/assets/**")),
                        // Permit public access to the login page.
                        new AntPathRequestMatcher(this.adminServer.path("/login")))
                    .permitAll()
                    // Permit asynchronous processing of a request without requiring authentication.
                    // FIXME: Permitting any async requests as a workaround appears dangerous.
                    // https://github.com/spring-projects/spring-security/issues/11027 (from 2022)
                    .dispatcherTypeMatchers(DispatcherType.ASYNC)
                    .permitAll()

                    //// For the Spring Boot Admin client (the "real" app being developed).
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

                    //// Applies to both SBA server and clients.
                    // All other requests must be authenticated.
                    .anyRequest()
                    .authenticated())
        // For Spring Boot Admin server: enables form-based login and logout.
        .formLogin(
            (formLogin) ->
                formLogin.loginPage(this.adminServer.path("/login")).successHandler(successHandler))
        .logout((logout) -> logout.logoutUrl(this.adminServer.path("/logout")))
        // Enables HTTP Basic Authentication support.
        .httpBasic(Customizer.withDefaults());

    // Enables CSRF-Protection using cookies.
    http.addFilterAfter(new CustomCsrfFilter(), BasicAuthenticationFilter.class)
        .csrf(
            (csrf) ->
                csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                    .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                    .ignoringRequestMatchers(
                        //// For the Spring Boot Admin server.
                        // Disables CSRF-Protection for the SBA server's endpoints that the SBA
                        // client uses to (de-)register.
                        new AntPathRequestMatcher(
                            this.adminServer.path("/instances"), POST.toString()),
                        new AntPathRequestMatcher(
                            this.adminServer.path("/instances/*"), DELETE.toString()),

                        //// For the Spring Boot Admin client.
                        // Disables CSRF-Protection for the SBA client's actuator endpoints that
                        // the SBA server uses to collect metrics.
                        new AntPathRequestMatcher("/actuator/**")));

    http.rememberMe(
        (rememberMe) -> rememberMe.key(UUID.randomUUID().toString()).tokenValiditySeconds(1209600));

    return http.build();
  }

  /** Required to provide UserDetailsService for "remember functionality". */
  @Bean
  public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
    // NOTE: Because this example project runs the Spring Boot Admin server and client in the same
    //       application, both the server's secured (with HTTP Basic Authentication) SBA API
    //       endpoint and the client's Spring actuator endpoints coincidentally require exactly the
    //       same username/password combination.
    //       In production, this is not recommended. See the recommendations of Spring Boot Admin at
    //       https://docs.spring-boot-admin.com/current/faq.html.
    //       Instead, in production you would separate clients from the server, and thus different
    //       username/password combinations can be used.
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
