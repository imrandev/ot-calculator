package com.apprunner.otcal.utils;

import com.apprunner.otcal.constant.Constant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateFormatUtils {
    private static DateFormatUtils instance;

    public static DateFormatUtils getInstance(){
        if (instance == null){
            instance = new DateFormatUtils();
        }
        return instance;
    }

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
    SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    Date getDutyStartTime(Date date){
        try {
            return dateTimeFormat.parse(String.format("%s %s", dateFormat.format(date), Constant.DUTY_START_TIME));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    Date getDutyEndTime(Date date){
        try {
            return dateTimeFormat.parse(String.format("%s %s", dateFormat.format(date), Constant.DUTY_END_TIME));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    boolean isHoliday(Date date){
        return false;
    }

    boolean isWeekend(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY
                || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY;
    }

    long getDutyTime(){
        long dT = 0;
        try {
            Date dS = timeFormat.parse("07:45:00");
            Date dE = timeFormat.parse("17:50:00");
            dT = dE.getTime() - dS.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dT;
    }

    public boolean isDateTime(String datetime){
        try {
            dateTimeFormat.parse(datetime);
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}
