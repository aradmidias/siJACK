package net.noratargo.siJACK.instanciators;

import net.noratargo.siJACK.interfaces.ParameterInstanciator;

public class FloatInstantiator implements ParameterInstanciator<Float> {

	@Override
	public Class<Float> getInstanceType() {
		return Float.class;
	}

	@Override
	public Float createNewInstanceFromString(String params) {
		return new Float(params);
	}

	@Override
	public Float createNewInstance(Float currentValue) {
		return currentValue;
	}

}
