package com.wipe;

import cn.hutool.core.bean.BeanUtil;
import com.wipe.userservice.pojo.domain.User;
import com.wipe.userservice.pojo.dto.UserUpdateRequest;
import org.junit.jupiter.api.Test;

/**
 * @author wipe
 * @date 2025/6/16 下午6:10
 */
public class JwtTest {

    @Test
    void name() {
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
        userUpdateRequest.setUserId(123L);
        userUpdateRequest.setEmail("");
        userUpdateRequest.setPhone("123456");
        User user = new User();
        BeanUtil.copyProperties(userUpdateRequest,user);
        System.out.println(user);
    }
}
