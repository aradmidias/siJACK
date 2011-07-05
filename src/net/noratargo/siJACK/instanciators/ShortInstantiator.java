package net.noratargo.siJACK.instanciators;

import net.noratargo.siJACK.interfaces.ParameterInstanciator;

public class ShortInstantiator implements ParameterInstanciator<Short> {

	@Override
	public Class<Short> getInstanceType() {
		return Short.class;
	}

	@Override
	public Short createNewInstanceFromString(String params) {
		return new Short(params);
	}

	@Override
	public Short createNewInstance(Short currentValue) {
		return currentValue;
	}

}
