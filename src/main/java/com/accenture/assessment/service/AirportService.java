package com.accenture.assessment.service;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accenture.assessment.model.Airport;
import com.accenture.assessment.model.Country;
import com.accenture.assessment.repository.AirportRepository;

import lombok.NonNull;

@Service
public class AirportService {

    @Autowired
    private AirportRepository airportRepository;

    /**
     * fetchAirport method to fetch the Airport detail for the country being searched.
     *
     * @param country the country whose Airport details must be fetched
     * @return List of Airport details for the specific country
     */
    public List<Airport> fetchAirport(@NonNull Country country) {
        return airportRepository.findByCountryId(country.getId());
    }

    /**
     * fetchAirports Airports for the List of countries provided
     *
     * @param countries the List of Countries
     * @return List of Airport
     */
    public List<Airport> fetchAirports(@NonNull List<Country> countries) {
        List<Long> countryIds = countries.stream().map(c -> c.getId()).collect(Collectors.toList());
        return airportRepository.findByCountryIdIn(countryIds);
    }

}
