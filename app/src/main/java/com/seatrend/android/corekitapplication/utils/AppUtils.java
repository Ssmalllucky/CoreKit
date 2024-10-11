package com.seatrend.android.corekitapplication.utils;

import android.content.Context;
import android.content.Intent;

/**
 * @ClassName AppUtils
 * @Author shuaijialin
 * @Date 2024/10/11
 * @Description Application 全局工具类
 */
public class AppUtils {

    /**
     * 重新启动 APP。
     * <br />
     * 此函数在 Android 8.0、Android 12、Android 13 上测试通过。
     * @param context 跳转来源Activity
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
