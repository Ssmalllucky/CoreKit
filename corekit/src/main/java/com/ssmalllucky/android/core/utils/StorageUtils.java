package com.ssmalllucky.android.core.utils;

import android.app.usage.StorageStats;
import android.app.usage.StorageStatsManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.UserHandle;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.File;
import java.util.UUID;

/**
 * @ClassName StorageUtils
 * @Author ssmalllucky
 * @Date 2024/10/17
 * @Description 手机存储空间工具类
 */
public class StorageUtils {

    public static final String TAG = "StorageUtils";
    public static final String FORMAT_BYTE = "B";
    public static final String FORMAT_KB = "KB";
    public static final String FORMAT_MB = "MB";
    public static final String FORMAT_GB = "GB";

    private static long getTotalSpace(File file) {
        StatFs statFs = new StatFs(file.getPath());
        return statFs.getBlockSizeLong() * statFs.getBlockCountLong();
    }

    // 获取剩余空间
    private static long getFreeSpace(File file) {
        StatFs statFs = new StatFs(file.getPath());
        return statFs.getBlockSizeLong() * statFs.getAvailableBlocksLong();
    }

    // 获取已用空间
    private static long getUsedSpace(File file) {
        return getTotalSpace(file) - getFreeSpace(file);
    }

    /**
     * 获取手机已使用的存储空间
     * <p>
     * 本方法通过访问手机的根目录并计算其已使用空间来获取手机的存储使用情况
     * 它使用Environment类的getDataDirectory()方法来获取手机数据目录，
     * 然后调用getUsedSpace方法计算已使用空间
     *
     * @return 手机已使用的存储空间，以字节为单位
     */
    public static long getPhoneUsedSpace() {
        File root = Environment.getDataDirectory(); // 获取手机根目录
        return getUsedSpace(root);
    }

    /**
     * 获取手机已使用的存储空间大小
     *
     * @param format 存储空间大小的格式化方式，用于指定返回的大小单位（如B、KB、MB、GB等）
     * @return 根据给定格式格式化后的手机已使用存储空间大小的字符串表示
     */
    public static String getPhoneUsedSize(String format) {
        File root = Environment.getDataDirectory(); // 获取手机根目录
        long usedSpace = getUsedSpace(root);
        String formattedStorageSize = formatStorageSize(usedSpace, format);
        Log.d(TAG, "phoneUsedSize: " + formattedStorageSize);
        return formattedStorageSize;
    }

    /**
     * 获取总存储空间大小
     * <p>
     * 此方法根据指定的格式返回设备的总存储空间大小它首先计算总空间大小，
     * 然后根据提供的格式进行格式化如果未提供格式或者格式无效，将使用默认格式
     *
     * @param format 存储空间大小的格式字符串，例如"%.2f"表示两位小数
     * @return 格式化后的总存储空间大小字符串
     */
    public static String getTotalStorageSize(String format) {
        // 获取设备的总存储空间大小（字节）
        long totalSpace = getTotalStorageSpace();
        // 根据指定的格式格式化总存储空间大小
        String formattedTotalSize = formatStorageSize(totalSpace, format);
        // 日志输出格式化后的总存储空间大小
        Log.d(TAG, "phoneTotalSize: " + formattedTotalSize);
        // 返回格式化后的总存储空间大小字符串
        return formattedTotalSize;
    }

    /**
     * Android 获取内置存储空间大小（适用于Android 8.0以下）
     *
     * @param context
     * @param format
     * @return
     */
    public static String getInternalStorageSize(Context context, String format) {
        long space = getInternalStorageSpace(context);
        String formattedSize = formatStorageSize(space, format);
        Log.d(TAG, "internalStorageSize: " + formattedSize);
        return formattedSize;
    }

    /**
     * 获取外置存储空间大小（适用于Android 8.0以下）
     *
     * @param context
     * @param format
     * @return
     */
    public static String getExternalStorageSize(Context context, String format) {
        long space = getExternalStorageSpace(context);
        String formattedSize = formatStorageSize(space, format);
        Log.d(TAG, "externalStorageSize: " + formattedSize);
        return formattedSize;
    }

