package com.ssmalllucky.android.corekit.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

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

    /**
     * 获取系统版本号（内部）
     * @param context
     * @return
     */
    @SuppressWarnings("unused")
    public static int getVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            return (packageManager.getPackageInfo(context.getPackageName(), 0)).versionCode;
        } catch (PackageManager.NameNotFoundException nameNotFoundException) {
            return 1;
        }
    }

    /**
     * 获取系统版本号（外部）
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            return (packageManager.getPackageInfo(context.getPackageName(), 0)).versionName;
        } catch (PackageManager.NameNotFoundException nameNotFoundException) {
            return "";
        }
    }

    /**
     * 判断第三方APP是否可以启动
     *
     * @param context
     * @param packageName
     */
    @SuppressWarnings("unused")
    public static boolean isThirdPackageValid(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(packageName);
        return intent != null;
    }

    @SuppressWarnings("unused")
    public static void showSoftInput(EditText editText) {
        new Handler().postDelayed(() -> {
            InputMethodManager inputManager =
                    (InputMethodManager) editText.getContext().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(editText, 0);
        }, 500);
    }
}
