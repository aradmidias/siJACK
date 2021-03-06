package net.noratargo.siJACK.annotations;

import net.noratargo.siJACK.annotationHelper.NameAnnotationHelper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation allows to give additional names to a field or Constructor or a construcotr's parameter.
 * 
 * @author HMulthaupt
 * @see NameAnnotationHelper
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
public @interface Name {
	String[] value();
	
	boolean addFieldNameIfPossible() default true;
	
	int defaultName() default 0;
}
