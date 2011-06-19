package net.noratargo.siJACK.instanciators;

import net.noratargo.siJACK.interfaces.ParameterInstanciator;

public class LongInstantiator implements ParameterInstanciator<Long> {

	@Override
	public Class<Long> getInstanceType() {
		return Long.class;
	}

	@Override
	public Long createNewInstance(String params, Long currentValue) {
		/* The Long-Class is immutable, so we can return the provided object without problems: */
		return currentValue == null ? new Long(params) : currentValue;
	}

}
