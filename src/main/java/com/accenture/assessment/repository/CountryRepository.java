package com.accenture.assessment.repository;


import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.accenture.assessment.model.Country;


public interface CountryRepository extends CrudRepository<Country, Long> {

    List<Country> findByLowerCaseNameStartingWith(String name);

    Country findByLowerCaseName(String name);

    Country findByLowerCaseCode(String code);

    List<Country> findByIdIn(List<Long> countryIds);
}
