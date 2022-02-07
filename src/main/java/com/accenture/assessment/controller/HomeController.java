package com.accenture.assessment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;


/**
 *
 * Controller for redirecting to the Home page
 */
@Slf4j
@Controller
public class HomeController {

	@RequestMapping("/")
	public String home() {
		log.info("HomePage");
		return "homePage";
	}


}
