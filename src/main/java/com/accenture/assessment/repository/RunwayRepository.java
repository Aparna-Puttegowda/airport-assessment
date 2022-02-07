package com.accenture.assessment.repository;


import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.accenture.assessment.model.Runway;

public interface RunwayRepository extends CrudRepository<Runway, Long> {

    List<Runway> findByAirportId(Long airportId);

    List<Runway> findByAirportIdIn(List<Long> airportIds);

    List<Runway> findByCountryIdIn(List<Long> countryIds);
}
