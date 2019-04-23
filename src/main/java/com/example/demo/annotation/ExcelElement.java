package com.example.demo.annotation;

import java.lang.annotation.*;

/**
 * 用于集合类与对象
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ExcelElement {

    /**
     *
     * 默认属性
     * @return  String 返回类型
     * @throws
     */
    String value() default "";
}