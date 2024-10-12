package com.seatrend.android.core.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ClassName ObjectUtils
 * @Author shuaijialin
 * @Date 2023/7/21
 * @Description
 */
public class ObjectUtils {

    /**
     * 将对象转化为Map集合
     *
     * @param obj
     * @return
     * @throws IllegalAccessException
     */
    public static Map<String, String> getObjectToMap(Object obj) {
        try {
            Map<String, String> map = new LinkedHashMap<>();
            Class<?> clazz = obj.getClass();
            System.out.println(clazz);
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                String fieldName = field.getName();
                String value = (String) field.get(obj);
                if (value == null) {
                    value = "";
                }
                map.put(fieldName, value);
            }
            return map;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    /**
     * 判断该对象是否所有属性为空
     * 返回ture表示所有属性为null，返回false表示不是所有属性都是null
     */
    public static boolean isAllFieldNull(Object object) {

        if (object == null) {
            return true;
        }

        boolean flag = true;

        Class clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            //设置属性是可以访问的(私有的也可以)
            field.setAccessible(true);
            Object value = null;
            try {
                value = field.get(object);
                // 只要有1个属性不为空,那么就不是所有的属性值都为空
                if (value != null) {
                    flag = false;
                    break;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return flag;
    }
}
