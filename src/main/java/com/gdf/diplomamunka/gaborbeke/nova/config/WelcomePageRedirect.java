package com.gdf.diplomamunka.gaborbeke.nova.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WelcomePageRedirect implements WebMvcConfigurer {

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {

    registry.addViewController("/")
        .setViewName("forward:/home.xhtml");

    registry.setOrder(Ordered.HIGHEST_PRECEDENCE);

    WebMvcConfigurer.super.addViewControllers(registry);
 }
}
