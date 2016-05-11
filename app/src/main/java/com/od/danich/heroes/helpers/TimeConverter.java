package com.od.danich.heroes.helpers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class TimeConverter {
    public static String convert(long time){
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+2"));
        String formattedDate = sdf.format(date);
        return formattedDate;
    }
}
