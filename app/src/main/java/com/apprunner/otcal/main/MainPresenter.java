package com.apprunner.otcal.main;

import com.apprunner.otcal.utils.DateFormatUtils;
import com.apprunner.otcal.utils.OverTimeUtils;

public class MainPresenter implements MainContract.Presenter {

    private MainContract.View mMainView;
    private long secondsInMilli = 1000;
    private long minutesInMilli = secondsInMilli * 60;
    private long hoursInMilli = minutesInMilli * 60;
    private long daysInMilli = hoursInMilli * 24;

    MainPresenter(MainContract.View mMainView) {
        this.mMainView = mMainView;
    }

    @Override
    public void onProcessOvertime(String start, String end) {
        if (start.isEmpty() || end.isEmpty()) mMainView.onValidation("Please provide both");
        if (!DateFormatUtils.getInstance().isDateTime(start) || !DateFormatUtils.getInstance().isDateTime(end)) mMainView.onValidation("Please provide valid datetime");

        OverTimeUtils overTimeUtils = new OverTimeUtils(start, end);
        overTimeUtils.setOvertimeExecution(overtimeExecution);
        overTimeUtils.onExecute();
    }

    private OverTimeUtils.OvertimeExecution overtimeExecution
            = new OverTimeUtils.OvertimeExecution() {
        @Override
        public void onResult(long ov) {
            long elapsedDays = ov / daysInMilli;
            ov = ov % daysInMilli;
            long elapsedHours = ov / hoursInMilli;
            ov = ov % hoursInMilli;
            long elapsedMinutes = ov / minutesInMilli;
            ov = ov % minutesInMilli;
            long elapsedSeconds = ov / secondsInMilli;
            mMainView.onViewResult(String.format("%sd %sh %sm %ss", elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds));
        }
    };
}
