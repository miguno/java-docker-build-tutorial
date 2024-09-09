package com.miguno.javadockerbuild.controllers;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/** Implements a basic landing page at endpoint `/`. */
@SuppressFBWarnings("SPRING_ENDPOINT")
@RestController
public class RootController {

  @Value("${app.spring-boot-admin.role.user.name}")
  private String roleUserName;

  @Value("${spring.application.name}")
  private String appName;

  @Value("${app.spring-boot-admin.role.user.password}")
  private String roleUserPassword;

  @Value("${app.spring-boot-admin.role.admin.name}")
  private String roleAdminName;

  @Value("${app.spring-boot-admin.role.admin.password}")
  private String roleAdminPassword;

  /**
   * Returns a basic landing page for this application.
   *
   * @return Basic landing page in HTML format.
   */
  @GetMapping("/")
  @SuppressFBWarnings("VA_FORMAT_STRING_USES_NEWLINE")
  public String root() {
    return String.format(
        """
        <h1>Welcome to %s</h1>
        <p>Enjoy playing around with this application!</p>
        <h2>Example Endpoints</h2>
        <ul>
          <li><a href="/welcome"><code>/welcome</code></a> &mdash; this app's example endpoint</li>
          <li><a href="/actuator/health"><code>/actuator/health</code></a> &mdash; Spring built-in feature</li>
          <li><a href="/actuator/prometheus"><code>/actuator/prometheus</code></a> &mdash; Spring built-in feature</li>
          <li><a href="/admin">Spring Boot Admin dashboard</a> <strong>(requires login, see below)</strong></li>
          <li><a href="/swagger-ui.html">Swagger UI</a></li>
        </ul>
        <h2>User Accounts</h2>
        <p>For endpoints that require login.</p>
        <ul>
          <li>Admin user: <strong><code>%s</code></strong> with password <strong><code>%s</code></strong></li>
          <li>Regular user: <strong><code>%s</code></strong> with password <strong><code>%s</code></strong></li>
        </p>
        <ul>
        </ul>
        """,
        appName, roleAdminName, roleAdminPassword, roleUserName, roleUserPassword);
  }
}
