package com.wipe.commonmodel.util;

import com.wipe.commonmodel.AxiosResult;
import com.wipe.commonmodel.enums.EnumStatusCode;
import com.wipe.commonmodel.exception.ServiceException;

/**
 * @author wipe
 * @date 2025/6/21 下午4:33
 */
public class AxiosResultCheck {

    private AxiosResultCheck() {
    }

    public static void check(AxiosResult<?> axiosResult) {
        if (!axiosResult.getCode().equals(EnumStatusCode.SUCCESS.getCode())) {
            throw new ServiceException(EnumStatusCode.ERROR_OPERATION, axiosResult.getMsg());
        }
    }
}
