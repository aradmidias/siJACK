package net.noratargo.siJACK.testClasses;

import net.noratargo.siJACK.annotations.Description;

public class ParametrisedClass extends AbstractParametrisedClass {

	@Description("This field will be added to the ConfigurationManager.")
	public String subString = "unset as well";
}
