package com.wipe.userservice.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wipe.userservice.pojo.domain.User;
import com.wipe.userservice.mapper.UsersMapper;
import com.wipe.userservice.service.UsersService;
import org.springframework.stereotype.Service;

/**
* @author 29770
* @description 针对表【users】的数据库操作Service实现
* @createDate 2025-06-16 16:03:54
*/
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, User>
implements UsersService{

}
