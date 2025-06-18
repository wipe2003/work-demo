package com.wipe.commonmodel.model.dto.permission;

import com.wipe.commonmodel.model.dto.BasePageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author wipe
 * @date 2025/6/18 下午4:22
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserRolePageRequest extends BasePageRequest implements Serializable {

    private static final long serialVersionUID = 2297950425903062153L;

    private String permissionCode;
}
