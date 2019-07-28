package com.nex.blub.PiCo.tileServices;

import android.graphics.drawable.Icon;
import android.os.Build;
import android.service.quicksettings.TileService;
import androidx.annotation.RequiresApi;
import android.util.Log;

import com.nex.blub.PiCo.DeviceUpdater;
import com.nex.blub.PiCo.R;
import com.nex.blub.PiCo.devices.Light;
import com.nex.blub.PiCo.interfaces.Notifiable;


@RequiresApi(api = Build.VERSION_CODES.N)
public class WohnzimmerLampe extends TileService implements Notifiable {

    // Log-Tag für diese Klasse
    private static final String TAG = "WohnzimmerLampe";

    // Timer-Intervall in Millisekunden, in der der Status des Licht aktualisiert wird
    private static final int TIMER_INTERVAL = 30000;

    // aktueller Status der Lampe
    private static boolean toggleState = true;

    // Lampen-Instanz erstellen
    private static Light light = null;


    private static DeviceUpdater deviceUpdater = null;


    /**
     * Start des Services
     * Wird jedes mal aufgerufen, wenn Quicksettings für die Lampe angezeigt wird
     */
    @Override
    public void onStartListening() {
        Log.d(TAG, "onStartListening");

        if (light == null) {
            light = new Light("WohnzimmerLicht");
            Log.d(TAG, "Registriere neuen DeviceUpdater");
            deviceUpdater = new DeviceUpdater(this, TIMER_INTERVAL);
            deviceUpdater.update();
        } else {
            deviceUpdater.update();
        }

    }


    /**
     * Wird als Callback-Methode aufgerufen, wenn der Status des Lichts aktualisert wurde
     *
     * @param notification Payload, wenn das Gerät geupdatet wurde.
     */
    public void getNotification(String notification) {
        light.updateValues(notification);
        toggleState = light.getStatus();

        Log.d(TAG, "STATUS DES LICHTS: " + toggleState);
        this.updateTileIcon();
    }


    /**
     * Tile wird angeklickt
     */
    @Override
    public void onClick() {
        // Licht togglen
        light.toggle();

        // TileIcon updaten
        this.updateTileIcon();
    }


    /**
     * Icon des Tiles updaten, wenn sich Status des Licts geändert hat
     */
    private void updateTileIcon() {
        int iconId = (toggleState) ? R.drawable.light_on : R.drawable.light_off;
        Icon icon = Icon.createWithResource(getApplicationContext(), iconId);

        toggleState = !toggleState;

        getQsTile().setIcon(icon);
        getQsTile().updateTile();
    }
}