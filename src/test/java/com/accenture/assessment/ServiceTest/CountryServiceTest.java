package com.accenture.assessment.ServiceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.accenture.assessment.model.Country;
import com.accenture.assessment.repository.CountryRepository;
import com.accenture.assessment.service.CountryService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CountryServiceTest {
	
	@Mock
    private CountryRepository countryRepo;

    @InjectMocks
    private CountryService countryService;

    private static  Country aruba() {
        return Country.builder().id(1).code("AW").name("Aruba").build();
    }

    @Test
    public void fetchCountryTest() throws Exception {
        when(countryRepo.findByLowerCaseCode("aw")).thenReturn(aruba());
        when(countryRepo.findByLowerCaseName("aruba")).thenReturn(aruba());
        assertEquals(countryService.fetchCountry("AW"), Optional.of(aruba()) );
        assertEquals(countryService.fetchCountry("Aruba"), Optional.of(aruba()));

        when(countryRepo.findByLowerCaseCode("netherlands")).thenReturn(null);
        when(countryRepo.findByLowerCaseName("nl")).thenReturn(null);
        assertEquals(countryService.fetchCountry("NL"), Optional.ofNullable(null));
        assertEquals(countryService.fetchCountry("Netherlands"), Optional.ofNullable(null));
    }

}
