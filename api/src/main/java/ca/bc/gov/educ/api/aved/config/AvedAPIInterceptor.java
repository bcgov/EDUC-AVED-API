package ca.bc.gov.educ.api.aved.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The type Pen my ed api interceptor.
 */
@Component
public class AvedAPIInterceptor implements AsyncHandlerInterceptor {

  /**
   * The constant log.
   */
  private static final Logger log = LoggerFactory.getLogger(AvedAPIInterceptor.class);

  /**
   * Pre handle boolean.
   *
   * @param request  the request
   * @param response the response
   * @param handler  the handler
   * @return the boolean
   */
  @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        if (request.getMethod() != null && request.getRequestURL() != null)
            log.info("{} {}", request.getMethod(), request.getRequestURL());
        if (request.getQueryString() != null)
            log.debug("Query string     : {}", request.getQueryString());
        return true;
    }

  /**
   * After completion.
   *
   * @param request  the request
   * @param response the response
   * @param handler  the handler
   * @param ex       the ex
   */
  @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, Exception ex) {
        int status = response.getStatus();
        if(status >= 200 && status < 300) {
            log.info("RESPONSE STATUS: {}", status);
        } else {
            log.error("RESPONSE STATUS: {}", status);
        }
    }
}
