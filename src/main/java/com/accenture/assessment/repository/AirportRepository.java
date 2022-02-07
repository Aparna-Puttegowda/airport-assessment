package com.accenture.assessment.repository;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.accenture.assessment.model.Airport;

public interface AirportRepository extends CrudRepository<Airport, Long> {

    List<Airport> findByCountryId(long countryId);

    List<Airport> findByCountryIdIn(List<Long> countryId);

    @Query("SELECT a.countryId FROM Airport a GROUP BY a.countryId ORDER BY COUNT(a) DESC")
    List<Long> countriesWithHighNumberOfAirports();
}
