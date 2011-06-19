package net.noratargo.siJACK.testClasses;

import net.noratargo.siJACK.annotations.ParameterDescription;

public abstract class AbstractParametrisedClass {

	@ParameterDescription("This field should be define-able,  although this class is not instanciable!")
	public String s = "unset value";
}
