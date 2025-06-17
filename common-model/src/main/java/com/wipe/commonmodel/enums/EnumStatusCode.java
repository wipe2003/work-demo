package com.wipe.commonmodel.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wipe
 * @date 2025/6/6 下午6:36
 */
@Getter
@AllArgsConstructor
public enum EnumStatusCode {

    SUCCESS(0, "success"),
    ERROR_PARAMS(40000, "请求参数错误"),
    ERROR_NOT_LOGIN(40100, "请先登陆"),
    ERROR_NO_AUTH(40101, "无此权限"),
    ERROR_NOT_FOUND(40400, "请求数据不存在"),
    ERROR_SYSTEM(50000, "系统异常"),
    ERROR_OPERATION(50001, "操作失败");

    private final int code;
    private final String msg;
}
