package net.noratargo.siJACK.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value={ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.LOCAL_VARIABLE, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE})
//@Target(ElementType.FIELD)
public @interface ParameterDescription {

	/**
	 * Defines the comment being displayed, when
	 * 
	 * @return
	 */
	String value() default "";

	String description() default "no description available";

	/**
	 * Use this, to specify this parameter's default value, if setting the parameter to a default value does not work.
	 * 
	 * @return A textual representation of this parameter's default value, which can be parsed into the actual class.
	 */
	String defaultValue() default "";
	
	boolean isDefaultValueNull() default false;
	
	boolean doNotComplainAboutDefaultParameter() default false;

	ParameterName[] name() default @ParameterName();

	/**
	 * Defines the ParameterName element, that should be used as default. If -1 is given, then there will be no default
	 * value for this element (Neither a prefix, nor a name).
	 * 
	 * @return
	 */
	int defaultParameterNameIndex() default 0;
}
