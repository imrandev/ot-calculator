package com.apprunner.otcal.utils;

import android.util.Log;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class OverTimeUtils {

    private static OverTimeUtils instance;
    private long secondsInMilli = 1000;
    private long minutesInMilli = secondsInMilli * 60;
    private long hoursInMilli = minutesInMilli * 60;
    private long daysInMilli = hoursInMilli * 24;

    private Date tS, tE;
    private Date oS, oE;
    private Calendar temp;
    private long oV, dT, tT;

    private OvertimeExecution overtimeExecution;

    public OverTimeUtils(String start, String end) {
        try {
            this.tS = DateFormatUtils.getInstance().dateTimeFormat.parse(start);
            this.tE = DateFormatUtils.getInstance().dateTimeFormat.parse(end);

            temp = Calendar.getInstance();
            temp.setTime(DateFormatUtils.getInstance().dateFormat.parse(start));

            oS = DateFormatUtils.getInstance().getDutyStartTime(temp.getTime());
            oE = DateFormatUtils.getInstance().getDutyEndTime(temp.getTime());
            dT = DateFormatUtils.getInstance().getDutyTime();
            tT = 24 * 60 * 60 * 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setOvertimeExecution(OvertimeExecution overtimeExecution) {
        this.overtimeExecution = overtimeExecution;
    }

    public void onExecute(){
        if (temp == null) return;
        doExecution(getCount());
    }

    private void doExecution(int count){
        if (count > 1){
            onMultiDays(count);
        } else {
            onSingleDay();
        }
    }

    private void onSingleDay() {
        if (DateFormatUtils.getInstance().isWeekend(tE) || DateFormatUtils.getInstance().isHoliday(tE)){
            oV = tE.getTime() - tS.getTime();
        } else {
            if (tS.before(oS) && tE.before(oE)){
                if (tE.after(oS)) tE = oS;
                oV = tE.getTime() - tS.getTime();
            } else if (tS.after(oS) && tE.after(oE)){
                if (tS.before(oE)) tS = oE;
                oV = tE.getTime() - tS.getTime();
            } else if (tS.before(oS) && tE.after(oE)){
                oV = (tE.getTime() - tS.getTime()) - dT;
            }
        }
        overtimeExecution.onResult(oV);
    }

    private void onMultiDays(int daysCount) {
        for (int i=0; i<daysCount; i++){
            if (i == 0){
                temp.add(Calendar.DATE, 1);
                long tempEndPoint = getTempDatePoint(temp.getTime()).getTime();
                if (DateFormatUtils.getInstance().isHoliday(tS) || DateFormatUtils.getInstance().isWeekend(tS)){
                    oV = tempEndPoint - tS.getTime();
                } else {
                    if (tS.before(oS)){
                        oV = ((tempEndPoint - tS.getTime()) - dT);
                    } else {
                        if (tS.after(oS) && tS.before(oE)) tS = oE;
                        oV = tempEndPoint - tS.getTime();
                    }
                }
            } else if (i == daysCount -1){
                long tempStartPoint = getTempDatePoint(temp.getTime()).getTime();
                oS = DateFormatUtils.getInstance().getDutyStartTime(temp.getTime());
                oE = DateFormatUtils.getInstance().getDutyEndTime(temp.getTime());

                if (DateFormatUtils.getInstance().isWeekend(tE) || DateFormatUtils.getInstance().isHoliday(tS)){
                    oV += tE.getTime() - tempStartPoint;
                } else {
                    if (tE.before(oS)){
                        oV += (tE.getTime() - tempStartPoint);
                    } else if (tE.after(oE)){
                        oV += ((tE.getTime() - tempStartPoint) - dT);
                    } else {
                        tE = oS;
                        oV += (tE.getTime() - tempStartPoint);
                    }
                }
                overtimeExecution.onResult(oV);
            } else {
                if (DateFormatUtils.getInstance().isHoliday(temp.getTime()) || DateFormatUtils.getInstance().isWeekend(temp.getTime())){
                    oV += tT;
                } else {
                    oV += (tT - dT);
                }
                temp.add(Calendar.DATE, 1);
            }
        }
    }

    private int getCount(){
        if (tS != null && tE != null){
            long diff = tE.getTime() - tS.getTime();
            long elapsedDays = diff / daysInMilli;
            diff = diff % daysInMilli;
            long elapsedHours = diff / hoursInMilli;
            diff = diff % hoursInMilli;
            long elapsedMinutes = diff / minutesInMilli;
            diff = diff % minutesInMilli;
            long elapsedSeconds = diff / secondsInMilli;
            if (elapsedHours > 0 || elapsedMinutes > 0 || elapsedSeconds > 0) elapsedDays++;
            Log.e("elapsedDays", String.format("%s", elapsedDays));
            return (int) elapsedDays;
        }
        return -1;
    }

    private Date getTempDatePoint(Date date){
        try {
            return DateFormatUtils.getInstance().dateTimeFormat.parse(String.format("%s %s",
                    DateFormatUtils.getInstance().dateFormat.format(date), "00:00:00"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public interface OvertimeExecution {
        void onResult(long ov);
    }
}
