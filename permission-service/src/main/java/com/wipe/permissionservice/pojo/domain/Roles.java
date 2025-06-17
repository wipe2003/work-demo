package com.wipe.permissionservice.pojo.domain;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 29770
 * @TableName roles
 */
@Data
@TableName(value ="roles")
@NoArgsConstructor
public class Roles implements Serializable {
    /**
     * 角色 id
     */
    @TableId
    private Integer roleId;

    /**
     * 角色码
     */
    private String roleCode;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


}