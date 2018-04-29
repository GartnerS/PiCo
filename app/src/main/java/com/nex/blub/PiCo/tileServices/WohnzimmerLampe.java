package com.nex.blub.PiCo.tileServices;

import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.CountDownTimer;
import android.service.quicksettings.TileService;
import android.support.annotation.RequiresApi;
import android.util.Log;
import com.nex.blub.PiCo.R;
import com.nex.blub.PiCo.devices.Light;


@RequiresApi(api = Build.VERSION_CODES.N)
public class WohnzimmerLampe extends TileService {

    private static final String TAG = "TileService_Wohnzimmer";

    private static final int timerInterval = 30000;

    // aktueller Status der Lampe
    private boolean toggleState = true;

    // Lampen-Instanz erstellen
    private Light light = null;


    /**
     * Start des Services
     */
    @Override
    public void onStartListening() {
        this.light = new Light("WohnzimmerLicht");

        new CountDownTimer(timerInterval, 1000) {
            public void onTick(long millisUntilFinished) {}

            public void onFinish() {
                Log.d(TAG, "Timer abgelaufen");
                update();
                this.start();
            }
        }.start();

        this.update();
    }


    private void update() {
        // Aktuellen Status des Lichts abfragen
        this.light.update();
        this.toggleState = this.light.getStatus();

        // TileIcon updaten
        this.updateTileIcon();
    }


    /**
     * Tile wird angeklickt
     */
    @Override
    public void onClick() {
        // Licht togglen
        this.light.toggle();

        // TileIcon updaten
        this.updateTileIcon();
    }


    /**
     * Icon des Tiles updaten, wenn sich Status des Licts geändert hat
     */
    private  void updateTileIcon() {
        int iconId = (toggleState) ? R.drawable.light_off : R.drawable.light_on;
        Icon icon = Icon.createWithResource(getApplicationContext(), iconId);

        toggleState = !toggleState;

        getQsTile().setIcon(icon);
        getQsTile().updateTile();
    }
}