package com.seven.ije.models.enums;

public enum StateName {
    ABIA("ABIA"),
    ADAMAWA("ADAM"),
    AKWA_IBOM("AKWA"),
    ANAMBRA("ANAM"),
    BAUCHI("BAUC"),
    BAYELSA("BAYE"),
    BENUE("BENU"),
    BORNO("BORN"),
    CROSS_RIVER("CROS"),
    DELTA("DELT"),
    EDO("EDO_"),
    EKITI("EKIT"),
    ENUGU("ENUG"),
    GOMBE("GOMB"),
    IMO("IMO_"),
    JIGAWA("JIGA"),
    KADUNA("KADU"),
    KANO("KANO"),
    KATSINA("KATS"),
    KEBBI("KEBB"),
    KOGI("KOGI"),
    KWARA("KWAR"),
    LAGOS("LAGO"),
    NASARAWA("NASA"),
    NIGER("NIGE"),
    OGUN("OGUN"),
    ONDO("ONDO"),
    OSUN("OSUN"),
    OYO("OYO_"),
    PLATEAU("PLAT"),
    RIVERS("RIVE"),
    SOKOTO("SOKO"),
    TARABA("TARA"),
    YOBE("YOBE"),
    ZAMFARA("ZAMF"),
    ABUJA("ABUJ");

    private String stateCode;

    public String getStateCode() {
        return stateCode;
    }

    StateName(String stateCode){this.stateCode = stateCode;}
}
