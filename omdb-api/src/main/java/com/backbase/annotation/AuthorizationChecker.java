package com.backbase.annotation;

import com.backbase.model.confg.ServiceAccessNames;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AuthorizationChecker {
    ServiceAccessNames serviceAccessName();
}
