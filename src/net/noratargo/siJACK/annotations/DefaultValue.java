package net.noratargo.siJACK.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

/**
 * Allows to specify the default value for a {@link Field} or a constructor's Parameter.
 * <p>
 * If this annotation is not specified, the following rules will be applied for determination of the default value of the not-annotated element:
 * <ol>
 * <li>If its type is a native data type (byte, short, char, int, long, float, double, boolean) then its default value can be determinated (since they always do have a default value, which default to 0 or 0.0 or <code>false</code>). If this does not apply:
 * <li>Use the {@link Field}'s default value or use <code>null</code> as default value.
 * </ol>
 * 
 * @author HMulthaupt
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface DefaultValue {
	String value();
	
	/**
	 * If set to true, null will be set as default value, regardless of {@link #value()}.
	 * 
	 * @return true, if the default value shall be <code>null</code>.
	 */
	boolean isNull() default false;
}
