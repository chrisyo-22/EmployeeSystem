package com.chrisyo.aop;
import java.lang.annotation.*;

/**
 * Custom Log Annotation
 */
@Target({ElementType.METHOD})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {
}