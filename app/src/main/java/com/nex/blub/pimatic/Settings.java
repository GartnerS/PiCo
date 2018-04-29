package com.nex.blub.pimatic;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

public class Settings extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
    }

    public void save(View v) {
        SettingsStore store = new SettingsStore(this);

        EditText e = (EditText) findViewById(R.id.input_intervall);
        store.write("intervall", Integer.valueOf(e.getText().toString()));

        Switch s = (Switch) findViewById(R.id.btn_lueften);
        store.write("lueften", s.isChecked()   );
    }
}
