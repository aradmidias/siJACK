package net.noratargo.siJACK.testClasses;

import net.noratargo.siJACK.annotations.FieldDescription;

public class ParametrisedClass extends AbstractParametrisedClass {

	@FieldDescription("This field will be added to the ConfigurationManager.")
	public String subString = "unset as well";
}
