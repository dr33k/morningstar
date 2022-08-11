package com.seven.RailroadApp.models.enums;

import java.time.LocalTime;

public enum TravelTime {
    NINE_AM("9:00"),
    TWELVE_AM("12:00"),
    THREE_PM("15:00"),
    SIX_PM("18:00");
    String time;
    TravelTime(String time ){
       this.time = time ;
    }
}
