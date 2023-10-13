package com.seven.morningstar.location;

import com.seven.morningstar.enums.LocationStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

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
