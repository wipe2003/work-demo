package com.wipe.userservice.pojo.vo;

import cn.hutool.core.bean.BeanUtil;
import com.wipe.userservice.pojo.domain.User;
import lombok.Data;

/**
 * @author wipe
 * @date 2025/6/18 下午2:39
 */
@Data
public class UserVo {

    private Long userId;

    private String username;

    public static UserVo toUserVo(User user) {
        UserVo userVo = new UserVo();
        BeanUtil.copyProperties(user, userVo);
        return userVo;
    }

    public static User toUser(UserVo userVo) {
        User user = new User();
        BeanUtil.copyProperties(userVo, user);
        return user;
    }
}
