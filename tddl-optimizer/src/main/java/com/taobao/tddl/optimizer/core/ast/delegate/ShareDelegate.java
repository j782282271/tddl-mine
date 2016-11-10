package com.taobao.tddl.optimizer.core.ast.delegate;

import java.lang.annotation.*;

/**
 * 定义需要被share模式代理的方法
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface ShareDelegate {

}
