package com.example.FootballLeagues.config;

import com.example.FootballLeagues.web.interceptor.StatisticInterceptor;
import com.example.FootballLeagues.web.interceptor.TimeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

  private final StatisticInterceptor statisticInterceptor;
  private final TimeInterceptor timeInterceptor;

  public WebConfiguration(StatisticInterceptor statisticInterceptor, TimeInterceptor timeInterceptor) {
    this.statisticInterceptor = statisticInterceptor;
    this.timeInterceptor = timeInterceptor;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(statisticInterceptor);
    registry.addInterceptor(timeInterceptor);
  }
}
