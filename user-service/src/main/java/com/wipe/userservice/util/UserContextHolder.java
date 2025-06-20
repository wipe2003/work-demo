package com.wipe.userservice.util;

import com.wipe.commonmodel.enums.EnumStatusCode;
import com.wipe.commonmodel.exception.ServiceException;
import com.wipe.userservice.pojo.vo.UserVo;

/**
 * @author wipe
 * @date 2025/6/18 下午2:50
 */
public class UserContextHolder {

    private UserContextHolder() {
    }

    private static final ThreadLocal<UserVo> USER_HOLDER = new ThreadLocal<>();

    public static void set(UserVo userVo) {
        USER_HOLDER.set(userVo);
    }

    public static UserVo get() {
        UserVo userVo = USER_HOLDER.get();
        if (userVo == null) {
            throw new ServiceException(EnumStatusCode.ERROR_NOT_LOGIN);
        }
        return userVo;
    }

    public static void remove() {
        USER_HOLDER.remove();
    }
}
