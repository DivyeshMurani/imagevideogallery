package org.div.vram.imagegallery.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtil {

    public static String time(long milliSeconds) {
        String val = String.valueOf(milliSeconds);
//        String time = val+"000";
        String time = val;

        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//        System.out.println(milliSeconds);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(time));
        return formatter.format(calendar.getTime());

    }

    public static String getTime(long milliSeconds) {
        String val = String.valueOf(milliSeconds);
        String time = val+"000";
//        String time = val;

        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//        System.out.println(milliSeconds);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(time));
        return formatter.format(calendar.getTime());

    }

    public static long timeAdd(long milliSeconds) {
        String val = String.valueOf(milliSeconds);
        String time = val+"000";
        return Long.parseLong(time);

    }



}
