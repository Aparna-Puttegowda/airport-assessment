package com.accenture.assessment.service;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accenture.assessment.model.Airport;
import com.accenture.assessment.model.Country;
import com.accenture.assessment.model.Runway;
import com.accenture.assessment.repository.RunwayRepository;

import lombok.NonNull;

@Service
public class RunwayService {

    @Autowired
    private RunwayRepository runwayRepository;

    /**
     * Get Runways for each Airport
     *
     * @param airports the airports List to fetch the Runway details based on Airport Ids
     * @return List of Runways
     */
    public List<Runway> getRunwaysForAirports(@NonNull List<Airport> airports) {
        List<Long> airportIds = airports.stream().map(a -> a.getId()).collect(Collectors.toList());
        return runwayRepository.findByAirportIdIn(airportIds);
    }

    /**
     * Get Runways for Countries
     *
     * @param countries the Countries List to fetch the Runway based on Country Ids
     * @return List of Runway
     */
    public List<Runway> getRunwaysForCountries(@NonNull List<Country> countries) {
        List<Long> countryIds = countries.stream().map(a -> a.getId()).collect(Collectors.toList());
        return runwayRepository.findByCountryIdIn(countryIds);
    }
}
