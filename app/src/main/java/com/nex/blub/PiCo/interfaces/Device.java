package com.nex.blub.PiCo.interfaces;

/**
 * Interface, dass alle in Pimatic eingerichteten Geräte implementieren sollten
 */
public interface Device {

    // Abfragen des Namens des Geräts
    String getName();

    // Aktualisiert die View, die mit dem Gerät asoziiert ist
    void update();

    // Diese Methode wird nach einem API-Request aufgerufen.
    void receiveResult(String result);

}
