package com.ssmalllucky.android.core.utils;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 对使用Gson框架处理Json数据的封装
 *
 * @author ssmalllucky
 */
public class GsonUtils {
    private static Gson gson;

    static {
        gson = new Gson();
    }

    public static <T> T fromJson(String jsonString, Class<T> classOfT) {
        try {
            return gson.fromJson(jsonString, classOfT);
        } catch (Exception e) {
            return null;
        }
    }

    @SuppressWarnings("unused")
    public static <T> T fromJson(String jsonString, Type typeOfT) {
        return gson.fromJson(jsonString, typeOfT);
    }

    public static String toJson(Object src) {
        return gson.toJson(src);
    }

    @SuppressWarnings("unused")
    public static Type getType(final Class<?> rawClass, final Class<?> parameterClass) {
        return new ParameterizedType() {
            @NonNull
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[]{parameterClass};
            }

            @NonNull
            @Override
            public Type getRawType() {
                return rawClass;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }

        };
    }

    @SuppressWarnings("unused")
    public static <T> ArrayList<T> jsonToArrayList(String json, Class<T> clazz) {
        Type type = new TypeToken<ArrayList<JsonObject>>() {
        }.getType();
        ArrayList<JsonObject> jsonObjects = new Gson().fromJson(json, type);

        ArrayList<T> arrayList = new ArrayList<>();
        for (JsonObject jsonObject : jsonObjects) {
            arrayList.add(new Gson().fromJson(jsonObject, clazz));
        }
        return arrayList;
    }

    @SuppressWarnings("unused")
    public static String format(String mJson) {

        if (TextUtils.isEmpty(mJson)) {
            return null;
        }

        StringBuilder source = new StringBuilder(mJson);
        int offset = 0;//目标字符串插入空格偏移量
        int bOffset = 0;//空格偏移量
        for (int i = 0; i < mJson.length(); i++) {
            char charAt = mJson.charAt(i);
            if (charAt == '{' || charAt == '[') {
                bOffset += 4;
                source.insert(i + offset + 1, "\n" + generateBlank(bOffset));
                offset += (bOffset + 1);
            } else if (charAt == ',') {
                source.insert(i + offset + 1, "\n" + generateBlank(bOffset));
                offset += (bOffset + 1);
            } else if (charAt == '}' || charAt == ']') {
                bOffset -= 4;
                source.insert(i + offset, "\n" + generateBlank(bOffset));
                offset += (bOffset + 1);
            }
        }
        return source.toString();
    }

    private static String generateBlank(int num) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < num; i++) {
            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }
}
