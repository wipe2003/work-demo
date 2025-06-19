package com.wipe.permissionservice.aop.annotation;

import com.wipe.commonmodel.enums.EnumRole;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wipe
 * @date 2025/6/19 上午12:50
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PermissionCheck {

    EnumRole value();
}
