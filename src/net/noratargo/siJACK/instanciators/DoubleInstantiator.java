package net.noratargo.siJACK.instanciators;

import net.noratargo.siJACK.interfaces.ParameterInstanciator;

public class DoubleInstantiator implements ParameterInstanciator<Double> {

	@Override
	public Class<Double> getInstanceType() {
		return Double.class;
	}

	@Override
	public Double createNewInstanceFromString(String params) {
		return new Double(params);
	}

	@Override
	public Double createNewInstance(Double currentValue) {
		return currentValue;
	}

}
