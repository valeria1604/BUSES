/**
 * Nazwa: Jazda autobusow
 * Autor: Valeriia Tykhoniuk (266319)
 * Data utworzenia: 10.01.2023
 */
package com.company;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class NarrowBridgeAnimation extends JFrame implements ItemListener {
    public static final int MAX_CARS_ON_THE_BRIDGE = 3;
    private static int TRAFFIC = 1000;
    private final Font font = new Font("MonoSpaced", Font.BOLD | Font.ITALIC, 16);

    private List<Bus> busesWaiting = new LinkedList();
    private List<Bus> busesWaitingEast = new LinkedList();
    private List<Bus> busesWaitingWest = new LinkedList();
    private int counterBuses;
    private List<Bus> busesOnTheBridge = new LinkedList();

    private JTextField bridgeField = new JTextField(30);
    private JTextField queueField = new JTextField(30);
    JTextArea textArea = new JTextArea(25, 50);
    public JComboBox<RestrictionType> limitComboBox = new JComboBox<RestrictionType>(RestrictionType.values());

    private RestrictionType chosenLimitType = RestrictionType.ONE_SIDE;
    public BusDirection chosenBusDirection = BusDirection.EAST;

    public static void main(String[] args) {
        NarrowBridgeAnimation bridge = new NarrowBridgeAnimation();
        while (true) {
            Bus bus = new Bus(bridge);
            (new Thread(bus)).start();
            try {
                Thread.sleep((long) (5500 - TRAFFIC));
            } catch (InterruptedException interruptedException) {
            }
        }
    }


    private void printBridgeInfo(Bus bus, String message) {
        StringBuilder sb = new StringBuilder();
        sb.append("Bus[" + bus.getId() + "->" + bus.getDir() + "]  ");
        sb.append(message + "\n");
        textArea.insert(sb.toString(), 0);
        sb = new StringBuilder();
        Iterator iterator = this.busesWaiting.iterator();

        Bus b;
        while (iterator.hasNext()) {
            b = (Bus) iterator.next();
            sb.append(b.getId());
            sb.append(" ");
        }

        queueField.setText(sb.toString());
        sb = new StringBuilder();
        iterator = busesOnTheBridge.iterator();

        while (iterator.hasNext()) {
            b = (Bus) iterator.next();
            sb.append(b.getId());
            sb.append(" ");
        }
        bridgeField.setText(sb.toString());
    }

    private void printInfo(String message) {
        this.textArea.insert(message + "\n", 0);
    }

    public synchronized void getOnTheBridge(Bus bus) {
        if (chosenLimitType.equals(RestrictionType.UNLIMITED)) {
            unlimitedCarsMoving(bus);
        } else if (chosenLimitType.equals(RestrictionType.THREE_CARS_ON_BRIDGE)) {
            threeCarsMoving(bus);
        } else if (chosenLimitType.equals(RestrictionType.ONE_SIDE)) {
            oneSideRoadMoving(bus);
        } else if (chosenLimitType.equals(RestrictionType.TWO_SIDE)) {
            twoSideRoadMoving(bus);
        }
    }

    private void unlimitedCarsMoving(Bus bus) {
        busesOnTheBridge.add(bus);
        printBridgeInfo(bus, "ON THE BRIDGE");
    }

    private void twoSideRoadMoving(Bus bus) {
        while (!busesOnTheBridge.isEmpty()) {
            addWaitBus(bus, busesWaiting);
        }
        busesOnTheBridge.add(bus);
        printBridgeInfo(bus, "ON THE BRIDGE");
    }

    private void threeCarsMoving(Bus bus) {
        if (busesOnTheBridge.size() >= MAX_CARS_ON_THE_BRIDGE) {
            addWaitBus(bus, busesWaiting);
        }
        busesOnTheBridge.add(bus);
        printBridgeInfo(bus, "ON THE BRIDGE");
    }

    private void oneSideRoadMoving(Bus bus) {
        if (chosenBusDirection.equals(BusDirection.WEST)) {

            if (bus.getDir().equals(BusDirection.EAST)) {
                addWaitBus(bus, busesWaitingEast);
            } else if (bus.getDir().equals(BusDirection.WEST)) {
                busesOnTheBridge.add(bus);
                counterBuses++;
                printBridgeInfo(bus, "ON THE BRIDGE");
            }
            if (counterBuses > 5) {
                chosenBusDirection = BusDirection.EAST;
                printInfo("CURRENT DIRECTION FOR NEW CREATED BUSES WILL BE-> EAST");
                counterBuses = 0;
            }
        }

        if (chosenBusDirection.equals(BusDirection.EAST)) {

            if (bus.getDir().equals(BusDirection.WEST)) {
                addWaitBus(bus, busesWaitingWest);
            } else if (bus.getDir().equals(BusDirection.EAST)) {
                busesOnTheBridge.add(bus);
                counterBuses++;
                printBridgeInfo(bus, "ON THE BRIDGE");
            }
            if (counterBuses > 5) {
                chosenBusDirection = BusDirection.WEST;
                printInfo("CURRENT DIRECTION FOR NEW CREATED BUSES WILL BE -> WEST");
                counterBuses = 0;
            }
        }
    }


    private void addWaitBus(Bus bus, List busesWaiting) {
        busesWaiting.add(bus);
        printBridgeInfo(bus, "IS WAITING");
        try {
            wait();
        } catch (InterruptedException var3) {
        }
        busesWaiting.remove(bus);
    }


    public synchronized void getOffTheBridge(Bus bus) {
        busesOnTheBridge.remove(bus);
        printBridgeInfo(bus, "IS GETTING OFF THE BRIDGE");
        notify();
    }

    private NarrowBridgeAnimation() {
        setTitle("Simulation of the road through a bridge");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(550, 710);
        setResizable(false);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        createPanel(panel);
        panel.setFont(font);
        panel.setBackground(new java.awt.Color(158, 35, 26));
        setContentPane(panel);
        setVisible(true);
    }



    private void createPanel(JPanel panel) {
        final JSlider slider = new JSlider(0, 500, 5000, 2000);
        slider.setSize(300, 20);
        slider.setFont(font);
        slider.setMajorTickSpacing(1000);
        slider.setMinorTickSpacing(500);
        slider.setPaintLabels(true);

        Hashtable sliderLables = new Hashtable();
        sliderLables.put(0, new JLabel("Low"));
        sliderLables.put(5000, new JLabel("High"));
        slider.setLabelTable(sliderLables);
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                NarrowBridgeAnimation.TRAFFIC = slider.getValue();
            }
        });

        JLabel comboBoxLabel = new JLabel("       Limit type:");
        JLabel sliderLabel = new JLabel("Traffic intensity:");
        JLabel bridgeLabel = new JLabel("       On the bridge:");
        JLabel queueLabel = new JLabel("         Queue:");
        comboBoxLabel.setFont(font);
        comboBoxLabel.setForeground(Color.white);
        sliderLabel.setFont(font);
        sliderLabel.setForeground(Color.white);
        bridgeLabel.setFont(font);
        bridgeLabel.setForeground(Color.white);
        queueLabel.setFont(font);
        queueLabel.setForeground(Color.white);
        textArea.setFont(font);
        bridgeField.setFont(font);
        queueField.setFont(font);
        bridgeField.setEditable(false);
        queueField.setEditable(false);
        textArea.setEditable(false);

        limitComboBox.addItemListener(this);

        panel.add(comboBoxLabel);
        panel.add(limitComboBox);
        panel.add(sliderLabel);
        panel.add(slider);
        panel.add(bridgeLabel);
        panel.add(bridgeField);
        panel.add(queueLabel);
        panel.add(queueField);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane scroll_bars = new JScrollPane(textArea, 22, 30);
        panel.add(scroll_bars);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        chosenLimitType = (RestrictionType) limitComboBox.getSelectedItem();
    }

}



