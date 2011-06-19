package net.noratargo.siJACK.instanciators;

import net.noratargo.siJACK.interfaces.ParameterInstanciator;

public class IntegerInstantiator implements ParameterInstanciator<Integer> {

	@Override
	public Class<Integer> getInstanceType() {
		return Integer.class;
	}

	@Override
	public Integer createNewInstance(String params, Integer currentValue) {
		return (params == null ? new Integer(currentValue) : new Integer(params));
	}

}
