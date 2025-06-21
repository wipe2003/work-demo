package com.wipe;

import cn.hutool.json.JSONUtil;
import com.wipe.userservice.pojo.dto.UserRegisterRequest;
import org.junit.jupiter.api.Test;

/**
 * @author wipe
 * @date 2025/6/16 下午6:10
 */
public class JwtTest {

    @Test
    void name() {
        UserRegisterRequest request = new UserRegisterRequest();
        request.setUsername("wipe");
        request.setPassword("123456");
        request.setConfirmPassword("123456");
        System.out.println(JSONUtil.toJsonStr(request));
    }
}
