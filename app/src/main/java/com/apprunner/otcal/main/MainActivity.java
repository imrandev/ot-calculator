package com.apprunner.otcal.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.apprunner.otcal.R;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    private EditText mStartEdit, mEndEdit;
    private TextView mResultView;
    private MainContract.Presenter mMainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainPresenter = new MainPresenter(this);

        mStartEdit = findViewById(R.id.t_start_time);
        mEndEdit = findViewById(R.id.t_end_time);
        mResultView = findViewById(R.id.ov_result);
    }

    public void onCalculate(View view) {
        String mStart = mStartEdit.getText().toString();
        String mEnd = mEndEdit.getText().toString();
        mMainPresenter.onProcessOvertime(mStart, mEnd);
    }

    @Override
    public void onValidation(String message) {
        mEndEdit.setError(message);
    }

    @Override
    public void onViewResult(String overtime) {
        mResultView.setText(overtime);
    }
}
