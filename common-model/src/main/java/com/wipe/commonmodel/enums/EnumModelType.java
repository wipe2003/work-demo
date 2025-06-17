package com.wipe.commonmodel.enums;


import com.wipe.commonmodel.exception.ServiceException;
import lombok.Getter;

/**
 * @author wipe
 * @date 2025/6/9 下午8:04
 */
@Getter
public enum EnumModelType {

    LOVE(0, "conversation_love"),
    COMMON(1, "conversation_common");

    private final Integer type;
    private final String tableName;

    EnumModelType(Integer type, String tableName) {
        this.type = type;
        this.tableName = tableName;
    }

    public static EnumModelType formType(Integer type) {
        for (EnumModelType value : values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }
        throw new ServiceException(EnumStatusCode.ERROR_PARAMS, "类型参数错误");
    }

}
