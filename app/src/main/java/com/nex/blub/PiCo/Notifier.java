package com.nex.blub.PiCo;

import android.widget.Toast;

import com.nex.blub.PiCo.interfaces.PiCoActivity;


class Notifier {

    /**
     * Zeigt eine Toast-Nachricht an, sofern sich die Activity gerade im Vordergrund befindet.
     *
     * @param activity View auf dem der Toast angezeigt werden soll
     * @param resourceId Id des Textes der angezeigt werden soll
     */
     static void showToast(PiCoActivity activity, int resourceId) {
        if (activity.isInForeground()) {
            Toast.makeText(activity.getContext(), resourceId, Toast.LENGTH_SHORT).show();
        }
    }
}
