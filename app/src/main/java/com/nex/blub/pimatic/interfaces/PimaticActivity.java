package com.nex.blub.pimatic.interfaces;

import android.content.Context;

/**
 * Created by Blub on 19.05.2017.
 */

public interface PimaticActivity {

    public Context getContext();

    public boolean isInForeground();
}
