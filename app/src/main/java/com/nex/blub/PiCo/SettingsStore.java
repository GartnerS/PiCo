package com.nex.blub.PiCo;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.animation.AccelerateInterpolator;

public class SettingsStore {

    private SharedPreferences sharedPref;

    private SharedPreferences.Editor editor;

    public SettingsStore(Activity activity) {
        this.sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        this.editor = this.sharedPref.edit();
    }

    public void write(String key, boolean value) {
        this.editor.putBoolean(key, value);
        this.editor.commit();
    }

    public void write(String key, String value) {
        this.editor.putString(key, value);
        this.editor.commit();
    }

    public void write(String key, int value) {
        this.editor.putInt(key, value);
        this.editor.commit();
    }

    public int readInt(String key) {
        return this.sharedPref.getInt(key, 0);
    }

    public String readString(String key) {
        return this.sharedPref.getString(key, "");
    }

    public boolean readBool(String key) {
        return this.sharedPref.getBoolean(key, false);
    }
}
