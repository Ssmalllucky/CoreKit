package com.ssmalllucky.android.corekit;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ssmalllucky.android.core.utils.StorageUtils;
import com.ssmalllucky.android.core.utils.DateAndTimeUtils;

import java.io.File;

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

        StorageUtils.getAppPackageSize(this);
        StorageUtils.getAppUsedInternalStorageSize(this);
        StorageUtils.getPhoneUsedSize(null);
        StorageUtils.getInternalStorageTotalSize(this,null);

//        for (int i = 0; i < getSpaceTotal().length; i++) {
//            Log.d(TAG, "onCreate: " + i + ": " + getSpaceTotal()[i]);
//        }
//
//        double progressF = ((double)getSpaceTotal()[0]) / getSpaceTotal()[2];
//        Log.d(TAG, "onCreate111: " + progressF);
//        int progress = (int)((float)getSpaceTotal()[0] / (float)getSpaceTotal()[2]);
//        int secondaryProgress = (int)((double)getSpaceTotal()[1] / (double)getSpaceTotal()[2]);
//        double progressS = ((double)getSpaceTotal()[1]) / getSpaceTotal()[2];
//        DecimalFormat df = new DecimalFormat("#.##");
//        try {
//            Number number = df.parse(df.format(progressF));
//            Number number1 = df.parse(df.format(progressS));
//            int s = (int) (number.doubleValue() * 100);
//            int s1 = (int) (number1.doubleValue() * 100);
//            Log.d(TAG, "onCreate:444 " + s);
//            Log.d(TAG, "onCreate:555 " + s1);
//        } catch (ParseException e) {
//            throw new RuntimeException(e);
//        }
//        Log.d(TAG, "onCreate333: " + df.format(progressF));
//        Log.d(TAG, "onCreate333: " + df.format(progressS));

        Log.d(TAG, "formatStorageSize: " + StorageUtils.formatStorageSize(100, StorageUtils.FORMAT_KB));
    }

//    public long[] getSpaceTotal() {
//        long appPackageSpace = StorageUtils.getAppPackageSpace(this);
//        long appStorageSpace = StorageUtils.getAppStorageSpace(this);
//        long phoneUsedSpace = StorageUtils.getPhoneUsedSpace();
//        long totalStorageSpace = StorageUtils.getTotalStorageSpace();
//        return new long[]{appPackageSpace + appStorageSpace, phoneUsedSpace, totalStorageSpace};
//    }

}
