package com.wipe.loggingservice.pojo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


import java.io.Serializable;

/**
 * @author 29770
 * @TableName operation_logs
 */
@TableName(value ="operation_logs")
@Data
public class OperationLogs implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long logId;

    /**
     * userId
     */
    private Long userId;

    /**
     * 操作
     */
    private String action;

    /**
     * ip
     */
    private String ip;

    /**
     * 明细
     */
    private String detail;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}