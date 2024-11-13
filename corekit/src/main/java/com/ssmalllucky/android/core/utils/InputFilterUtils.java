package com.ssmalllucky.android.core.utils;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;

/**
 * @ClassName InputFilterUtils
 * @Author ssmalllucky
 * @Date 2023/11/15
 * @Description
 */
public class InputFilterUtils {

    /**
     * 整数正则表达式
     */
    public static final String PATTERN_NUMBER = "^0|\\d+$";

    /**
     * 整数或小数正则表达式
     */
    public static final String PATTERN_DECIMAL = "^\\d+(\\.\\d+)?$";

    public static final String PATTERN_VIN = "[A-HJ-NPR-Z0-9]{17}";

    public static InputFilter getNumberFilter() {
        return (source, start, end, dest, dstart, dend) -> {
            if (source.toString().matches(InputFilterUtils.PATTERN_NUMBER)) {
                return source;
            } else {
                return "";
            }
        };
    }

    public static TextWatcher getVINTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 文本变化后的操作
                String filteredStr = s.toString().toUpperCase().replaceAll("[^A-HJ-NPR-Z0-9]", "");
                if (!filteredStr.equals(s.toString())) {
                    s.replace(0, s.length(), filteredStr);
                }
            }
        };
    }

    public static TextWatcher getPlateNumberTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String filteredStr = s.toString().toUpperCase().replaceAll("[^A-Z0-9]", "");
                if (!filteredStr.equals(s.toString())) {
                    s.replace(0, s.length(), filteredStr);
                }
            }
        };
    }

    public static InputFilter getFilter(String matcher) {
        return (source, start, end, dest, dstart, dend) -> {
            if (matcher != null && !matcher.contains(source)) {
                return source;
            } else {
                return "";
            }
        };
    }

    public static InputFilter getDecimalFilter() {
        return (source, start, end, dest, dstart, dend) -> {
            if (source.toString().matches(InputFilterUtils.PATTERN_DECIMAL)) {
                return source;
            } else {
                return "";
            }
        };
    }
}
