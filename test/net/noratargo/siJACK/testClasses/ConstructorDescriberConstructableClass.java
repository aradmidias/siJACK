package net.noratargo.siJACK.testClasses;

import net.noratargo.siJACK.annotations.ConstructorDescription;
import net.noratargo.siJACK.annotations.Name;
import net.noratargo.siJACK.annotations.ParameterDescription;
import net.noratargo.siJACK.annotations.Prefix;

public class ConstructorDescriberConstructableClass {
	public String s;
	public Long l;
	public Boolean b;

	@ConstructorDescription(groupName = { "", "type a" })
	public ConstructorDescriberConstructableClass(
			@ParameterDescription(name = @Name({ "s", "string" }), defaultValue = "hallo") String s) {
		this.s = s;
	}

	@ConstructorDescription(groupName = { "", "type a" })
	public ConstructorDescriberConstructableClass(
			@ParameterDescription(name = @Name({ "l", "loadFactor" }), defaultValue = "515023112652") Long l,
			@ParameterDescription(name = @Name({ "isDynamic" }), defaultValue = "true") boolean b) {
		this.b = b;
		this.l = l;
	}

	@Prefix()
	@ConstructorDescription(groupName = { "", "type a" })
	public ConstructorDescriberConstructableClass(
			@ParameterDescription(name = @Name({ "s", "string" }), defaultValue = "hallo") String s,
			@ParameterDescription(name = @Name({ "l" }), defaultValue = "9842196030", description = "I will not be affected by setting parameter \"loadFactor\"!") Long l) {
		this.l = l;
		this.s = s;
	}

	@ConstructorDescription(groupName = { "", "type a" })
	public ConstructorDescriberConstructableClass(
			@ParameterDescription(name = @Name({ "s", "string" }), defaultValue = "hallo") String s,
			@ParameterDescription(name = @Name({ "isDynamic" }), defaultValue = "true") boolean b) {
		this.b = b;
		this.s = s;
	}

	@ConstructorDescription(groupName = { "", "type a" })
	public ConstructorDescriberConstructableClass(
			@ParameterDescription(name = @Name({ "isDynamic" }), defaultValue = "true") boolean b,
			@ParameterDescription(name = @Name({ "loadFactor" }), defaultValue = "51641012020", description = "i will not be changed by setting parameter \"l\"!") Long l) {
		this.b = b;
		this.l = l;
	}
}
