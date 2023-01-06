package com.seven.ije.models.entities;

import com.seven.ije.models.enums.VoyageStatus;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name="voyage")
@Table(name="voyage")
@Data
@ToString
public class Voyage {
    @Id
    @GenericGenerator(name = "voyage_uuid_gen", strategy = "org.hibernate.id.UUIDGenerator")
    @GeneratedValue(generator = "voyage_uuid_gen")
    private UUID voyageNo;

    //Join columns on composite primary key
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name="departureStateCode",referencedColumnName="stateCode"),
            @JoinColumn(name="departureStationNo",referencedColumnName="stationNo")
    })
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Location departureLocation;

    @ManyToOne
    @JoinColumns({
               @JoinColumn(name="arrivalStateCode",referencedColumnName="stateCode"),
               @JoinColumn(name="arrivalStationNo",referencedColumnName="stationNo")
    })
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Location arrivalLocation;

    @Column(nullable = false)
    private LocalDateTime travelDateTime;

    @Column(nullable = true)
    private LocalDateTime arrivalDateTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private VoyageStatus status;
    }