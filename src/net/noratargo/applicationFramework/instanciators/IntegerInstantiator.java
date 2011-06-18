package net.noratargo.applicationFramework.instanciators;

import net.noratargo.applicationFramework.interfaces.ParameterInstanciator;

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
