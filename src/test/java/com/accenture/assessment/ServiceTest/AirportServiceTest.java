package com.accenture.assessment.ServiceTest;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.accenture.assessment.model.Airport;
import com.accenture.assessment.model.Country;
import com.accenture.assessment.repository.AirportRepository;
import com.accenture.assessment.service.AirportService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AirportServiceTest {

	@Mock
	private AirportRepository airportRepo;
	
	@InjectMocks
	private AirportService airportService;
	
	private static Country aruba() {
        return Country.builder().id(1).code("AW").name("Aruba").build();
    }
	
	private static List<Airport> arubaAirports(){
		return Collections.singletonList(Airport.builder().id(1).countryId(aruba().getId()).name("Airport").ident("1").build());
	}
	
    private static Country netherlands() {
	        return Country.builder().id(2).code("NL").name("Netherlands").build();
	}
    
    @Test
    public void fetchAirportTest() throws Exception {
        when(airportRepo.findByCountryId(1)).thenReturn(arubaAirports());
        //(airportService.fetchAirport(aruba())).isEqualTo(arubaAirports());
        assertEquals(airportService.fetchAirport(aruba()), arubaAirports());
        
        when(airportRepo.findByCountryId(2)).thenReturn(Collections.emptyList());
        assertEquals(airportService.fetchAirport(netherlands()), Collections.EMPTY_LIST);
       // assertNull(airportService.fetchAirport(netherlands())).isEmpty();
    }

    @Test
    public void testFindAirportsWithList() throws Exception {
        when(airportRepo.findByCountryIdIn(Collections.singletonList(1L))).thenReturn(arubaAirports());
       // assertThat(airportService.fetchAirports(Collections.singletonList(aruba()))).isEqualTo(arubaAirports());
        assertEquals(airportService.fetchAirports(Collections.singletonList(aruba())), arubaAirports());
        
        when(airportRepo.findByCountryIdIn(Collections.singletonList(2L))).thenReturn(Collections.emptyList());
        //assertThat(airportService.fetchAirports(Collections.singletonList(netherlands()))).isEmpty();
        assertEquals(airportService.fetchAirports(Collections.singletonList(netherlands())), Collections.EMPTY_LIST);
    }
}
