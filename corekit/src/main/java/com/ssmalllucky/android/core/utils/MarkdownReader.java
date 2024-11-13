package com.ssmalllucky.android.core.utils;

import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
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
     * 从输入流中读取 Markdown 格式的更新日志信息，并解析到 UpdateEntity 对象中
     * 该方法主要用于解析 CHANGELOG.md 文件中的更新信息，以便在应用中展示
     *
     * @param inputStream 输入流，通常来源于 CHANGELOG.md 文件
     * @return 解析后的 UpdateEntity 对象，包含版本号和更新内容
     */
    public static UpdateEntity readMarkdownSections(InputStream inputStream) {
        UpdateEntity updateEntity = new UpdateEntity();
        InputStreamReader inputReader;
        SpannableStringBuilder builder = new SpannableStringBuilder();
        try {
            inputReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputReader);
            String line;
            boolean hasReadRoot = false;

            int lineNumber = 0;

            Log.d(TAG, "Starting to read the file contents through the while loop...");

            while ((line = reader.readLine()) != null) {
                Log.d(TAG, "lineNumber: " + (lineNumber++));
                // 检查是否是版本号
                if (line.startsWith("### ")) {
                    // 确保只读取一次根版本号
                    if (hasReadRoot) {
                        break;
                    } else {
                        hasReadRoot = true;
                    }

                    // 提取版本号
                    String versionMD = line.replace("###", "").trim();
                    updateEntity.setVersionName(versionMD.startsWith("V") ? versionMD : "V" + versionMD);
                } else if (startsWithDigit(line)) {
                    resolveStartsWithDigit(line, builder);
                } else if (line.startsWith("- ")) {
                    resolveStartWithDash(line, builder);
                } else if (line.contains(":")) {
                    resolveStartsWithColon(line, builder);
                } else if (line.startsWith("【")) {
                    resolveStartsSquareBracketsLeft(line, builder);
                }
                // 以“--”开头的行不会被 Markdown 进行标记和格式化，所以适合作为不想返回在结果中的行（但可以出现在git提交内容
                // 和 CHANGELOG.md文件中
                else if (line.startsWith("--")) {
                    builder.append("");
                } else {
                    resolveNormalLine(line, builder);
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
     * 格式化以数字字符开头的行，并移除任何星号，处理完成后添加换行符，最终返回格式化后的文本
     *
     * @param line    需要处理的原始字符串，检查并替换特定开头字符
     * @param builder 用于累积处理后文本的SpannableStringBuilder对象
     */
    private static void resolveStartsWithDigit(String line, SpannableStringBuilder builder) {
        // 将行首的破折号和空格替换为项目符号符号
        String replaced = line.replaceAll("- ", "•");
        // 移除所有的星号
        replaced = replaced.replaceAll("\\**", "");
        // 创建一个新的SpannableString对象来保存替换后的字符串
        SpannableString spannableString = new SpannableString(replaced);
        // 设置整个字符串的样式为正常，以统一格式
        spannableString.setSpan(new StyleSpan(Typeface.NORMAL), 0, replaced.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 将格式化后的文本追加到构建器中
        builder.append(spannableString);
        // 追加换行符以分隔不同的文本行
        builder.append("\n");
    }

    /**
     * 处理以“- ”开头的行，并移除多余的星号，处理完成后添加换行符，最终返回格式化后的SpannableStringBuilder
     *
     * @param line    需要处理的字符串
     * @param builder SpannableStringBuilder实例，用于收集和格式化处理后的文本
     */
    private static void resolveStartWithDash(String line, SpannableStringBuilder builder) {
        // 将破折号和空格替换为子弹点
        String replaced = line.replaceAll("- ", "•");
        // 移除所有多余的星号
        replaced = replaced.replaceAll("\\**", "");
        // 创建一个SpannableString实例用于进一步的格式化
        SpannableString spannableString = new SpannableString(replaced);
        // 设置文本为普通样式
        spannableString.setSpan(new StyleSpan(Typeface.NORMAL), 0, replaced.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 将格式化后的文本追加到builder中
        builder.append(spannableString);
        // 添加换行符以区分不同的文本行
        builder.append("\n");
    }

    /**
     * 处理以“:”开头的行，并移除多余的星号，处理完成后添加换行符，最终返回格式化后的SpannableStringBuilder
     *
     * @param line    需要处理的文本行，预期以冒号开头
     * @param builder SpannableStringBuilder实例，用于追加格式化后的文本
     */
    private static void resolveStartsWithColon(String line, SpannableStringBuilder builder) {
        // 移除冒号和其后可能存在的多余星号，并修剪前导空格
        String replaced = line.substring(line.indexOf(":") + 1).trim();
        replaced = replaced.replaceAll("\\**", "");

        // 创建SpannableString实例，用于设置文本样式
        SpannableString spannableString = new SpannableString(replaced);

        // 设置文本为正常样式
        spannableString.setSpan(new StyleSpan(Typeface.NORMAL), 0, replaced.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // 将格式化后的文本追加到builder中，并添加换行符
        builder.append(spannableString);
        builder.append("\n");
    }

    /**
     * 处理以“【”开头的行，并移除多余的星号，处理完成后添加换行符，最终返回格式化后的SpannableStringBuilder
     *
     * @param line    包含“【” 字符的原始字符串
     * @param builder SpannableStringBuilder对象，用于接收格式化后的文本
     */
    private static void resolveStartsSquareBracketsLeft(String line, SpannableStringBuilder builder) {
        // 移除【】符号并提取其中的内容
        String replaced = line.substring(line.indexOf("【") + 1).trim();
        replaced = replaced.replaceAll("】", "");

        // 创建SpannableString对象以对提取的文本进行格式化
        SpannableString spannableString = new SpannableString(replaced);

        // 设置文本为粗体格式
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, replaced.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // 将格式化后的文本追加到builder中，并添加换行符以分隔不同的文本块
        builder.append(spannableString);
        builder.append("\n");
    }

    /**
     * 处理正常文本行，为其添加普通样式并追加换行符
     *
     * @param line    需要处理的文本行
     * @param builder 用于追加文本的SpannableStringBuilder对象
     */
    private static void resolveNormalLine(String line, SpannableStringBuilder builder) {
        // 创建一个SpannableString对象，用于设置文本样式
        SpannableString spannableString = new SpannableString(line);
        // 为整个文本设置普通样式
        spannableString.setSpan(new StyleSpan(Typeface.NORMAL), 0, line.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 将格式化后的文本追加到builder中
        builder.append(spannableString);
        // 追加换行符，以便于下一行文本的显示
        builder.append("\n");
    }

    public static boolean startsWithDigit(String input) {
        return input != null && input.matches("^[0-9].*");
    }
}

