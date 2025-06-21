package com.wipe.commonmodel;


import com.wipe.commonmodel.enums.EnumStatusCode;
import com.wipe.commonmodel.exception.ServiceException;
import lombok.Data;

/**
 * @author wipe
 * @date 2025/6/6 下午6:34
 */
@Data
public class AxiosResult<T> {

    private Integer code;
    private String msg;
    private T data;

    private AxiosResult() {
    }

    /**
     * 构造一个成功响应结果。
     *
     * @param msg  响应消息
     * @param data 响应数据
     * @param <T>  数据类型
     * @return 包含成功状态码、消息和数据的成功响应对象
     */
    public static <T> AxiosResult<T> success(String msg, T data) {
        AxiosResult<T> result = new AxiosResult<>();
        result.setMsg(msg);
        result.setCode(EnumStatusCode.SUCCESS.getCode());
        result.setData(data);
        return result;
    }

    /**
     * 构造一个成功响应结果，使用默认的成功消息。
     *
     * @param data 响应数据
     * @param <T>  数据类型
     * @return 包含成功状态码和数据的成功响应对象
     */
    public static <T> AxiosResult<T> success(T data) {
        return success(EnumStatusCode.SUCCESS.getMsg(), data);
    }

    public static <T> AxiosResult<T> success() {
        return success(null);
    }

    /**
     * 构造一个错误的响应结果。
     *
     * @param code 错误状态码
     * @param msg  错误消息
     * @return 包含错误状态码和消息的错误响应对象
     */
    public static AxiosResult<Object> error(Integer code, String msg) {
        AxiosResult<Object> result = new AxiosResult<>();
        result.setMsg(msg);
        result.setCode(code);
        return result;
    }

    /**
     * 构造一个错误的响应结果，使用枚举中的状态码和消息。
     *
     * @param statusCode 错误状态码枚举
     * @return 包含错误状态码和消息的错误响应对象
     */
    public static AxiosResult<Object> error(EnumStatusCode statusCode) {
        return error(statusCode.getCode(), statusCode.getMsg());
    }

    /**
     * 构造一个错误的响应结果，使用枚举中的状态码和自定义消息。
     *
     * @param statusCode 错误状态码枚举
     * @param msg        自定义错误消息
     * @return 包含错误状态码和消息的错误响应对象
     */
    public static AxiosResult<Object> error(EnumStatusCode statusCode, String msg) {
        return error(statusCode.getCode(), msg);
    }

    /**
     * 创建一个表示操作错误的AxiosResult对象
     * 此方法用于生成一个统一的错误响应，当操作失败但没有具体错误信息时使用
     *
     * @return AxiosResult<Object> 表示操作错误的AxiosResult对象，包含错误状态码和默认错误信息
     */
    public static AxiosResult<Object> error() {
        return error(EnumStatusCode.ERROR_OPERATION);
    }

    public static void check(AxiosResult<?> result) {
        if (!result.getCode().equals(EnumStatusCode.SUCCESS.getCode())) {
            throw new ServiceException(EnumStatusCode.ERROR_OPERATION, result.getMsg());
        }
    }

}