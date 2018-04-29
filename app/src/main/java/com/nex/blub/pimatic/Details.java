package com.nex.blub.pimatic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.nex.blub.pimatic.devices.Temperatur;

public class Details extends Activity {

    Temperatur device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent in = getIntent();
        String title = (String) getIntent().getExtras().get("Device_Name");

        getActionBar().setTitle(title);

        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
