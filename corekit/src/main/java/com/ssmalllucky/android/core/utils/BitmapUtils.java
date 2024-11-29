package com.ssmalllucky.android.core.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;

import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @ClassName BitmapUtils
 * @Author ssmalllucky
 * @Date 2023/7/26
 * @Description
 */
public class BitmapUtils {

    public static final String TAG = "BitmapUtils";

    public static String saveBitmap(Bitmap bitmap, String filePath, String fileName) {

        File catalog = new File(filePath);
        if (!catalog.exists() || !catalog.isDirectory()) {
            catalog.mkdirs();
        }

        File file = new File(catalog, fileName);

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            return file.getPath();
        } catch (Exception e) {
            return null;
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String saveBitmap(Bitmap bitmap, String absFilePath) {
        File file = new File(absFilePath);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            return file.getPath();
        } catch (Exception e) {
            return null;
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 压缩图片质量
     *
     * @param image 需要压缩的图片Bitmap对象
     * @return 压缩完成后的Bitmap对象
     */
    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int quality = 100;
        final BitmapFactory.Options options = new BitmapFactory.Options();
        //设置默认压缩比
        options.inSampleSize = 2;
        //先设置不填充Bitmap对象内容（为了更快地计算Bitmap的宽高）
        options.inJustDecodeBounds = true;
        ByteArrayInputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.decodeStream(inputStream, null, options);

        //拿到默认的Bitmap宽高
        int placeWidth = options.outWidth;
        int placeHeight = options.outHeight;

        //根据较大的数字处理压缩（因为横屏或者竖屏状态下，手机的宽高值会相互替换）
        while (Math.max(placeWidth, placeHeight) > 800) {
            inputStream.reset();
            options.inSampleSize += 2;
            BitmapFactory.decodeStream(inputStream, null, options);
            placeWidth = options.outWidth;
            placeHeight = options.outHeight;
            Log.d(TAG, "compressImage: while:  " + placeWidth + " * " + placeHeight);
        }

        Log.d(TAG, "compressImage: final width and height: " + options.outWidth + " * " + options.outHeight);
        Log.d(TAG, "compressImage: baos.toByteArray().length before: " + baos.toByteArray().length);

        options.inJustDecodeBounds = false;
        while (baos.toByteArray().length / 1024 > 300) {
            baos.reset();
            image.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            quality -= 10;
            Log.d(TAG, "compressImage: quality changed: nowSize: " + baos.toByteArray().length / 1024 + " KB");
            Log.d(TAG, "compressImage: quality changed: " + quality);
            if (quality <= 0) {
                break;
            }
        }
        Log.d(TAG, "compressImage: quality: " + quality);
        Log.d(TAG, "compressImage: baos.toByteArray().length after: " + baos.toByteArray().length);
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, options);
        return bitmap;
    }

    public static Bitmap getBitmapVector(Context context, int vectorDrawableId) {
        final VectorDrawableCompat drawable = VectorDrawableCompat.create(context.getResources(), vectorDrawableId, null);
        if (drawable == null) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 获取Bitmap内存占用
     *
     * @param bitmap
     * @return
     */
    public int getBitmapSize(Bitmap bitmap) {

        if (bitmap == null) {
            return -1;
        }

        if (Build.VERSION.SDK_INT >= 19) {
            return bitmap.getAllocationByteCount();
        }

        if (Build.VERSION.SDK_INT >= 12) {
            return bitmap.getByteCount();
        }

        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    /**
     * 给 Bitmap 添加水印，支持添加多行水印。
     *
     * @param context     上下文
     * @param bitmap      Bitmap图片对象
     * @param waterString 水印内容
     * @return
     */
    public static Bitmap addWaterMaskingToBitmap(Context context, Bitmap bitmap, String waterString) {

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int textHeight = h / 20;
        int textSize = w / 80;

        try {

            Bitmap newBitmap = Bitmap.createBitmap(w, h + textHeight, Bitmap.Config.RGB_565);

            Canvas whiteCanvas = new Canvas(newBitmap);

            // 绘制文本内容，可能包含多行文本，所以不能使用 drawText，应该使用 StaticLayout
            TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            textPaint.setColor(Color.GRAY);
            textPaint.setTextSize(dip2px(context, textSize));
            StaticLayout staticLayout = new StaticLayout(waterString, textPaint, whiteCanvas.getWidth(), Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
            newBitmap.recycle();
            newBitmap = Bitmap.createBitmap(w, h + staticLayout.getHeight(), Bitmap.Config.RGB_565);
            whiteCanvas = new Canvas(newBitmap);

            Paint paint = new Paint();
            paint.setAntiAlias(true);
            whiteCanvas.drawARGB(255, 255, 255, 255);
            paint.setColor(Color.GRAY);
            whiteCanvas.drawBitmap(bitmap, 0, 0, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
            whiteCanvas.drawRect(0, 0, w, h + textHeight, paint);
            Paint bitpaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            bitpaint.setColor(Color.GRAY);
            bitpaint.setTextSize(dip2px(context, textSize));
            Rect bounds = new Rect();
            bitpaint.getTextBounds(waterString, 0, waterString.length(), bounds);

            float y = whiteCanvas.getHeight() - staticLayout.getHeight();
            whiteCanvas.save();
            whiteCanvas.translate(20, y);
            staticLayout.draw(whiteCanvas);
            whiteCanvas.restore();
            return newBitmap;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
