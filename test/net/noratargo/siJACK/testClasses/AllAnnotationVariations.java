package net.noratargo.siJACK.testClasses;

import net.noratargo.siJACK.annotations.DefaultValue;
import net.noratargo.siJACK.annotations.Description;
import net.noratargo.siJACK.annotations.Name;
import net.noratargo.siJACK.annotations.Prefix;

/* add a second parameter: */
@Prefix("AllAnnotationVariations")
public class AllAnnotationVariations {

	@Prefix("")
	@Description("This is everything...")
	@DefaultValue("field 2 is better then field 1")
	@Name({"field2", "filed3"})
	public String field1 = "field 1";
	
	@Prefix("")
	@Name("additionalConstructorName")
	@Description("This one creates a simple object")
	public AllAnnotationVariations(
			@Prefix("anotherPrefixForAParameter")
			@Name("field23") // The name, under that this parameter can be adressed.
			@Description("the value for a field")
			@DefaultValue("maybe group 999 is better than asdf")
			String field23) {
		field1 = field23;
	}
}
