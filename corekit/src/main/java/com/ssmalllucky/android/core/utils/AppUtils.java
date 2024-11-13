package com.ssmalllucky.android.core.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;

/**
 * @ClassName AppUtils
 * @Author ssmalllucky
 * @Date 2024/10/11
 * @Description App 帮助类
 */
public class AppUtils {

    public static final String TAG = "AppUtils";

    /**
     * 重启应用
     * <p>
     * 本方法通过启动一个新的指定活动来实现应用的重启在重启之前，它会结束当前应用的所有活动，
     * 确保用户看到的是应用的新实例此方法适用于应用需要重新初始化所有组件和状态的场景。
     * 此函数在 Android 8.0、Android 12、Android 13 上验证通过
     *
     * @param context 上下文，用于启动新的活动
     * @param clazz   指定要启动的活动类，通常是应用的主活动
     */
    public void restartApp(Context context, Class<?> clazz) {
        // 创建意图，用于启动新的活动
        Intent intent = new Intent(context, clazz);
        // 添加标志，以确保新的活动是单独的任务，并清除之前的所有任务
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // 启动新的活动
        context.startActivity(intent);

        // 结束当前应用的进程
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    /**
     * 调用相机拍照
     *
     * @param savePath 相机拍照后保存照片的文件夹路径
     * @param fileName 照片文件名称
     */
    public static void captureImage(Context context, String savePath, String fileName, @NonNull String authority, ActivityResultLauncher<Uri> mGetContent) {
        File parentDirs = new File(savePath);
        Uri imageUri;
        if (!parentDirs.exists() || !parentDirs.isDirectory()) {
            boolean mkCreated = parentDirs.mkdirs();
            if (!mkCreated) {
                Log.d(TAG, "无法在指定路径创建文件夹，文件夹已存在。");
            }
        }

        File imageFile = new File(parentDirs, fileName);

        try {
            boolean fileCreated = imageFile.createNewFile();
            if (!fileCreated) {
                Log.d(TAG, "无法在指定路径创建文件，文件已存在。");
            }
        } catch (IOException e) {
            return;
        }

        imageUri = FileProvider.getUriForFile(context, authority, imageFile);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        mGetContent.launch(imageUri);
    }

    /**
     * 从相册中选择图片
     * <p>
     * 此方法用于启动一个意图，以选择来自设备相册的图片
     * 它设置意图的动作和类型，以确保用户可以选择图片文件
     */
    public static void selectFromAlbum() {
        // 创建一个意图，用于从相册选择内容
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        // 设置意图的类型，仅选择图像文件
        intent.setType("image/*");
    }
}
