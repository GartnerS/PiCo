package com.nex.blub.pimatic;

import android.view.View;

import com.nex.blub.pimatic.interfaces.Device;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateManager {

    private static final String TAG = "UpdateManager";

    private Map<Device, List<View>> devices = new HashMap<>();


    public void setDevices(Map<Device, List<View>> devices) {
        this.devices = devices;
    }


}
