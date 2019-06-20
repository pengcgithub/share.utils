package com.platform.common.export;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.TYPE})
public @interface Excel {

	String name() default "";
	
	int width() default 20;
	
	boolean skip() default false;
}
