package com.example.FootballLeagues.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

  public static final String DEFAULT_ERROR_VIEW = "errors/error-object-not-found";

  @ExceptionHandler({ObjectNotFoundException.class})
  public ModelAndView handleDbExceptions(ObjectNotFoundException e) {
    ModelAndView modelAndView = new ModelAndView(DEFAULT_ERROR_VIEW);
    modelAndView.addObject("errorMessage", e.getMessage());
    modelAndView.setStatus(HttpStatus.NOT_FOUND);
    return modelAndView;
  }

}