    /**
     * 获取外置存储空间大小（long类型，适用于Android 8.0以下）
     *
     * @param context
     * @return
     */
    public static long getExternalStorageSpace(Context context) {
        StorageManager storageManager = context.getSystemService(StorageManager.class);
        for (StorageVolume volume : storageManager.getStorageVolumes()) {
            if (volume.isRemovable()) {
                if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                    File directory = volume.getDirectory();
                    Log.d(TAG, "present sdk >= Android R,volume.isRemoved.directory = " + directory.getAbsolutePath());
                    Log.d(TAG, "present sdk >= Android R,volume.isRemoved.directory space = " + directory.getTotalSpace());
                    return directory.getTotalSpace();
                }
            } else {
                if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                    File directory = volume.getDirectory();
                    Log.d(TAG, "volume.!isRemoved. directory = " + directory.getAbsolutePath());
                    Log.d(TAG, "volume.!isRemoved. directory space = " + directory.getTotalSpace());
                    return directory.getTotalSpace();
                }
            }
        }
        return 0;
    }

    /**
     * 获取内置存储空间大小（long类型，适用于Android 8.0以下）
     *
     * @param context
     * @return
     */
    public static long getInternalStorageSpace(Context context) {
        File filesDir = context.getFilesDir();
        return filesDir.getTotalSpace();
    }

    public static long getFreeSpace(Context context) {
        File filesDir = context.getFilesDir();
        return filesDir.getFreeSpace();
    }


    /**
     * 获取设备的总存储空间
     *
     * @return 设备的总存储空间，以字节为单位
     */
    public static long getTotalStorageSpace() {
        // 获取外部存储路径（如果存在）
        File storageDir = Environment.getExternalStorageDirectory();

        // 使用 StatFs 获取存储信息
        StatFs statFs = new StatFs(storageDir.getPath());
        // 获取存储块的大小
        long blockSize = statFs.getBlockSizeLong();
        // 获取总块数
        long totalBlocks = statFs.getBlockCountLong();

        // 计算总存储空间（字节）
        return blockSize * totalBlocks;
    }

    /**
     * 格式化存储大小，使其更易于阅读
     * 此方法将给定的字节大小转换为适当的单位（B、KB、MB、GB），并格式化为两位小数
     *
     * @param sizeInBytes 需要格式化的存储大小，以字节为单位
     * @return 格式化后的存储大小字符串，形如 "123.45 KB"
     */
    public static String formatStorageSize(long sizeInBytes, @Nullable String format) {
        double size = sizeInBytes;

        // 单位数组
        String[] units = {"B", "KB", "MB", "GB"};
        int targetIndex = -1;

        // 查找目标单位的索引
        if (format != null) {
            for (int i = 0; i < units.length; i++) {
                if (units[i].equalsIgnoreCase(format)) {
                    targetIndex = i;
                    break;
                }
            }

            if (targetIndex != -1) {
                // 转换到目标单位
                for (int i = 0; i < targetIndex; i++) {
                    size /= 1024; // 每次除以1024，向目标单位逼近
                }

                // 格式化到两位小数
                return String.format("%.2f %s", size, units[targetIndex]);
            } else {
                return getNearFormatSize(sizeInBytes);
            }
        } else {
            return getNearFormatSize(sizeInBytes);
        }
    }

    private static String getNearFormatSize(long sizeInBytes) {
        final long KB = 1024;
        final long MB = KB * 1024;
        final long GB = MB * 1024;

        if (sizeInBytes >= GB) {
            return String.format("%.2f GB", (double) sizeInBytes / GB);
        } else if (sizeInBytes >= MB) {
            return String.format("%.2f MB", (double) sizeInBytes / MB);
        } else if (sizeInBytes >= KB) {
            return String.format("%.2f KB", (double) sizeInBytes / KB);
        } else {
            return sizeInBytes + " B";
        }
    }

    /**
     * 获取应用的存储空间大小并按指定格式返回
     *
     * @param context 应用上下文，用于访问应用的资源和数据库
     * @param format  指定的格式字符串，用于定义返回的存储空间大小的格式
     * @return 格式化后的存储空间大小字符串
     */
    public static String getAppStorageSize(Context context, String format) {
        // 获取设备的总存储空间大小（字节）
        long totalSpace = getAppStorageSpace(context);

        if (totalSpace == -1) {
            return null;
        }

        // 根据指定的格式格式化总存储空间大小
        String formattedSize = formatStorageSize(totalSpace, format);
        // 日志输出格式化后的总存储空间大小
        Log.d(TAG, "appStorageSize: " + formattedSize);
        // 返回格式化后的总存储空间大小字符串
        return formattedSize;
    }

    // 获取应用占用的存储空间（单位：字节）
    public static long getAppStorageSpace(Context context) {

        if (contextIsNull(context)) {
            return -1;
        }

        // 获取应用的内部存储目录
        File appDataDir = context.getExternalCacheDir();

        // 计算应用内部存储空间占用情况
        return getFolderSize(appDataDir);
    }

    // 计算指定文件夹的大小（单位：字节）
    private static long getFolderSize(File folder) {
        long size = 0;
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // 如果是目录，递归计算文件夹大小
                    size += getFolderSize(file);
                } else {
                    // 如果是文件，直接累加文件大小
                    size += file.length();
                }
            }
        }
        return size;
    }

    private static boolean contextIsNull(Context context) {
        return context == null;
    }

    /**
     * 获得SD卡总大小
     *
     * @return
     */
    public static String getSDTotalSize() {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        String s = formatStorageSize(blockSize * totalBlocks, null);
        Log.d(TAG, "getSDTotalSize: " + s);
        return s;
    }

    public static long getAppPackageSpace(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                StorageStatsManager storageStatsManager = (StorageStatsManager) context.getSystemService(Context.STORAGE_STATS_SERVICE);
                PackageManager packageManager = context.getPackageManager();
                String packageName = context.getPackageName();

                // 获取用户信息
                UserHandle userHandle = android.os.Process.myUserHandle();
                UUID uuid = UUID.fromString("41217664-9172-527a-b3d5-edabb50a7d69");

                // 获取应用存储统计信息
                StorageStats storageStats = storageStatsManager.queryStatsForPackage(uuid, packageName, userHandle);
                long space = storageStats.getAppBytes() + storageStats.getDataBytes() + storageStats.getCacheBytes();
                Log.d(TAG, "app package Space: " + space + " bytes");
                return space;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0L;
    }

    public static String getAppPackageSize(Context context) {
        long appPackageSpace = getAppPackageSpace(context);
        String formattedSize = formatStorageSize(appPackageSpace, StorageUtils.FORMAT_MB);
        Log.d(TAG, "appPackageSize: " + formattedSize);
        return formattedSize;
    }
}
