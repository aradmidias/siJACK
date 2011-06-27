package net.noratargo.siJACK.annotations;

import net.noratargo.siJACK.annotationHelper.PrefixAnnotationHelper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation can be used, to specifiy a list of prefixes.
 * <p>
 * By default (if no annotation is given) the classes fully qualified class name will be used as prefix and as default
 * prefix.
 * 
 * @author HMulthaupt
 * @see PrefixAnnotationHelper
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER })
public @interface Prefix {
	/**
	 * A List of prefixes.
	 * 
	 * @return
	 */
	String[] value() default "";

	/**
	 * If <code>true</code>, then the classes full qualified name will be added to the parameter list.
	 * 
	 * @return
	 */
	boolean addClassPathToPrefixes() default true;

	/**
	 * SPecifies, which of the given prefixes should be used. -1 specifies, that no prefix will be used. If the classes
	 * fully qualified name should be used as default prefix, remember, that it will be added to the end of the list of
	 * the prefixes.
	 * 
	 * @return
	 */
	int defaultValue() default 0;
}
