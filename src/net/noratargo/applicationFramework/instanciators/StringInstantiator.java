package net.noratargo.applicationFramework.instanciators;

import net.noratargo.applicationFramework.interfaces.ParameterInstanciator;

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
