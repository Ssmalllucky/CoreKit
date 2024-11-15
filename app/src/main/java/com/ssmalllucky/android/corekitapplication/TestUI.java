package com.ssmalllucky.android.corekit;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ssmalllucky.android.core.utils.DateAndTimeUtils;

/**
 * @ClassName TestClass
 * @Author ssmalllucky
 * @Date 2024/10/28
 * @Description
 */
public class TestUI extends AppCompatActivity {

    public static final String TAG = "TestUI";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        String compareDate = "2024-10-29";
        Log.d(TAG, "onCreate: 比较日期 " + compareDate + " " + (DateAndTimeUtils.compareDateStrWithToday(compareDate) > 0 ? "小于" : "大于") + "当前日期");
    }
}
