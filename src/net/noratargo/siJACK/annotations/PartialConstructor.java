package net.noratargo.siJACK.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a construcotr as partial. This is necessary to know, since only in this case one or more parameters are not required to have a {@link DefaultValue} annotation.
 * 
 * @author HMulthaupt
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.CONSTRUCTOR)
public @interface PartialConstructor {
	
}
