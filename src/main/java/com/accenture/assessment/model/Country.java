package com.accenture.assessment.model;

import static com.accenture.assessment.utils.Utils.l;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Singular;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;


/**
 * Entity class for Country
 *
 */
@Entity
@Table(indexes = {
        @Index(name = "Country_Code", columnList = "code", unique = true),
        @Index(name = "Country_Name", columnList = "name")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "code", "name"})
@ToString(exclude = {"airports", "lowerCaseName", "lowerCaseCode"})
@Slf4j
public class Country {
    @Id
    @Column(name = "countryId")
    @NonNull
    private long id;

    @NonNull
    private String code;

    @NonNull
    private String name;

    private String lowerCaseName;

    private String lowerCaseCode;

    private String continent;

    private String wikipediaLink;

    private String keywords;

    @Singular
    // @OneToMany(cascade = ALL, mappedBy = "country")
    @Transient
    private List<Airport> airports;

    @PrePersist
    @PreUpdate
    private void populateLowerCaseNameAndCode() {
    	lowerCaseName = name == null ? null : name.toLowerCase();
    	lowerCaseCode = code == null ? null : code.toLowerCase();
    }

    public static Country from(Map<String, String> values) {
        return builder()
                .id(l(values, "id"))
                .code(values.get("code"))
                .name(values.get("name"))
                .continent(values.get("continent"))
                .wikipediaLink(values.get("wikipedia_link"))
                .keywords(values.get("keywords"))
                .build();
    }
}
