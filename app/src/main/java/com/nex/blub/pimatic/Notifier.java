package com.nex.blub.pimatic;

import android.widget.Toast;
import com.nex.blub.pimatic.interfaces.PimaticActivity;


class Notifier {

    /**
     * Zeigt eine Toast-Nachricht an, sofern sich die Activity gerade im Vordergrund befindet.
     *
     * @param activity View auf dem der Toast angezeigt werden soll
     * @param resourceId Id des Textes der angezeigt werden soll
     */
     static void showToast(PimaticActivity activity, int resourceId) {
        if (activity.isInForeground()) {
            Toast.makeText(activity.getContext(), resourceId, Toast.LENGTH_SHORT).show();
        }
    }
}
