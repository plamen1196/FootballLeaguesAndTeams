package com.example.FootballLeagues.web;

import com.example.FootballLeagues.service.StatisticService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class StatisticController {

  private final StatisticService statisticService;

  public StatisticController(StatisticService statisticService) {
    this.statisticService = statisticService;
  }


  @GetMapping("/statistics")
  public ModelAndView statistics() {
    ModelAndView modelAndView = new ModelAndView();
    modelAndView.addObject("statistic", statisticService.getStats());
    modelAndView.setViewName("statistic");
    return modelAndView;
  }

}
