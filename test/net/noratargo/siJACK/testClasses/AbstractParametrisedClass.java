package net.noratargo.siJACK.testClasses;

import net.noratargo.siJACK.annotations.Description;

public abstract class AbstractParametrisedClass {

	@Description("This field should be define-able,  although this class is not instanciable!")
	public String s = "unset value";
}
