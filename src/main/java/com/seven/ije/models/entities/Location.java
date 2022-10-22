package com.seven.ije.models.entities;

import com.seven.ije.models.enums.LocationStatus;
import com.seven.ije.models.enums.StateName;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Entity(name="location")
@Table(name="location")
@Data
@ToString
public class Location {
    @EmbeddedId
    private LocationId locationId;

    @Column(nullable = false)
    private String stationName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StateName stateName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LocationStatus status;
}
