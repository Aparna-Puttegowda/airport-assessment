package com.accenture.assessment.service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accenture.assessment.model.Country;
import com.accenture.assessment.repository.AirportRepository;
import com.accenture.assessment.repository.CountryRepository;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CountryService {

    @Autowired
    CountryRepository countryRepository;

    @Autowired
    AirportRepository airportRepository;

    /**
     * To search with prefix names
     *
     * @param prefix the prefix string entered by the user
     * @return List of String
     */
    public List<String> countryNamesWithPrefixes(@NonNull String prefix) {
        return countryRepository.findByLowerCaseNameStartingWith(prefix.toLowerCase()).stream().map(c -> c.getName()).collect(Collectors.toList());
    }

    /**
     * fetch Country Details
     *
     * @param countryParam the Country entered by the user
     * @return the Country details if present
     */
    public Optional<Country> fetchCountry(@NonNull String countryParam) {
        Country country = countryRepository.findByLowerCaseName(countryParam.toLowerCase());
        if (country == null) {
            country = countryRepository.findByLowerCaseCode(countryParam.toLowerCase());
        }
        if (country == null) {
            /*
             Bonus handling
             If prefix matches only one country, return the same
            */
            List<String> names = countryNamesWithPrefixes(countryParam);
            if (names.size() == 1) {
                country = countryRepository.findByLowerCaseName(names.get(0).toLowerCase());
            }
        }
        return Optional.ofNullable(country);
    }

    /**
     * Top Ten Countries with the highest number of Airports
     *
     * @return List of Country
     */
    public List<Country> tenCountriesWithHighestNumberOfAirports() {
        List<Long> countryIds = airportRepository.countriesWithHighNumberOfAirports().stream().limit(10).collect(Collectors.toList());
        log.debug("TenCountriesWithHighestNumberOfAirports => {}", countryIds);
        return countryRepository.findByIdIn(countryIds);
    }
}
