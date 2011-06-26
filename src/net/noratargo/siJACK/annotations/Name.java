package net.noratargo.siJACK.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This annotation can be used to change the name, under that the field is available.
 * <p>
 * if you are using multiple prefixes (e.g. "foo" and "bar") as well as multiple parameter names (values) (e.g. "x" and
 * "y") then this parameter is available under all possible combinations (e.g. "foo.x", "foo.y", "bar.x" and "bar.y")
 * <p>
 * 
 * @author HMulthaupt
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Name {

	/**
	 * Define the Prefixes to use.
	 * <p>
	 * If none of the defined prefixes is marked a being the default one AND {@link #applyFieldPrefix()} is
	 * <code>true</code>, then a default Prefix might be found in the Field-Prefixes. If there is no prefix, either, no
	 * prefix will be choosen. (Although this does not effect the selection of the Parameter's name).
	 * 
	 * @return
	 */
	Prefix prefix() default @Prefix(addFullQualifiedClassNameToPrefixList = false, defaultValue = -1);

	/**
	 * The paramete's namer. If this value is an empty String <code>""</code> or does not contain any elements at all,
	 * then the field's name will be used instead.
	 * 
	 * @return
	 */
	String[] value() default {};

	/**
	 * Defines the value, that should be used as default.
	 */
	int defaultValue() default 0;
	
	boolean addFieldNameToValues() default true;

	/**
	 * If set to <code>true</code> the prefixes defined at class-level will be used in addition to the ones, specified
	 * here. If no prefixes have been specified here, only the prefixes, defined by the ClassPrefix Annotation will be
	 * used. If there is no ClassPrefix annotation, then the class name will be used as prefix.
	 * <p>
	 * The Field prefix is either the prefix, set via a {@link Prefix} annotation at a class, or via a {@link Prefix}
	 * annotation at a field. The field-leveled Prefix will override the class-prefix even if there is no prefix given.
	 * 
	 * @return true, if the prefixes, applying to
	 */
	boolean applyFieldPrefix() default true;
}
