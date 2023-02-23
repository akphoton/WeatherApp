package com.example.weatherapp.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class CommonUtils {

    public static String getDate(String date, int timezone) {
        String format = "dd MMM, yyyy h:mm a";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());

        int hours = timezone / 3600;
        int minutes = (timezone % 3600) / 60;

        String timeString = String.format("%02d:%02d", hours, minutes);
        String formatTimeZone = !timeString.startsWith("-") ? timeString = "+" + timeString : timeString;
        sdf.setTimeZone(TimeZone.getTimeZone("GMT" + formatTimeZone));
        return sdf.format(new Date(Long.parseLong(date) * 1000));

    }
}
