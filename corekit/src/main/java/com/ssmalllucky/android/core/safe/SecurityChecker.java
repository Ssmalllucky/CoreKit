package com.ssmalllucky.android.core.safe;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.provider.Settings;
import android.util.Log;

import com.scottyab.rootbeer.RootBeer;

/**
 * @ClassName SecurityChecker
 * @Author shuaijialin
 * @Date 2024/10/11
 * @Description 安全检查工具类，用于检查设备是否处于安全状态
 */
public class SecurityChecker {

    public static final String TAG = "SecurityChecker";

    /**
     * 检查USB调试是否已启用
     *
     * @param context 应用程序上下文，用于访问全局设置
     * @return 返回一个布尔值，指示USB调试是否已启用
     */
    public static boolean isUsbDebuggingEnabled(Context context) {
        // 从全局设置中获取ADB_ENABLED选项的值，如果未设置，则默认为0
        boolean debugEnabled = Settings.Global.getInt(context.getContentResolver(),
                Settings.Global.ADB_ENABLED, 0) == 1;
        Log.d(TAG, "isUsbDebuggingEnabled: " + debugEnabled);
        // 返回USB调试是否已启用的布尔值
        return debugEnabled;
    }

    /**
     * 检查应用程序是否处于调试模式
     *
     * @param context 应用程序上下文，用于获取应用信息
     * @return 如果应用程序是可调试的，则返回true；否则返回false
     * <p>
     * 此方法尝试获取应用程序的信息，并检查其是否被标记为可调试
     * 如果在获取应用信息过程中遇到任何异常，将默认返回false，即认为应用不处于调试模式
     */
    public static boolean isDebugMode(Context context) {
        try {
            // 获取应用程序信息
            ApplicationInfo info = context.getApplicationInfo();
            // 检查应用信息中的标志是否包含可调试标志
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception x) {
            // 在发生异常时，默认认为应用不处于调试模式
            return false;
        }
    }

    /**
     * 检查设备是否获取了root权限
     *
     * @param context 应用程序上下文，用于访问应用程序特定资源
     * @return 如果设备获取了root权限，则返回true；否则返回false
     */
    public static boolean isRooted(Context context) {
        // 创建RootBeer实例，用于检测设备是否root
        RootBeer rootBeer = new RootBeer(context);
        // 调用RootBeer的isRooted方法检查设备是否获取root权限
        boolean isRooted = rootBeer.isRooted();
        // 在日志中记录设备是否获取了root权限
        Log.d(TAG, "isRooted: " + isRooted);
        // 返回设备是否获取了root权限的结果
        return isRooted;
    }
}