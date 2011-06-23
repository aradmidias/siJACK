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

	/**
	 * Stores the description of every parameter. Note that you should always specify a default value, since it is not
	 * possible, to obtain default values for a method's parameter (since you can not specify any)
	 * 
	 * @return
	 */
	ParameterDescription[] parameters() default {};
}
