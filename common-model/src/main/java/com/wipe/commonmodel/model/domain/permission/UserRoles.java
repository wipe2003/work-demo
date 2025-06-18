package com.wipe.commonmodel.model.domain.permission;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


import java.io.Serializable;

/**
 * @author 29770
 * @TableName user_roles
 */
@Data
@TableName(value ="user_roles")
public class UserRoles implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户 id
     */
    private Long userId;

    /**
     * 角色 id
     */
    private Long roleId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


}