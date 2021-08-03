package me.devtools4.aops.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Trace {

  String TRACE_ANNOTATION = "@annotation(me.devtools4.aops.annotations.Trace)";

  String level() default "TRACE";
}