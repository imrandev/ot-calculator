package com.apprunner.otcal.main;

public class MainContract {

    interface View {
        void onValidation(String message);
        void onViewResult(String overtime);
    }

    interface Presenter {
        void onProcessOvertime(String start, String end);
    }
}
