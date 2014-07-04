package com.jd.living.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.text.TextUtils;

public class StringUtil {

    private static DecimalFormat formatter;
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");



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
            return "0";
        } else {
            return value.replace("kr", "").replaceAll("\\s","");
        }
    }

    public static String getTimeStampAsString(long timestamp) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timestamp);
            return sdf.format(calendar.getTime());
        } catch (Exception e) {

        }
        return "";
    }

    public static String startWithUpperCase(String text) {
        if (text.length() >= 2) {
            return Character.toUpperCase(text.charAt(0)) + text.substring(1);
        } else {
            return text;
        }
    }

    public static String getText(String value, String[] names, String[] types) {
        String text = names[0];
        for (int i = 0; i < types.length ; i++) {
            if (value.equals(types[i])){
                return names[i];
            }
        }
        return "";
    }
}
