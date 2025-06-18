package com.wipe.commonmodel.exception;


import com.wipe.commonmodel.AxiosResult;
import com.wipe.commonmodel.enums.EnumStatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author wipe
 * @date 2025/6/6 下午6:59
 */
@Slf4j
@ApiIgnore
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理Service异常的处理器方法
     *
     * @param e ServiceException实例，包含异常代码和消息
     * @return 返回一个AxiosResult对象，包含错误代码和消息
     */
    @ExceptionHandler(ServiceException.class)
    public AxiosResult<Object> handleServiceException(ServiceException e) {
        // 记录异常信息，以便于调试和日志分析
        log.error("ServiceException:{}", e.getMessage());
        // 返回一个错误的AxiosResult对象，包含异常的代码和消息
        return AxiosResult.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public AxiosResult<?> paramsExceptionHandler(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        if (fieldError == null) {
            return AxiosResult.error(EnumStatusCode.ERROR_PARAMS);
        }
        String defaultMessage = fieldError.getDefaultMessage();
        log.error("参数错误：{}", defaultMessage);
        return AxiosResult.error(EnumStatusCode.ERROR_PARAMS, defaultMessage);
    }

    @ExceptionHandler(Exception.class)
    public AxiosResult<Object> handleRuntimeException(Exception e) {
        log.error("Exception", e);
        return AxiosResult.error(EnumStatusCode.ERROR_SYSTEM);
    }

}
