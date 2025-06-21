package com.wipe.userservice.service;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wipe.commonmodel.model.dto.BasePageRequest;
import com.wipe.userservice.pojo.domain.User;
import com.wipe.userservice.pojo.dto.UserLoginRequest;
import com.wipe.userservice.pojo.dto.UserQueryRequest;
import com.wipe.userservice.pojo.dto.UserRegisterRequest;
import com.wipe.userservice.pojo.dto.UserResetPasswordRequest;
import com.wipe.userservice.pojo.vo.UserVo;


/**
* @author 29770
* @description 针对表【users】的数据库操作Service
* @createDate 2025-06-16 16:03:54
*/
public interface UsersService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userRegisterRequest req
     * @return id
     */
    Long userRegister(UserRegisterRequest userRegisterRequest);

    /**
     * 用户登录
     *
     * @param loginRequest login Request
     * @return jwt
     */
    String userLogin(UserLoginRequest loginRequest);


    /**
     * 获取用户列表
     * @param roleCode 根据用户权限码区分
     * @param pageRequest pageRequest
     * @return page
     */
    Page<UserVo> listUser(String roleCode, BasePageRequest pageRequest);

    /**
     * 重置密码（用户调用）
     */
    void resetPassword(UserResetPasswordRequest userResetPasswordRequest);

    /**
     * 重置密码（超管和管理员调用）
     */
    void resetPasswordDirectly(UserResetPasswordRequest userResetPasswordRequest);

    /**
     * 获取当前用户权限码
     */
    String getCurrentRoleCode();

    /**
     * 根据 Id 修改用户信息
     */
    void updateByUserId(User user);

    /**
     * 获取查询条件工具方法
     *
     * @param userQueryRequest req
     * @return wrapper
     */
    LambdaQueryChainWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);


}
