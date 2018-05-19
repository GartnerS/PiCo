package com.nex.blub.PiCo.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class StringUtils {

    private static DecimalFormat formatter = (DecimalFormat) NumberFormat.getNumberInstance(Locale.ENGLISH);

    public static String formatDoubleToDecimal(Double value) {
        formatter.applyPattern("##.#");
        return formatter.format(value);
    }
}
