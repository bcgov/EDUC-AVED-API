package ca.bc.gov.educ.api.aved.config;

import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * The type Pen my ed apimvc config.
 */
@Configuration
public class AvedAPIMVCConfig implements WebMvcConfigurer {

  /**
   * The Interceptor.
   */
  @Getter(AccessLevel.PRIVATE)
  private final AvedAPIInterceptor interceptor;

  /**
   * Instantiates a new Pen my ed apimvc config.
   *
   * @param interceptor the interceptor
   */
  @Autowired
  public AvedAPIMVCConfig(final AvedAPIInterceptor interceptor) {
    this.interceptor = interceptor;
  }

  /**
   * Add interceptors.
   *
   * @param registry the registry
   */
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(interceptor).addPathPatterns("/**");
  }
}
