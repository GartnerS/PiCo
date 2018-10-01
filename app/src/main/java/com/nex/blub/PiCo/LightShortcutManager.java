package com.nex.blub.PiCo;

import android.app.Activity;
import android.util.Log;

import com.nex.blub.PiCo.devices.Light;
import com.nex.blub.PiCo.interfaces.Notifiable;

public class LightShortcutManager extends Activity implements Notifiable {



    public LightShortcutManager() {
        //super.setAction("NEX_LOCATION_SHORTCUT");

    }


    public void test() {

        try {
            Light light = new Light("WohnzimmerLicht");
            light.toggle();
            Log.i("test", "=========> OK");
        } catch (Exception e) {
            Log.e("test", e.getMessage());
        }
    }


    /**
     * Wird als Callback-Methode aufgerufen, wenn der Status des Lichts aktualisert wurde
     *
     * @param notification Payload, wenn das Gerät geupdatet wurde.
     */
    public void getNotification(String notification) {


    }

}
