package com.jd.living.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.text.TextUtils;

import com.jd.living.R;

public class StringUtil {

    private static DecimalFormat currencyFormatter;
    private static DecimalFormat numberFormatter;

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    private static DecimalFormat getCurrencyFormatter() {
        if (currencyFormatter == null) {
            currencyFormatter = (DecimalFormat) NumberFormat.getCurrencyInstance();
            DecimalFormatSymbols symbols = currencyFormatter.getDecimalFormatSymbols();
            symbols.setGroupingSeparator(' ');
            currencyFormatter.setMaximumFractionDigits(0);
            currencyFormatter.setDecimalFormatSymbols(symbols);
        }

        return currencyFormatter;
    }

    private static DecimalFormat getNumberFormatter() {
        if (numberFormatter == null) {
            numberFormatter = (DecimalFormat) NumberFormat.getNumberInstance();
            DecimalFormatSymbols symbols = numberFormatter.getDecimalFormatSymbols();
            symbols.setGroupingSeparator(' ');
            currencyFormatter.setDecimalFormatSymbols(symbols);
        }
        return numberFormatter;
    }

    public static String getCurrencyFormattedString(long value) {
        if (value == 0) {
            return "0";
        } else {
            return getCurrencyFormatter().format(value);
        }
    }

    public static String getNumberFormattedString(double value) {
        if (value == 0) {
            return "0";
        } else {
            return getNumberFormatter().format(value);
        }
    }

    public static String getNumberFormattedString(long value) {
        if (value == 0) {
            return "0";
        } else {
            return getNumberFormatter().format(value);
        }
    }

    public static String getCurrencyFormattedString(double value) {
        if (value == 0) {
            return "0";
        } else {
            return getCurrencyFormatter().format(value);
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
        String result = "";
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        long currentTimeStamp = System.currentTimeMillis();

        long diffHours = (currentTimeStamp - timestamp) / (60 * 60 * 1000);


        if (diffHours < 24 && diffHours < calendar.get(Calendar.HOUR_OF_DAY)) {
            result = timeFormat.format(calendar.getTime());
        } else {
            result = dateFormat.format(calendar.getTime());
        }

        return result;
    }

    public static String startWithUpperCase(String text) {
        if (text.length() >= 2) {
            return Character.toUpperCase(text.charAt(0)) + text.substring(1);
        } else {
            return text;
        }
    }

    public static String getText(String value, String[] names, String[] types) {
        for (int i = 0; i < types.length ; i++) {
            if (value.equals(types[i])){
                return names[i];
            }
        }
        return "";
    }

    public static String getDaysSince(Context context, String startDate, String endDate)  {
        long diffDays = 0;

        try {
            Date start = dateFormat.parse(startDate);
            Date end = new Date();
            if (!TextUtils.isEmpty(endDate)) {
                end = dateFormat.parse(endDate);
            }
            diffDays = (end.getTime() - start.getTime()) / (24 * 60 * 60 * 1000);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        String result = "";

        if (!TextUtils.isEmpty(endDate)) {
            result = context.getResources().getQuantityString(R.plurals.daysPublished, (int) diffDays, (int) diffDays);
        } else if (diffDays == 0) {
            result = context.getString(R.string.days_available_zero);
        } else {
            result = context.getResources().getQuantityString(R.plurals.daysAvailable, (int) diffDays, (int) diffDays);
        }

        return result;
    }
}
