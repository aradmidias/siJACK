package net.noratargo.siJACK.testClasses;

import net.noratargo.siJACK.annotations.FieldDescription;

public abstract class AbstractParametrisedClass {

	@FieldDescription("This field should be define-able,  although this class is not instanciable!")
	public String s = "unset value";
}
