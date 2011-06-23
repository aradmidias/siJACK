package net.noratargo.siJACK.testClasses;

import net.noratargo.siJACK.annotations.ConstructorDescription;
import net.noratargo.siJACK.annotations.ParameterDescription;
import net.noratargo.siJACK.annotations.ParameterName;
import net.noratargo.siJACK.annotations.Prefix;

public class ConstructorDescriberConstructableClass {
	public String s;
	public Long l;
	public Boolean b;

	@ConstructorDescription(groupName = { "", "type a" }, parameters = { @ParameterDescription(name = @ParameterName({
			"s", "string" }), defaultValue = "hallo") })
	public ConstructorDescriberConstructableClass(String s) {
		this.s = s;
	}

	@ConstructorDescription(groupName = { "", "type a" }, parameters = {
			@ParameterDescription(name = @ParameterName({ "l", "loadFactor" }), defaultValue = "515023112652"),
			@ParameterDescription(name = @ParameterName({ "isDynamic" }), defaultValue = "true"), })
	public ConstructorDescriberConstructableClass(Long l, boolean b) {
		this.b = b;
		this.l = l;
	}

	@Prefix()
	@ConstructorDescription(groupName = { "", "type a" }, parameters = {
			@ParameterDescription(name = @ParameterName({ "s", "string" }), defaultValue = "hallo"),
			@ParameterDescription(name = @ParameterName({ "l" }), defaultValue = "9842196030", description = "I will not be affected by setting parameter \"loadFactor\"!") })
	public ConstructorDescriberConstructableClass(String s, Long l) {
		this.l = l;
		this.s = s;
	}

	@ConstructorDescription(groupName = { "", "type a" }, parameters = {
			@ParameterDescription(name = @ParameterName({ "s", "string" }), defaultValue = "hallo"),
			@ParameterDescription(name = @ParameterName({ "isDynamic" }), defaultValue = "true") })
	public ConstructorDescriberConstructableClass(String s, boolean b) {
		this.b = b;
		this.s = s;
	}

	@ConstructorDescription(groupName = { "", "type a" }, parameters = {
			@ParameterDescription(name = @ParameterName({ "isDynamic" }), defaultValue = "true"),
			@ParameterDescription(name = @ParameterName({ "loadFactor" }), defaultValue = "51641012020", description = "i will not be changed by setting parameter \"l\"!") })
	public ConstructorDescriberConstructableClass(boolean b, Long l) {
		this.b = b;
		this.l = l;
	}
}
