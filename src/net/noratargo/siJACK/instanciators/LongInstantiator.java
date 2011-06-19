package net.noratargo.siJACK.instanciators;

import net.noratargo.siJACK.interfaces.ParameterInstanciator;

public class LongInstantiator implements ParameterInstanciator<Long> {

	@Override
	public Class<Long> getInstanceType() {
		return Long.class;
	}

	@Override
	public Long createNewInstanceFromString(String param) {
		return new Long(param);
	}
	
	@Override
	public Long createNewInstance(Long originalValue) {
		return originalValue;
	}
}
