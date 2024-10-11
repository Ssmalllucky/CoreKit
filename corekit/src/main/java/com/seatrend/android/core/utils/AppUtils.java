package com.seatrend.android.core.utils;

import android.content.Context;
import android.content.Intent;

/**
 * @ClassName AppUtils
 * @Author shuaijialin
 * @Date 2024/10/11
 * @Description Application 帮助类
 */
public class AppUtils {

    /**
     * 重启 APP
     *  <p />
     * 此函数在 Android 8.0、Android 12、Android 13 上验证通过
     * @param context 发起跳转的Activity
     */
    public void restartApp(Context context, Class<?> clazz) {
        Intent intent = new Intent(context, clazz);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        // 结束当前应用的进程
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
}
