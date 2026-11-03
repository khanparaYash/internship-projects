package org.example.orderservice.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) //where we can apply our custom annotation.
@Retention(RetentionPolicy.RUNTIME) //keep this annotation available at runtime.
public @interface Auditable {
    String action(); //specify what action we want to audit.
}
