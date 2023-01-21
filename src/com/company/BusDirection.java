/**
 * Nazwa: Jazda autobusow
 * Autor: Valeriia Tykhoniuk (266319)
 * Data utworzenia: 10.01.2023
 */
package com.company;

public enum BusDirection {
    EAST,
    WEST;

    @Override
    public String toString() {
        switch (this) {
            case EAST:
                return "East";
            case WEST:
                return "West";
        }
        return "";
    }
}
