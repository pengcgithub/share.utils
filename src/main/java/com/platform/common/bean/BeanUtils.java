package com.platform.common.bean;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

/**
 * bean对象工具类<br/>
 *
 * @author pengc
 * @date 2019/4/24 19:03
 */
public class BeanUtils {

    /**
     * 深拷贝属性对象<br/>
     *
     * @param orig  源对象
     * @param dest  目标对象
     */
    public static void copyProperties(final Object orig, final Object dest) {
        try {
            org.apache.commons.beanutils.BeanUtils.copyProperties(dest, orig);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 拷贝list元素对象，将origs中的元素信息，拷贝覆盖至dests中<br/>
     *
     * @param origs 源list对象
     * @param dests 目标list对象
     * @param origsElementTpe 源list元素类型对象
     * @param destElementTpe 目标list元素类型对象
     * @param <T1> 源list元素类型
     * @param <T2> 目标list元素类型
     */
    public static <T1,T2> void copyProperties(final List<T1> origs, final List<T2> dests, Class<T1> origsElementTpe, Class<T2> destElementTpe){
        if (origs == null || dests == null) {
            return ;
        }

        if (dests.size() != 0) {
            //防止目标对象被覆盖，要求必须长度为零
            throw new RuntimeException("目标对象存在值");
        }

        try {
            for (T1 orig: origs) {
                T2 t = destElementTpe.newInstance();
                dests.add(t);
                copyProperties(orig,t);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 该方法是用于相同对象不同属性值的合并，如果两个相同对象中同一属性都有值，那么sourceBean中的值会不会覆盖targetBean中的值<br/>
     *
     * @param sourceBean    被提取的对象bean
     * @param targetBean    用于合并的对象bean
     */
    public static <T> void mergeBeanNotCover(T sourceBean, T targetBean) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(targetBean.getClass());
            // Iterate over all the attributes
            for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {

                // Only copy writable attributes
                if (descriptor.getWriteMethod() != null) {
                    Object originalValue = descriptor.getReadMethod()
                            .invoke(targetBean);

                    // Only copy values values where the destination values is null
                    if (originalValue == null) {
                        Object defaultValue = descriptor.getReadMethod().invoke(
                                sourceBean);
                        descriptor.getWriteMethod().invoke(targetBean, defaultValue);
                    }

                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 该方法是用于相同对象不同属性值的合并，如果两个相同对象中同一属性都有值，那么sourceBean中的值会覆盖targetBean中的值<br/>
     *
     * @param sourceBean    被提取的对象bean
     * @param targetBean    用于合并的对象bean
     */
    public static <T> void mergeBeanCover(T sourceBean, T targetBean) {
        Class sourceBeanClass = sourceBean.getClass();
        Class targetBeanClass = targetBean.getClass();

        Field[] sourceFields = sourceBeanClass.getDeclaredFields();
        Field[] targetFields = targetBeanClass.getDeclaredFields();
        for (int i = 0; i < sourceFields.length; i++) {
            Field sourceField = sourceFields[i];
            if (Modifier.isStatic(sourceField.getModifiers())) {
                continue;
            }

            Field targetField = targetFields[i];
            if (Modifier.isStatic(targetField.getModifiers())) {
                continue;
            }

            sourceField.setAccessible(true);
            targetField.setAccessible(true);

            try {
                if (!(sourceField.get(sourceBean) == null) && !"serialVersionUID".equals(sourceField.getName())) {
                    targetField.set(targetBean, sourceField.get(sourceBean));
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

}
