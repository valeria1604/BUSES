/**
 * Nazwa: Jazda autobusow
 * Autor: Valeriia Tykhoniuk (266319)
 * Data utworzenia: 10.01.2023
 */
package com.company;

public enum RestrictionType {
    ONE_SIDE("One-side"),
    UNLIMITED("Unlimited"),
    TWO_SIDE("Two-side"),
    THREE_CARS_ON_BRIDGE("Three cars on bridge");

    String value;

    RestrictionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
