package com.accenture.assessment;


import java.io.IOException;

import com.accenture.assessment.model.Runway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;

import com.accenture.assessment.model.Airport;
import com.accenture.assessment.model.Country;
import com.accenture.assessment.repository.AirportRepository;
import com.accenture.assessment.repository.CountryRepository;
import com.accenture.assessment.repository.RunwayRepository;
import com.accenture.assessment.utils.Utils;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@ComponentScan("com.accenture.assessment")
@Slf4j
public class AirportAssessmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(AirportAssessmentApplication.class, args);
    }

    @Autowired
    CountryRepository countryRepository;

    @Autowired
    AirportRepository airportRepository;

    @Autowired
    RunwayRepository runwayRepository;

    @Bean
    @Profile("!test")
    public CommandLineRunner bootstrap(ApplicationContext ctx) {
        return args -> {
            if (!countryRepository.findAll().iterator().hasNext()) {
                saveCountry();
                saveAirport();
                saveRunway();
            }
        };
    }

    /**
     * Saving the Country data  from csv to the database
     *
     * @throws IOException
     */
    void saveCountry() throws IOException {
        Utils.readCsv("countries.csv").parallelStream().forEach(row -> {
            Country country = Country.from(row);
            log.debug("Saving {}", country);
            countryRepository.save(country);
        });
    }

    /**
     * Saving the Airport details from csv to the database
     *
     * @throws IOException
     */
    void saveAirport() throws IOException {
        Utils.readCsv("airports.csv").parallelStream().forEach(row -> {
            Airport airport = Airport.from(row, (country) -> countryRepository.findByLowerCaseCode(country.toLowerCase()).getId());
            log.debug("Saving {}", airport);
            airportRepository.save(airport);
        });
    }

    /**
     *
     * Saving the Runway Details from csv to  the database
     *
     * @throws IOException
     */
    void saveRunway() throws IOException {
        Utils.readCsv("runways.csv").parallelStream().forEach(row -> {
            Runway runway = Runway.from(row, (airportId) -> airportRepository.findById(airportId).get().getCountryId());
            log.debug("Saving {}", runway);
            runwayRepository.save(runway);
        });
    }


}
