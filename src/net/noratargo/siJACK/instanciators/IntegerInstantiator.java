package net.noratargo.siJACK.instanciators;

import net.noratargo.siJACK.interfaces.ParameterInstanciator;

public class IntegerInstantiator implements ParameterInstanciator<Integer> {

	@Override
	public Class<Integer> getInstanceType() {
		return Integer.class;
	}

	@Override
	public Integer createNewInstanceFromString(String params) {
		return new Integer(params);
	}

	@Override
	public Integer createNewInstance(Integer currentValue) {
		return currentValue;
	}

}
