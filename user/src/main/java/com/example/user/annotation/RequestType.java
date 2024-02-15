package com.example.user.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.example.user.annotation.RequestTypeEnum.GET;

@Retention(RetentionPolicy.RUNTIME)
public @interface RequestType {
    RequestTypeEnum value() default GET;
}
