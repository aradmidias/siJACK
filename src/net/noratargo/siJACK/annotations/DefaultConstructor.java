package net.noratargo.siJACK.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a classes' constructor as the default one. there may currently only be one default constructor.
 * <p>
 * Currently only one PartialConstrucotr per class is supported.
 * 
 * @author HMulthau
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.CONSTRUCTOR)
public @interface DefaultConstructor {
}
