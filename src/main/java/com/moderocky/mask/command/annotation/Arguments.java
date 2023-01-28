package com.moderocky.mask.command.annotation;

import com.moderocky.mask.command.Argument;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

@Target({METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Arguments {

    String[] literal() default {};

    Class<? extends Argument<?>>[] value() default {};

}
