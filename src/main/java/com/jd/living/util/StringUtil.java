package com.jd.living.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

import android.text.TextUtils;

public class StringUtil {

    private static DecimalFormat formatter;


    private static DecimalFormat getFormatter () {
        if (formatter == null) {
            formatter = (DecimalFormat) NumberFormat.getCurrencyInstance();
            DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
            symbols.setGroupingSeparator(' ');
            formatter.setMaximumFractionDigits(0);
            formatter.setDecimalFormatSymbols(symbols);
        }

        return formatter;
    }

    public static String getFormattedString(long value) {
        if (value == 0) {
            return "0";
        } else {
            return getFormatter().format(value);
        }
    }

    public static String getFormattedString(double value) {
        if (value == 0) {
            return "0";
        } else {
            return getFormatter().format(value);
        }
    }

    public static String getStringAsNumber(String value) {
        if (TextUtils.isEmpty(value)) {
            return "";
        } else {
            return value.replace("kr", "").replaceAll("\\s","");
        }
    }
}
