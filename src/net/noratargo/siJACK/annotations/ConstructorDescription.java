package net.noratargo.siJACK.annotations;

public @interface ConstructorDescription {

	/**
	 * Specifies a description, that can be displayed to the user, when printing out the help for this element.
	 * 
	 * @return
	 */
	String description() default "";

	/**
	 * Specifies the name(s) of the groups, to that this constructo belongs.
	 * 
	 * @return
	 */
	String[] groupName() default "";
}
