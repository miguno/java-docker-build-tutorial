package com.miguno.javadockerbuild.routes;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Redirects <code>/admin/</code> requests to <code>/admin</code>.
 *
 * <p>Spring treats paths like <code>foo/</code> vs. `<code>foo</code>` differently. This controller
 * ensures that a user is correctly redirected after a login.
 */
@SuppressFBWarnings("SPRING_ENDPOINT")
@Controller
@RequestMapping("/${spring.boot.admin.context-path}")
public class AdminRedirector {

  @Value("${spring.boot.admin.context-path}")
  private String adminPath;

  @GetMapping("/${spring.boot.admin.context-path}/")
  @SuppressFBWarnings("SPRING_FILE_DISCLOSURE")
  public ModelAndView redirectWithUsingRedirectPrefix(ModelMap model) {
    return new ModelAndView(String.format("redirect:/%s", adminPath), model);
  }
}
