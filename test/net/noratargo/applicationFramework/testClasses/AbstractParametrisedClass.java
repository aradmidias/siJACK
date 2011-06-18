package net.noratargo.applicationFramework.testClasses;

import net.noratargo.applicationFramework.annotations.ParameterDescription;

public abstract class AbstractParametrisedClass {

	@ParameterDescription("This field should be define-able,  although this class is not instanciable!")
	public String s = "unset value";
}
