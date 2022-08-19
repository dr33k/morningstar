package com.seven.RailroadApp.models.entities;

import com.seven.RailroadApp.models.enums.LocationStatus;
import com.seven.RailroadApp.models.enums.StateName;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity(name="location")
@Table(name="location")
@Data
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
