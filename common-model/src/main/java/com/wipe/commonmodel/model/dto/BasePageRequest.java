package com.wipe.commonmodel.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author wipe
 * @date 2025/6/18 下午4:21
 * 基础分页类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasePageRequest implements Serializable {

    private static final long serialVersionUID = 3388390799692811277L;

    private Integer current;
    private Integer size;

}
