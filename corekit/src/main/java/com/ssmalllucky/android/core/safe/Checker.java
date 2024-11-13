package com.ssmalllucky.android.core.safe;

import android.content.Context;

import androidx.annotation.Nullable;

/**
 * @ClassName Checker
 * @Author ssmalllucky
 * @Date 2024/11/13
 * @Description 检测器基类
 */
public abstract class Checker {
    protected static boolean contextIsNull(@Nullable Context context) {
        return context == null;
    }
}
