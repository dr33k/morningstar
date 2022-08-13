package com.seven.RailroadApp.models.enums;

public enum StateCode {
    ABIA("ABIA"),
    ADMW("ADAMAWA"),
    AKIB("AKWA_IBOM"),
    ANAM("ANAMBRA"),
    BAUC("BAUCHI"),
    BAYL("BAYELSA"),
    BENU("BENUE"),
    BORN("BORNO"),
    CROS("CROSS_RIVER"),
    DELT("DELTA"),
    EDO_("EDO"),
    EKIT("EKITI"),
    ENUG("ENUGU"),
    GOMB("GOMBE"),
    IMO_("IMO"),
    JIGW("JIGAWA"),
    KADU("KADUNA"),
    KANO("KANO"),
    KATS("KATSINA"),
    KEBB("KEBBI"),
    KOGI("KOGI"),
    KWAR("KWARA"),
    LGOS("LAGOS"),
    NSRW("NASARAWA"),
    NGER("NIGER"),
    OGUN("OGUN"),
    ONDO("ONDO"),
    OSUN("OSUN"),
    OYO_("OYO"),
    PLAT("PLATEAU"),
    RVRS("RIVERS"),
    SOKO("SOKOTO"),
    TRBA("TARABA"),
    YOBE("YOBE"),
    ZMFR("ZAMFARA"),
    ABUJ("ABUJA");

    private String stateString;

    public String getStateString() {
        return stateString;
    }

    StateCode(String stateString){this.stateString = stateString;}
}
