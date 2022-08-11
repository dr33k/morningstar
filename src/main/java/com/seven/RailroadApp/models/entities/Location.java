package com.seven.RailroadApp.models.entities;

import com.seven.RailroadApp.models.enums.StateCode;
import lombok.Data;

import javax.persistence.*;

@Entity(name="location")
@Table(name="location")
@Data
@IdClass(LocationId.class)
public class Location {
    @Id
    @Enumerated(EnumType.STRING)
    private StateCode stateCode;

    @Id
    private String stationNo;

    @Column(nullable = false)
    private String stationName;

    @Column(nullable = false)
    private String stationLocation;
}
