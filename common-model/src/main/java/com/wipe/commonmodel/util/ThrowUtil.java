package com.wipe.commonmodel.util;

import com.wipe.commonmodel.enums.EnumStatusCode;
import com.wipe.commonmodel.exception.ServiceException;

/**
 * @author wipe
 * @date 2025/6/17 下午7:30
 */
public class ThrowUtil {

    /**
     * 条件成立则抛出异常
     *
     * @param condition 条件
     * @param exception 异常
     */
    public static void throwIf(boolean condition, RuntimeException exception) {
        if (condition) {
            throw exception;
        }
    }

    /**
     * 条件成立则抛出异常
     *
     * @param condition 条件
     * @param errorCode 异常码
     */
    public static void throwIf(boolean condition, EnumStatusCode errorCode) {
        throwIf(condition, new ServiceException(errorCode));
    }

    /**
     * 条件成立则抛出异常
     *
     * @param condition 条件
     * @param errorCode 异常码
     * @param message   异常信息
     */
    public static void throwIf(boolean condition, EnumStatusCode errorCode, String message) {
        throwIf(condition, new ServiceException(errorCode, message));
    }
}
