package com.lzz.tools.helper;

import com.lzz.tools.dto.SelectOptionResult;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 枚举帮助类
 *
 * @author lizhizhao
 * @since 2020-05-17 14:32
 */
public class EnumHelper {

    private static Map<String, Map<Object, Object>> cacheMap;

    /**
     * 根据value获取枚举
     *
     * @param clazz
     * @param value
     * @return value对应的枚举
     */
    public static <T extends Enum> T getByValue(Class<T> clazz, Object value) {
        return (T) getMap(clazz, "value").get(value);
    }

    /**
     * 根据code获取枚举
     *
     * @param clazz
     * @param code
     * @return code对应的枚举
     */
    public static <T extends Enum> T getByCode(Class<T> clazz, Object code) {
        return (T) getMap(clazz, "code").get(code);
    }

    /**
     * 根据指定field的值获取枚举
     *
     * @param clazz
     * @param fieldName
     * @param object
     * @return T
     */
    public static <T extends Enum> T getByFieldValue(Class<T> clazz, String fieldName, Object object) {
        return (T) getMap(clazz, fieldName).get(object);
    }

    /**
     * 功能描述:枚举转入map
     *
     * @param:t：枚举，keyField：枚举中哪个字段作为map的key，valueField：枚举中哪个字段作为map的value，放入的map
     */
    public static <T extends Enum> Map enumToMap(T t, String keyField, String valueField, Map<Object, Object> returnMap) {
        try {
            returnMap.put(getValue(t, keyField), getValue(t, valueField));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnMap;
    }

    /**
     * 功能描述:枚举数组转map，
     *
     * @param:t：枚举数组，keyField：枚举中哪个字段作为map的key，valueField：枚举中哪个字段作为map的value
     * 例：enumsToMap(EnableEnum.values(), "code", "description");
     * @return:
     */
    public static <T extends Enum> Map enumArrayToMap(T[] t, String keyField, String valueField) {
        Map<Object, Object> returnMap = new LinkedHashMap<>();
        for (int i = 0; i < t.length; i++) {
            enumToMap(t[i], keyField, valueField, returnMap);
        }
        return returnMap;
    }

    /**
     * 获取枚举描述(根据code)
     *
     * @param clazz
     * @param object
     * @return java.lang.String
     */
    public static <T extends Enum> String getDescription(Class<T> clazz, Object object) {
        if (object == null) {
            return "";
        }

        T ee = EnumHelper.getByCode(clazz, object);
        if (ee != null) {
            Map map = JSONObject.parseObject(JSONObject.toJSONString(ee), Map.class);
            if (map.containsKey("description")) {
                return map.get("description").toString();
            }
        }
        return "";
    }

    /**
     * 功能描述:枚举数组转成与前端约定好的实体
     *
     * @param clazz
     * @param codeField
     * @param valueField
     */
    public static <T extends Enum> List<SelectOptionResult> toSelectOptionResult(Class<T> clazz, String codeField, String valueField) {
        List<SelectOptionResult> selectOptionResults = new ArrayList<>();

        for (Map.Entry<Object, Object> entry : getMap(clazz, codeField).entrySet()) {
            SelectOptionResult selectOptionResult = new SelectOptionResult();
            selectOptionResult.setCode(entry.getKey());

            for (Map.Entry<Object, Object> entryValue : getMap(clazz, valueField).entrySet()) {
                if (entryValue.getValue().equals(entry.getValue())) {
                    selectOptionResult.setLabel(entryValue.getKey());
                    break;
                }
            }

            selectOptionResults.add(selectOptionResult);
        }

        return selectOptionResults;
    }

    /**
     * 枚举转前端下拉框选项（默认取：code，description）
     */
    public static <T extends Enum> List<SelectOptionResult> toSelectOptionResult(Class<T> clazz) {
        return toSelectOptionResult(clazz, "code", "description");
    }

    //region 私有方法

    /**
     * 从静态map中取对应关系
     *
     * @param clazz
     * @param fieldName
     */
    private static <T extends Enum> Map<Object, Object> getMap(Class<T> clazz, String fieldName) {
        if (cacheMap == null) {
            cacheMap = new LinkedHashMap<>();
        }

        Map<Object, Object> parameters = cacheMap.get(clazz.getName() + fieldName);

        if (parameters != null) {
            return parameters;
        } else {
            parameters = new LinkedHashMap<>();
        }

        for (T e : clazz.getEnumConstants()) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                parameters.put(field.get(e), e);
            } catch (NoSuchFieldException e1) {
                e1.printStackTrace();
                return new LinkedHashMap<>();
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
                return new LinkedHashMap<>();
            }
        }

        cacheMap.put(clazz.getName() + fieldName, parameters);

        return parameters;
    }

    /**
     * 根据字段名获取字段值
     *
     * @param ob
     * @param name
     * @return
     * @throws Exception
     */
    public static Object getValue(Object ob, String name) throws Exception {
        Method[] m = ob.getClass().getMethods();
        for (int i = 0; i < m.length; i++) {
            if (("get" + name).toLowerCase().equals(m[i].getName().toLowerCase())) {
                return m[i].invoke(ob);
            }
        }
        return null;
    }

    //endregion
}
