package com.nex.blub.PiCo.interfaces;

/**
 * Interface, dass alle in Pimatic eingerichteten Geräte implementieren sollten
 */
public interface Device {

    // Abfragen des Namens des Geräts
    String getName();

    // Aktualisieren der Werte
    void updateValues(String newValues);
}
