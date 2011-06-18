package net.noratargo.applicationFramework.testClasses;

import net.noratargo.applicationFramework.annotations.ParameterDescription;

public class ParametrisedClass extends AbstractParametrisedClass {

	@ParameterDescription("This field will be added to the ConfigurationManager.")
	public String subString = "unset as well";
}
