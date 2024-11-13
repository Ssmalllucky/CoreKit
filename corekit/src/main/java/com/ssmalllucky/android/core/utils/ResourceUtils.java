package com.ssmalllucky.android.core.utils;

import android.content.Context;
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
     * 读取asset目录下文件
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

    private static boolean contextIsNull(Context context) {
        return context == null;
    }
}
