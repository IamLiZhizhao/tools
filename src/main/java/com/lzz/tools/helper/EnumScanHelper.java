package com.lzz.tools.helper;

import com.lzz.tools.dto.SelectOptionResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 枚举扫描帮助类
 */
@Slf4j
public class EnumScanHelper {
    private static Object INIT_LOCK = new Object();
    private static ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    private static MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resolver);
    private static Map<String, List<SelectOptionResult>> enumOptionResultMap = new ConcurrentHashMap<>();

    public List<SelectOptionResult> list(String enumClass) throws Exception {
        // 初始化枚举映射表
        initEnumOptionResultMap();

        // 特殊格式处理
        String keyField = "code";
        String valueField = "description";
        if (enumClass.contains("#")) {
            String[] params = enumClass.split("#");
            if (params.length != 3) {
                return enumOptionResultMap.get(params[0]);
            }
            enumClass = params[0];
            keyField = params[1];
            valueField = params[2];
        }
        List<SelectOptionResult> enumList = enumOptionResultMap.get(enumClass);

        if (ListHelper.isNullOrEmpty(enumList)) {
            Object[] enumObjects = Class.forName(enumClass).getEnumConstants();
            enumList = new ArrayList<>(enumObjects.length);
            for (Object obj : enumObjects) {
                SelectOptionResult optionResult = new SelectOptionResult();
                optionResult.setCode(EnumHelper.getValue(obj, keyField));
                optionResult.setLabel(EnumHelper.getValue(obj, valueField));
                enumList.add(optionResult);
            }
            enumOptionResultMap.put(enumClass, enumList);
        }

        return enumList;
    }

    public Map<String, List<SelectOptionResult>> listBatch(List<String> enumClassList) throws Exception {
        Map<String, List<SelectOptionResult>> resultMap = new HashMap<>(enumClassList.size());
        for (String enumClass : enumClassList) {
            resultMap.put(enumClass, list(enumClass));
        }
        return resultMap;
    }

    /**
     * 扫码项目中指定路径枚举，初始化枚举映射表
     */
    private void initEnumOptionResultMap() {
        if (enumOptionResultMap.isEmpty()) {
            synchronized (INIT_LOCK) {
                if (enumOptionResultMap.isEmpty()) {
                    // 应用中的枚举路径
                    String commonEnumPath = "com.lzz.tools.enums";
                    // 扫描枚举
                    doEnumScan(commonEnumPath);
                }
            }
        }
    }

    /**
     * 执行指定路径下的枚举扫描
     * 例如：om.lzz.tools.enums
     * @param location 路径
     */
    private void doEnumScan(String location) {
        try {
            String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
                    + ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(location))
                    + "/**/*.class";

            Resource[] resources = resolver.getResources(packageSearchPath);
            for (Resource resource : resources) {
                if (resource.isReadable()) {
                    MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);

                    //得到该类下面的所有方法
                    Class classes = Class.forName(metadataReader.getClassMetadata().getClassName());
                    if (classes.isEnum()) {
                        Field[] fields = classes.getDeclaredFields();

                        String keyField = null;
                        String valueField = null;
                        for (Field field : fields) {
                            String name = field.getName();

                            // 当枚举里同时存在“code”和“value”时，keyField以“code”优先
                            if ("code".equals(name) || "value".equals(name)) {
                                if (keyField != null && "code".equals(keyField)) {
                                    continue;
                                }

                                keyField = name;
                            }

                            if ("description".equals(name) || "desc".equals(name)) {
                                valueField = name;
                            }
                        }

                        if (keyField != null && valueField != null) {
                            Object[] enumObjects = classes.getEnumConstants();
                            List<SelectOptionResult> enumList = new ArrayList<>(enumObjects.length);
                            for (Object obj : enumObjects) {
                                SelectOptionResult optionResult = new SelectOptionResult();
                                optionResult.setCode(EnumHelper.getValue(obj, keyField));
                                optionResult.setLabel(EnumHelper.getValue(obj, valueField));
                                enumList.add(optionResult);
                            }

                            enumOptionResultMap.put(classes.getName(), enumList);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("Failure during scanning enums", e);
            throw new RuntimeException("Failure during scanning enums");
        }
    }
}
