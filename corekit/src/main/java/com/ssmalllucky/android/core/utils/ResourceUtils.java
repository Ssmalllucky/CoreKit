package com.ssmalllucky.android.core.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.InputStream;

/**
 * @ClassName ResourceUtils
 * @Author ssmalllucky
 * @Date 2024/10/17
 * @Description APP 资源获取工具类
 */
public class ResourceUtils {

    public static final String TAG = "ResourceUtils";

    /**
     * 通过资源名称字符串，查找资源ID。
     *
     * @param context      上下文
     * @param drawableName 资源名称字符串
     * @param context
     * @param drawableName
     * @return 资源ID，若context为null，或drawableName为null，或资源未找到，将返回-1.
     */
    public static int getResIdByDrawableName(Context context, String drawableName) {

        if (contextIsNull(context) || TextUtils.isEmpty(drawableName)) {
            return -1;
        }

        // 若没有找到资源，会抛出 Resources$NotFoundException 异常，需捕获。
        try {
            return context.getResources().getIdentifier(drawableName, "drawable", context.getPackageName());
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 读取asset目录下文件
     *
     * @param context 上下文
     * @param file
     * @param code
     * @return
     */
    public static String readAssetFile(Context context, String file, String code) {

        String result = "";

        if (contextIsNull(context)) {
            return result;
        }

        int len = 0;
        byte[] buf = null;
        try {
            InputStream in = context.getAssets().open(file);
            len = in.available();
            buf = new byte[len];
            in.read(buf, 0, len);
            result = new String(buf, code);
        } catch (Exception e) {
            Log.e(TAG, "readAssetFile: " + e.getCause());
        }
        return result;
    }

    /**
     * 检查给定的Context对象是否为null
     *
     * @param context 待检查的Context对象
     * @return 如果Context对象为null，则返回true；否则返回false
     */
    private static boolean contextIsNull(Context context) {
        return context == null;
    }
}
