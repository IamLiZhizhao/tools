package com.lzz.tools.helper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * 反射帮助类
 *
 * @author lizhizhao
 * @since 2021-02-27 09:55
 */
public class ReflectionHelper {

    /**
     * 根据字段实际类型返回
     * @param field
     * @param val
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     */
    private Object convertClass(Field field , Object val) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<?> typeCla = field.getType();
        String v = (String) val;

        if (!typeCla.isPrimitive()) { // 判断基本类型
            if(typeCla.equals(String.class)){ // 如果是string则直接返回
                return v ;
            }
            //  如果不为null 则通过反射实例一个对象返回
            return "".equals(v) ? null : typeCla.getConstructor(String.class).newInstance(v);
        }

        // 下面处理基本类型，返回包装类
        String name = typeCla.getName();
        switch (name){
            case "string": return v;
            case "int": return Integer.parseInt(v);
            case "byte": return Byte.parseByte(v);
            case "boolean": return Boolean.parseBoolean(v);
            case "double": return Double.parseDouble(v);
            case "float": return Float.parseFloat(v);
            case "long": return Long.parseLong(v);
            case "short": return Short.parseShort(v);
            default: return v;
        }
    }


}
