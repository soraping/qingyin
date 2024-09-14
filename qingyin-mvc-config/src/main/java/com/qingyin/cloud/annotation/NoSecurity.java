package com.qingyin.cloud.annotation;

import java.lang.annotation.*;

/**
 * <h1>无需校验</h1>
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoSecurity {
}
