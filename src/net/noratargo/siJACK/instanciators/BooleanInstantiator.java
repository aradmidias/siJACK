package net.noratargo.siJACK.instanciators;

import net.noratargo.siJACK.interfaces.ParameterInstanciator;

public class BooleanInstantiator implements ParameterInstanciator<Boolean> {

	@Override
	public Class<Boolean> getInstanceType() {
		return Boolean.class;
	}

	@Override
	public Boolean createNewInstanceFromString(String param) {
		return Boolean.valueOf(param);
	}

	@Override
	public Boolean createNewInstance(Boolean originalValue) {
		return originalValue;
	}

}
