package com.seatrend.android.core.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @ClassName MarkdownReader
 * @Author shuaijialin
 * @Date 2024/11/12
 * @Description Markdown文件读取类。
 */
public class MarkdownReader {

    public static final String TAG = "MarkdownReader";

    public static class UpdateEntity {
        private String versionName;
        private SpannableStringBuilder updateContent;

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public SpannableStringBuilder getUpdateContent() {
            return updateContent;
        }

        public void setUpdateContent(SpannableStringBuilder updateContent) {
            this.updateContent = updateContent;
        }

        @Override
        public String toString() {
            return "UpdateEntity{" +
                    "versionName='" + versionName + '\'' +
                    ", updateContent='" + updateContent + '\'' +
                    '}';
        }
    }

    /**
     * 读取 Markdown 格式文件的指定部分
     *
     * @param context  上下文对象，用于访问应用程序资源
     * @param fileName 要读取的文件名
     * @return 返回一个 UpdateEntity 对象，其中包含读取的 Markdown 内容和版本信息
     */
    public static UpdateEntity readMarkdownSections(Context context, String fileName) {
        UpdateEntity updateEntity = new UpdateEntity();
        InputStreamReader inputReader;
        SpannableStringBuilder builder = new SpannableStringBuilder();
        try {
            inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader reader = new BufferedReader(inputReader);
            String line;
            boolean hasReadRoot = false;
            boolean isSection = false;

            SpannableString spannableString;

            while ((line = reader.readLine()) != null) {
                Log.d(TAG, "readMarkdownSections -> line: " + line);
                if (line.startsWith("### ")) {
                    if (hasReadRoot) {
                        break;
                    } else {
                        hasReadRoot = true;
                    }
                    if (isSection) {
                        builder.append("\n");
                    }
                    String versionMD = line.replace("###", "").trim();
                    updateEntity.setVersionName(versionMD.startsWith("V") ? versionMD : "V" + versionMD);
                    isSection = true;
                } else if (startsWithDigit(line)) {
                    String replaced = line.replaceAll("- ", "•");
                    replaced = replaced.replaceAll("\\**", "");
                    spannableString = new SpannableString(replaced);
                    spannableString.setSpan(new StyleSpan(Typeface.NORMAL), 0, replaced.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    builder.append(spannableString);
                    builder.append("\n");
                } else if (line.startsWith("- ")) {
                    String replaced = line.replaceAll("- ", "•");
                    replaced = replaced.replaceAll("\\**", "");
                    spannableString = new SpannableString(replaced);
                    spannableString.setSpan(new StyleSpan(Typeface.NORMAL), 0, replaced.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    builder.append(spannableString);
                    builder.append("\n");
                } else if (line.contains(":")) {
                    String replaced = line.substring(line.indexOf(":") + 1).trim();
                    replaced = replaced.replaceAll("\\**", "");
                    spannableString = new SpannableString(replaced);
                    spannableString.setSpan(new StyleSpan(Typeface.NORMAL), 0, replaced.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    builder.append(spannableString);
                    builder.append("\n");
                } else if (line.startsWith("【")) {
                    String replaced = line.substring(line.indexOf("【") + 1).trim();
                    replaced = replaced.replaceAll("】", "");
                    spannableString = new SpannableString(replaced);
                    spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, replaced.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    builder.append(spannableString);
                    builder.append("\n");
                } else {
                    spannableString = new SpannableString(line);
                    spannableString.setSpan(new StyleSpan(Typeface.NORMAL), 0, line.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    builder.append(spannableString);
                    builder.append("\n");
                }
            }
            reader.close();
        } catch (IOException e) {
            Log.e(TAG, "readMarkdownSections -> exception: " + e.getCause());
            return null;
        }
        updateEntity.setUpdateContent(builder);
        Log.d(TAG, "readMarkdownSections: " + updateEntity);
        return updateEntity;
    }

    /**
     * 检查字符串是否以数字开始
     * <p>
     * 此方法通过正则表达式验证字符串的开头是否为数字（0-9）它首先检查输入字符串是否为null，
     * 然后使用matches方法和正则表达式"^[0-9].*"来确定字符串是否以数字开头正则表达式的含义是：
     * ^ 表示字符串的开始，
     * [0-9] 表示任何一个数字字符，
     * .* 表示任意数量的任何字符
     *
     * @param input 要检查的字符串
     * @return 如果字符串不为null且以数字开始，则返回true；否则返回false
     */
    public static boolean startsWithDigit(String input) {
        return input != null && input.matches("^[0-9].*");
    }
}

