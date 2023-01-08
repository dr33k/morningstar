package com.seven.ije.models.entities;

import com.seven.ije.models.enums.LocationStatus;
import com.seven.ije.models.records.LocationRecord;
import com.seven.ije.models.requests.LocationCreateRequest;
import lombok.Data;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;

@Entity
@Table(name="location")
@Data
@ToString
public class Location {
    @EmbeddedId
    private LocationId locationId;

    @Column(nullable = false)
    private String stationName;

    @Column(nullable = false)
    private String stateName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LocationStatus status;

    public static Location of(LocationRecord record) {
        Location l = new Location();
        BeanUtils.copyProperties(record, l);
        return l;
    }

    public static Location of(LocationCreateRequest request) {
        Location l = new Location();
        BeanUtils.copyProperties(request, l);
        return l;
    }
}
