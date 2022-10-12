package com.seven.railroadapp.models.entities;

import com.seven.railroadapp.models.enums.LocationStatus;
import com.seven.railroadapp.models.enums.StateName;
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
