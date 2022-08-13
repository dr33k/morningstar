package com.seven.RailroadApp.models.entities;

import com.seven.RailroadApp.models.enums.StateName;
import lombok.Data;

import javax.persistence.*;

@Entity(name="location")
@Table(name="location")
@Data
@IdClass(LocationId.class)
public class Location {
    @Id
    private String stateCode;

    @Id
    private String stationNo;

    @Column(nullable = false)
    private String stationName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StateName stateName;
}
