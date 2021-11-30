package com.example.FootballLeagues.web.interceptor;

import com.example.FootballLeagues.service.StatisticService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//This interceptor is giving information about every request.
// In the statistic view you can see the count, if you are ADMIN.
@Component
public class StatisticInterceptor implements HandlerInterceptor {

    private final StatisticService statisticService;

    public StatisticInterceptor(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        statisticService.onRequest();
        return true;
    }
}
