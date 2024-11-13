package com.ssmalllucky.android.core.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import androidx.core.content.ContextCompat;

import com.ssmalllucky.android.core.safe.Checker;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * @ClassName AndroidUtils
 * @Author ssmalllucky
 * @Date 2023/12/11
 * @Description
 */
public class AndroidUtils extends Checker {

    /**
     * 获取网络状态，WIFI 或 移动数据
     *
     * @param context APP上下文
     * @return 网络类型，W为WIFI，G为移动数据
     */
    public static String getNetWorkType(Context context) {
        if (contextIsNull(context)) {
            return "";
        }

        if (isCellular(context)) {
            return "G";
        } else if (isWIFI(context)) {
            return "W";
        } else {
            return "";
        }
    }

    /**
     * 是否为移动数据网络
     *
     * @param context APP上下文
     * @return
     */
    public static boolean isCellular(Context context) {

        if (contextIsNull(context)) {
            return false;
        }

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network networkInfo = manager.getActiveNetwork();

        if (networkInfo == null) {
            return false;
        }

        NetworkCapabilities capabilities = manager.getNetworkCapabilities(networkInfo);

        if (capabilities == null) {
            return false;
        }

        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR);
    }

    /**
     * 是否为WIFI数据网络
     *
     * @param context APP上下文
     * @return
     */
    public static boolean isWIFI(Context context) {

        if (contextIsNull(context)) {
            return false;
        }

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network networkInfo = manager.getActiveNetwork();

        if (networkInfo == null) {
            return false;
        }

        NetworkCapabilities capabilities = manager.getNetworkCapabilities(networkInfo);

        if (capabilities == null) {
            return false;
        }

        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
    }

    /**
     * 获取Imei号
     */
    @SuppressLint("HardwareIds")
    public static String getIMEI(Context context) {

        String IMEI = "";

        if (contextIsNull(context)) {
            return IMEI;
        }

        int checkPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE);
        if (checkPermission == PackageManager.PERMISSION_GRANTED) {
            IMEI = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        }
        return IMEI;
    }

    /**
     * 获取Imes号
     */
    @SuppressLint("HardwareIds")
    public static String getAndroidID(Context context) {
        String androidId = "";

        if (contextIsNull(context)) {
            return androidId;
        }

        int checkPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE);
        if (checkPermission == PackageManager.PERMISSION_GRANTED) {
            androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return androidId;
    }

    /**
     * 获取手机号
     */
    @SuppressLint("HardwareIds")
    public static String getPhoneNum(Context context) {
        String NativePhoneNumber = "";

        if (contextIsNull(context)) {
            return "";
        }

        int checkPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE);
        if (checkPermission == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager telephonyManager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);

            NativePhoneNumber = telephonyManager.getLine1Number();
        }
        return NativePhoneNumber;
    }

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            return "";
        }
        return "";
    }

    /**
     * 验证目标Activity是否存在
     *
     * @param context
     * @param packageName
     * @param className
     * @return
     */
    public static boolean isActivityExist(Context context, String packageName, String className) {
        if (TextUtils.isEmpty(packageName) || TextUtils.isEmpty(className)) {
            return false;
        }

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setComponent(new ComponentName(packageName, className));
        return context.getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null;
    }
}
