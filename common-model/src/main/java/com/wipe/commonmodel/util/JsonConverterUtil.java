package com.wipe.commonmodel.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;

import java.nio.charset.StandardCharsets;

/**
 * @author wipe
 * @date 2025/6/18 下午12:30
 */
public class JsonConverterUtil {

    private JsonConverterUtil() {
    }

    public static <T> T convert(byte[] jsonBytes, Class<T> clazz) {
        String json = new String(jsonBytes, StandardCharsets.UTF_8);
        if (clazz == String.class) {
            return clazz.cast(json);
        }
        return BeanUtil.toBean(json, clazz);
    }

    public static <T> T convert(byte[] jsonBytes, TypeReference<T> typeReference) {
        String json = new String(jsonBytes, StandardCharsets.UTF_8);
        return JSONUtil.toBean(json, typeReference, true);
    }
}
