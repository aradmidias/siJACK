package net.noratargo.siJACK.instanciators;

import net.noratargo.siJACK.interfaces.ParameterInstanciator;

public class ByteInstantiator implements ParameterInstanciator<Byte> {

	@Override
	public Class<Byte> getInstanceType() {
		return Byte.class;
	}

	@Override
	public Byte createNewInstanceFromString(String params) {
		return new Byte(params);
	}

	@Override
	public Byte createNewInstance(Byte currentValue) {
		return currentValue;
	}

}
