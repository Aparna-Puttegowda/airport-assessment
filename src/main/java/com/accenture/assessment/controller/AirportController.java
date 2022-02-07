package com.accenture.assessment.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.accenture.assessment.model.Airport;
import com.accenture.assessment.model.Country;
import com.accenture.assessment.model.Runway;
import com.accenture.assessment.service.AirportService;
import com.accenture.assessment.service.CountryService;
import com.accenture.assessment.service.RunwayService;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class AirportController {

	@Autowired
	private CountryService countryService;

	@Autowired
	private AirportService airportService;

	@Autowired
	private RunwayService runwayService;

	@ToString
	public static class CountryRunway {
		public final Country country;
		public Set<String> runways = new HashSet<>();
		public Set<String> runwayDetails = new HashSet<>();

		CountryRunway(Country country) {
			this.country = country;
		}
	}
	/**
	 *
	 * To Display the Airport details - top 10 countries with the highest number of airports.
	 *
	 * @param model the model to populate the
	 * @return String
	 */
	@RequestMapping("/airportDetails")
	public String report(Model model) {
		log.info("airportDetails");
		List<Country> countriesHavingMaxAirports = countryService.tenCountriesWithHighestNumberOfAirports();
		log.info("countriesHavingMaxAirports => {}", countriesHavingMaxAirports);
		Map<String, List<CountryRunway>> results = new HashMap<>();
		results.put("Top 10 Countries with highest number of Airports", getStatsFast(countriesHavingMaxAirports));
		model.addAttribute("results", results);
		log.info("Returning {}", results);
		return "airportDetails";
	}

	public List<CountryRunway> getStats(List<Country> countries) {
		fatenCountries(countries);
		return countries.stream().map(c -> {
			CountryRunway stats = new CountryRunway(c);
			// Runway Surfaces
			List<Runway> countryRunways = c.getAirports().stream().flatMap(a -> a.getRunways().stream()).collect(Collectors.toList());
			stats.runways = runwaySurfaces(countryRunways);
			stats.runwayDetails = topTenRunwayDetails(countryRunways);
			return stats;
		}).collect(Collectors.toList());
	}

	public void fatenCountries(List<Country> countries) {
		Map<Long, Country> countryMap = new HashMap<>();
		Map<Long, Airport> airportMap = new HashMap<>();
		countries.forEach(c -> {
			c.setAirports(new ArrayList<>());
			countryMap.put(c.getId(), c);
		});
		List<Airport> airports = airportService.fetchAirports(countries);
		airports.forEach(a -> {
			countryMap.get(a.getCountryId()).getAirports().add(a);
			a.setRunways(new ArrayList<>());
			airportMap.put(a.getId(), a);

		});
		List<Runway> runways = runwayService.getRunwaysForAirports(airports);
		runways.forEach(r -> {
			airportMap.get(r.getAirportId()).getRunways().add(r);
		});
	}

	public Map<Long, List<Runway>> runwaysForCountries(List<Country> countries) {
		Map<Long, List<Runway>> results = new HashMap<>();
		countries.forEach(c -> results.put(c.getId(), new ArrayList<>()));

		List<Runway> runways = runwayService.getRunwaysForCountries(countries);
		runways.forEach(r -> results.get(r.getCountryId()).add(r));

		return results;
	}

	public List<CountryRunway> getStatsFast(List<Country> countries) {
		Map<Long, List<Runway>> runways = runwaysForCountries(countries);
		return countries.stream().map(c -> {
			CountryRunway stats = new CountryRunway(c);
			List<Runway> countryRunways = runways.get(c.getId());
			stats.runways = runwaySurfaces(countryRunways);
			stats.runwayDetails = topTenRunwayDetails(countryRunways);
			return stats;
		}).collect(Collectors.toList());
	}

	private Set<String> runwaySurfaces(List<Runway> runways) {
		return runways.stream().map(r -> r.getSurface()).filter(r -> !r.isEmpty()).collect(Collectors.toSet());
	}


	private Set<String> topTenRunwayDetails(List<Runway> runways) {
		// Top 10 Runway Details
		Map<String, Long> runwayCounts = runways.stream().map(r -> r.getLe_ident()).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
		List<Map.Entry<String, Long>> ic = new ArrayList<>(runwayCounts.entrySet());
		ic.sort((x, y) -> (int) (x.getValue() - y.getValue()));
		return ic.stream().limit(10).map(Map.Entry::getKey).collect(Collectors.toSet());
	}


}
