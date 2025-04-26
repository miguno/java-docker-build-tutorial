package com.miguno.javadockerbuild.security;

import java.io.IOException;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

/** A custom CSRF Filter, derived from the Spring Boot Admin documentation. */
public class CustomCsrfFilter extends OncePerRequestFilter {

  public static final String CSRF_COOKIE_NAME = "XSRF-TOKEN";

  @SuppressFBWarnings(
      value = "COOKIE_USAGE",
      justification =
          "CSRF tokens are designed to be stored in cookies with appropriate security controls")
  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    if (csrf != null) {
      Cookie cookie = WebUtils.getCookie(request, CSRF_COOKIE_NAME);
      String token = csrf.getToken();

      if (cookie == null || token != null && !token.equals(cookie.getValue())) {
        cookie = new Cookie(CSRF_COOKIE_NAME, token);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);
      }
    }
    filterChain.doFilter(request, response);
  }
}
