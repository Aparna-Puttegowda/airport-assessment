package com.accenture.assessment.model;

import static com.accenture.assessment.utils.Utils.l;
import static com.accenture.assessment.utils.Utils.s;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Singular;
import lombok.ToString;

/**
 * Entity class for Airport
 *
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"countryId", "id", "name"})
@ToString(exclude = {"country", "runways"})
public class Airport {

    @Id
    @Column(name = "airportId")
    private long id;

    @NonNull
    private String ident; 

    private String type;
    
    @NonNull
    private String name;
    
    private String latitude_deg;
    private String longitude_deg;
    private String elevation_ft;
    private String continent;
    private String iso_Country;
    private String iso_Region;
    private String municipality;
    private String scheduledService;
    private String gps_Code;
    private String iata_Code;
    private String local_Code;
    private String home_Link;
    private String wikipedia_Link;
    private String keywords;

    @NonNull
    private Long countryId;

    // @NonNull
    // @ManyToOne(optional = false)
    // @JoinColumn(name = "countryId", nullable = false)
    @Transient
    private Country country;

    @Singular
    // @OneToMany(cascade = ALL, mappedBy = "airport")
    @Transient
    private List<Runway> runways;


    public static Airport from(Map<String, String> values, Function<String, Long> countryProvider) {
        return builder()
                .id(l(values, "id"))
                .ident(s(values, "ident"))
                .type(s(values, "type"))
                .name(s(values, "name"))
                .latitude_deg(s(values, "latitude_deg"))
                .longitude_deg(s(values, "longitude_deg"))
                .elevation_ft(s(values, "elevation_ft"))
                .continent(s(values, "continent"))
                .iso_Country(s(values, "iso_country"))
                .countryId(countryProvider.apply(s(values, "iso_country")))
                .iso_Region(s(values, "iso_region"))
                .municipality(s(values, "municipality"))
                .scheduledService(s(values, "scheduled_service"))
                .gps_Code(s(values, "gps_code"))
                .iata_Code(s(values, "iata_code"))
                .local_Code(s(values, "local_code"))
                .home_Link(s(values, "home_link"))
                .wikipedia_Link(s(values, "wikipedia_link"))
                .keywords(s(values, "keywords"))
                .build();
    }

}
