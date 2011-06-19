package net.noratargo.siJACK.instanciators;

import net.noratargo.siJACK.interfaces.ParameterInstanciator;

public class StringInstantiator implements ParameterInstanciator<String> {

	@Override
	public Class<String> getInstanceType() {
		return String.class;
	}

	@Override
	public String createNewInstanceFromString(String param) {
		return param;
	}
	
	@Override
	public String createNewInstance(String originalValue) {
		return originalValue;
	}
}
