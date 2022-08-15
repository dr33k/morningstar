package com.seven.RailroadApp.models.entities;

import com.seven.RailroadApp.models.enums.StateName;
import lombok.Data;

import javax.persistence.*;

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
}
