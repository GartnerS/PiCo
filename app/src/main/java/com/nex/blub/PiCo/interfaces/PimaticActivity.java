package com.nex.blub.PiCo.interfaces;

import android.content.Context;


public interface PimaticActivity {

    // Liefert den aktuellen Context der Activity zurück
    Context getContext();

    // Gibt an, ob sich die Activity im Vordergrund befindet
    boolean isInForeground();
}
