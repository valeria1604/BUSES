/**
 * Nazwa: Jazda autobusow
 * Autor: Valeriia Tykhoniuk (266319)
 * Data utworzenia: 10.01.2023
 */
package com.company;

import java.util.concurrent.ThreadLocalRandom;

public class Bus implements Runnable {

    private NarrowBridgeAnimation bridge;
    private int id;
    private BusDirection dir;

    private static final int MIN_BOARDING_TIME = 1000;
    private static final int MAX_BOARDING_TIME = 10000;
    private static final int GETTING_TO_BRIDGE_TIME = 500;
    private static final int CROSSING_BRIDGE_TIME = 3000;
    private static final int GETTING_TO_FINISH_TIME = 500;
    private static final int FINISH = 500;

    private static int busAmount = 0;

    public BusDirection getDir() {
        return dir;
    }

    public void setDir(BusDirection dir) {
        this.dir = dir;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sleep(int min, int max) {
        sleep(ThreadLocalRandom.current().nextInt(min, max));
    }


    public Bus(NarrowBridgeAnimation bridge) {
        this.bridge = bridge;
        this.id = ++busAmount;
        if (id == 1) {
            this.dir = BusDirection.EAST;
        } else {
            if (ThreadLocalRandom.current().nextInt(0, 2) == 0)
                this.dir = BusDirection.EAST;
            else this.dir = BusDirection.WEST;
        }
    }


    private void printBusInfo(String message) {
        String t = "Bus[" + this.id + "->" + this.dir + "]: " + message + "\n";
        this.bridge.textArea.insert(t, 0);
    }


    private void boarding() {
        printBusInfo("Is waiting for passengers");
        sleep(MIN_BOARDING_TIME, MAX_BOARDING_TIME);
    }

    private void goToTheBridge() {
        printBusInfo("Is riding to the bridge");
        sleep(GETTING_TO_BRIDGE_TIME);
    }


    private void rideTheBridge() {
        printBusInfo("Is riding through the bridge");
        sleep(CROSSING_BRIDGE_TIME);
    }


    private void goToTheFinish() {
        printBusInfo("Is riding to the final");
        sleep(GETTING_TO_FINISH_TIME);
    }


    private void finish() {
        printBusInfo("Is arrived");
        sleep(FINISH);
    }


    public void run() {
        sleep(5000);
        if (bridge.limitComboBox.equals(RestrictionType.ONE_SIDE)) {
            if (bridge.chosenBusDirection.equals(BusDirection.EAST)) {
                if (getDir() == BusDirection.EAST) {
                    boarding();
                    goToTheBridge();
                    bridge.getOnTheBridge(this);
                    rideTheBridge();
                    bridge.getOffTheBridge(this);
                    goToTheFinish();
                    finish();
                }
            }
            if (bridge.chosenBusDirection.equals(BusDirection.WEST)) {
                if (getDir() == BusDirection.WEST) {
                    boarding();
                    goToTheBridge();
                    bridge.getOnTheBridge(this);
                    rideTheBridge();
                    bridge.getOffTheBridge(this);
                    goToTheFinish();
                    finish();
                }
            }
        } else {
            boarding();
            goToTheBridge();
            bridge.getOnTheBridge(this);
            rideTheBridge();
            bridge.getOffTheBridge(this);
            goToTheFinish();
            finish();
        }
    }
}
