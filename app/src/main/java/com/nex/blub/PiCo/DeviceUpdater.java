package com.nex.blub.PiCo;

import android.os.CountDownTimer;
import android.util.Log;

import com.nex.blub.PiCo.interfaces.Notifiable;


public class DeviceUpdater {

    // Log-Tag für diese Klasse
    private static final String TAG = "DeviceUpdater";

    private final Notifiable receiver;


    public DeviceUpdater(Notifiable receiver, int updateInterval) {
        this.receiver = receiver;

        // Timer zum Aktualisieren der Geräte
        this.createTimer(updateInterval);
    }


    public void update() {
        new API(this).execute("values");
    }


    public void receiveResults(String result) {
        if (this.receiver == null) {
            return;
        }

        this.receiver.getNotification(result);
    }


    /**
     * Initialisiert einen Timer, um die Werte der Geräte zu aktualisieren.
     *
     * @param interval Intervall in Millisekunden bis der Timer abläuft
     */
    private void createTimer(int interval) {
        new CountDownTimer(interval, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                Log.d(TAG, "Timer abgelaufen");

                update();
                this.start();
            }
        }.start();
    }
}
