package com.wipe;

import cn.hutool.json.JSONUtil;
import com.wipe.userservice.pojo.dto.UserUpdateRequest;
import org.junit.jupiter.api.Test;

/**
 * @author wipe
 * @date 2025/6/16 下午6:10
 */
public class JwtTest {

    @Test
    void name() {
        UserUpdateRequest request = new UserUpdateRequest();
        request.setUserId(1L);
        request.setPhone("123465");
        request.setEmail("165161@163.com");
        System.out.println(JSONUtil.toJsonStr(request));
    }
}
