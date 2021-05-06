package com.group3.aspects;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.aspectj.lang.annotation.Aspect;

/* An annotation that specifies that requested functionality should be restricted to Moderators */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Aspect
public @interface Moderator {
	
	@Before(/*hook goes here*/)
	public
	
	@Pointcut("execution( *")
	
}
