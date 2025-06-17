package com.wipe.commonmodel.exception;


import com.wipe.commonmodel.enums.EnumStatusCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wipe
 * @date 2025/6/6 下午6:50
 */
@Getter
@AllArgsConstructor
public class ServiceException extends RuntimeException {

    private final int code;

    public ServiceException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public ServiceException(EnumStatusCode statusCode) {
        this(statusCode.getCode(), statusCode.getMsg());
    }

    public ServiceException(EnumStatusCode statusCode, String msg) {
        this(statusCode.getCode(), msg);
    }

}
