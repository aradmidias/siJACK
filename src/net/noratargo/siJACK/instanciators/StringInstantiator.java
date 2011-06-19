package net.noratargo.siJACK.instanciators;

import net.noratargo.siJACK.interfaces.ParameterInstanciator;

public class StringInstantiator implements ParameterInstanciator<String> {

	@Override
	public Class<String> getInstanceType() {
		return String.class;
	}

	@Override
	public String createNewInstance(String params, String currentValue) {
		return currentValue == null ? params : currentValue;
	}

}
