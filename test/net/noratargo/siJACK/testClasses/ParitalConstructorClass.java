package net.noratargo.siJACK.testClasses;

import net.noratargo.siJACK.annotations.DefaultValue;
import net.noratargo.siJACK.annotations.Name;
import net.noratargo.siJACK.annotations.PartialConstructor;
import net.noratargo.siJACK.interfaces.ConfigurationManager;

public class ParitalConstructorClass {

	public final int i;
	public final String s;
	public final ConfigurationManager cm;

	public ParitalConstructorClass(int i) {
		this.i = i;
		this.s = null;
		this.cm = null;
	}

	@PartialConstructor
	public ParitalConstructorClass(@DefaultValue("245") @Name("i") int i,
			@DefaultValue("Lorem ipsum") @Name("s") String s, ConfigurationManager cm) {
		this.i = i;
		this.s = s;
		this.cm = cm;
	}

	public ParitalConstructorClass() {
		this.i = -1000;
		this.s = null;
		this.cm = null;
	}
}
