package com.bishe.utils;


import java.lang.reflect.Field;

/**
 * Created by ZYL on 2019/2/16.
 * @Description: 该方法是用于相同对象不同属性值的合并，如果两个相同对象中同一属性都有值，
 *               那么sourceBean中的值会覆盖tagetBean重点的值
 */
public class MyBeanUtil {
    public static Object combineSydwCore(Object sourceBean, Object targetBean) {
        Class sourceBeanClass = sourceBean.getClass();
        Class targetBeanClass = targetBean.getClass();

        Field[] sourceFields = sourceBeanClass.getDeclaredFields();
        Field[] targetFields = sourceBeanClass.getDeclaredFields();
        for (int i = 0; i < sourceFields.length; i++) {
            Field sourceField = sourceFields[i];
            Field targetField = targetFields[i];
            sourceField.setAccessible(true);
            targetField.setAccessible(true);
            try {
                if (!(sourceField.get(sourceBean) == null)) {
                    targetField.set(targetBean, sourceField.get(sourceBean));
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return targetBean;
    }

}


