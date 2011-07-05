package net.noratargo.siJACK.instanciators;

import net.noratargo.siJACK.interfaces.ParameterInstanciator;

public class CharacterInstantiator implements ParameterInstanciator<Character> {

	@Override
	public Class<Character> getInstanceType() {
		return Character.class;
	}

	@Override
	public Character createNewInstanceFromString(String params) {
		return new Character(params.length() > 0 ? params.charAt(0) : 0x0);
	}

	@Override
	public Character createNewInstance(Character currentValue) {
		return currentValue;
	}

}
