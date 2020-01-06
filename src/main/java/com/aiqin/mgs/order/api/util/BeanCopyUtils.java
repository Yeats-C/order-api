package com.aiqin.mgs.order.api.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.util.CollectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author 刘
 * @Date
 * @Description Bean对象拷贝 使用Cgilb 性能最好
 * @Version 1.0
 */
@Slf4j
public class BeanCopyUtils {

    /**
     * 对象拷贝
     *
     * @param source 对象源
     * @param targer 目标对象
     */
    public static void copy(Object source, Object targer) {
        BeanCopier beanCopier = BeanCopier.create(source.getClass(), targer.getClass(), false);
        beanCopier.copy(source, targer, null);
    }

    /**
     * 对象拷贝忽略控制
     *
     * @param source 对象源
     * @param targer 目标对象
     */
    public static void copyIgnoreNullValue(Object source, Object targer) {
        if (source != null && targer != null) {
            BeanWrapper beanWrapper = new BeanWrapperImpl(source);
            PropertyDescriptor[] propertyDescriptors = beanWrapper.getPropertyDescriptors();
            Set<String> nullNames = new HashSet<>();
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                Object value = beanWrapper.getPropertyValue(propertyDescriptor.getName());
                if (value == null) {
                    nullNames.add(propertyDescriptor.getName());
                }
            }
            BeanUtils.copyProperties(source, targer, nullNames.toArray(new String[]{}));
        }
    }

    /**
     * 对象拷贝并创建target对象
     *
     * @param source 对象源
     * @param targer 目标对象
     */
    public static <T> T copy(Object source, Class<T> targer){
        try {
            if (source != null) {
                T instance = targer.newInstance();
                copy(source, instance);
                return instance;
            }
            return null;
        } catch (Exception e){
            throw new IllegalArgumentException();
        }
    }

    /**
     * 集合对象拷贝并创建target对象
     *
     * @param sources 对象源
     * @param targer  目标对象
     */
    public static <T> List<T> copyList(List sources, Class<T> targer){
        List<T> list = new ArrayList<T>();
        if (!CollectionUtils.isEmpty(sources)) {
            for (Object source : sources) {
                T t = copy(source, targer);
                list.add(t);
            }
        }
        return list;
    }

    public static <T> T copyValueWithoutNull(T source, T target){
        assert Objects.nonNull(source)||Objects.nonNull(target);
        Class<?> aClass = target.getClass();
        Field[] declaredFields = aClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            try {
                Field sourceField = source.getClass().getDeclaredField(declaredField.getName());
                Field targetField = target.getClass().getDeclaredField(declaredField.getName());
                sourceField.setAccessible(true);
                targetField.setAccessible(true);
                Object o = sourceField.get(source);
                if(Objects.nonNull(o)){
                    targetField.set(target,o);
                }
            } catch (Exception e) {
                log.error("设置非空转换错误",e);
            }
        }
        return target;
    }
}

