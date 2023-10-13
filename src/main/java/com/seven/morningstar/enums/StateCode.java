package com.seven.morningstar.enums;

public enum StateCode {
    ABIA("ABIA"),
    ADAM("ADAMAWA"),
    AKIB("AKWA_IBOM"),
    ANAM("ANAMBRA"),
    BAUC("BAUCHI"),
    BAYE("BAYELSA"),
    BENU("BENUE"),
    BORN("BORNO"),
    CROS("CROSS_RIVER"),
    DELT("DELTA"),
    EDO_("EDO"),
    EKIT("EKITI"),
    ENUG("ENUGU"),
    GOMB("GOMBE"),
    IMO_("IMO"),
    JIGA("JIGAWA"),
    KADU("KADUNA"),
    KANO("KANO"),
    KATS("KATSINA"),
    KEBB("KEBBI"),
    KOGI("KOGI"),
    KWAR("KWARA"),
    LAGO("LAGOS"),
    NASA("NASARAWA"),
    NIGE("NIGER"),
    OGUN("OGUN"),
    ONDO("ONDO"),
    OSUN("OSUN"),
    OYO_("OYO"),
    PLAT("PLATEAU"),
    RIVE("RIVERS"),
    SOKO("SOKOTO"),
    TARA("TARABA"),
    YOBE("YOBE"),
    ZAMF("ZAMFARA"),
    ABUJ("ABUJA");

    private String stateName;

    public String getStateName() {
        return this.stateName;
    }

    StateCode(String stateName){this.stateName = stateName;}
}
