package net.noratargo.siJACK.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies the prefixes to use for all fields of the annotated class or for the annotated field.
 * <p>
 * Multiple prefixes may be given. If this is the case, the usage will only give out the first one. This feature is
 * supposed to be used, if a refactoring occured (so the arrangement of parameters changed) and older configurations
 * shall still be supported.
 * <p>
 * <p>
 * if neither
 * <p>
 * <p>
 * The default values make the full-qualified-classname to the default prefix. See: {@link #defaultValue()}.
 * 
 * @author HMulthaupt
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.CONSTRUCTOR })
public @interface Prefix {

	/**
	 * The Prefix to prepend to all properties of the current class.
	 * <p>
	 * The first prefix will be treated as the up-to-date, default prefix (this is intresting for printing out help
	 * about parameter-information, since only the default-prefix will be printed out). If this annotation annotates a
	 * class and one of that classes field's has its own Prefix-parameter, then the field's-default Prefix takes
	 * precednce and will be used instead of the classes default prefix.
	 * <p>
	 * If no prefix is given AND {@link #addFullQualifiedClassNameToPrefixList()} is false, then no prefix will be set.
	 * <p>
	 * TODO: currently there is no way to mix the class-name and primitive-strings. (i.e. to allow refactoring)
	 * 
	 * @return The prefix.
	 */
	String[] value() default {};

	/**
	 * This adds the current fully-qualified-class-name to the list of prefixes, above - regardless of what has been
	 * added there.
	 */
	boolean addFullQualifiedClassNameToPrefixList() default true;

	/**
	 * Specifies the default value. The number must correspond to the element in the array above. Counting starts by 0.
	 * The value -1 omits a default prefix (so there will be no default prefix).
	 * <p>
	 * If {@link #addFullQualifiedClassNameToPrefixList()} is true, then this will be added at the end of
	 * {@link #value()}. Therefor, if you put three elements in {@link #value()} and set
	 * {@link #addFullQualifiedClassNameToPrefixList()} to true, you can set this to 3, to mark the
	 * full-qualified-classname as the default.
	 */
	int defaultValue() default 0;
}
