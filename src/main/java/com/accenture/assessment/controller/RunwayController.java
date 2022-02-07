package com.accenture.assessment.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.accenture.assessment.model.Airport;
import com.accenture.assessment.model.Country;
import com.accenture.assessment.model.Runway;
import com.accenture.assessment.service.AirportService;
import com.accenture.assessment.service.CountryService;
import com.accenture.assessment.service.RunwayService;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller to retrieve the Runway details for the respective Airport being searched by the user.
 */
@Slf4j
@Controller
public class RunwayController {

	@Autowired
	private CountryService countryService;

	@Autowired
	private AirportService airportService;

	@Autowired
	private RunwayService runwayService;

	/**
	 * Runway Controller
	 *
	 * @return the runway
	 */
	@RequestMapping("/runway")
	public String query() {
		log.info("Runway");
		return "runway";
	}

	/**
	 * Runway Controller endpoint with the search country
	 *
	 * @param countryParam the country being searched
	 * @param model the model to add the attributes
	 * @return String
	 */
	@RequestMapping("/runway/{countryParam}")
	public String runwayCountry(@PathVariable String countryParam, Model model) {
		log.info("Runway for {}", countryParam);
		Optional<Country> country = countryService.fetchCountry(countryParam);
		if (country.isPresent()) {
			log.debug("Found country {} for {}", country.get(), countryParam);
			data(country.get(), model);
		} else {
			log.info("No country found for {}", countryParam);
			model.addAttribute("error", "Invalid Country " + countryParam);
		}
		return "runway";
	}

	/**
	 * Integrated Data values
	 *
	 * @param country the country entered by the user
	 * @param model the model to add the attributes
	 */
	protected void data(@NonNull Country country, @NonNull Model model) {
		log.debug("Country and the Airport Data {}", country);
		List<Airport> airports = airportService.fetchAirport(country);
		Map<Long, Airport> airportMap = new HashMap<>();
		airports.forEach(a -> {
			airportMap.put(a.getId(), a);
			a.setRunways(new ArrayList<>());
		});
		List<Runway> runways = runwayService.getRunwaysForAirports(airports);
		runways.forEach(r -> airportMap.get(r.getAirportId()).getRunways().add(r));
		model.addAttribute("country", country);
		model.addAttribute("airports", airports);
	}


}
