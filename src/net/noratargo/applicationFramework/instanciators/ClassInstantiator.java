package net.noratargo.applicationFramework.instanciators;

import net.noratargo.applicationFramework.interfaces.ParameterInstanciator;

public class ClassInstantiator implements ParameterInstanciator<Class> {

	@Override
	public Class<Class> getInstanceType() {
		return Class.class;
	}

	@Override
	public Class createNewInstance(String params, Class currentValue) {
		try {
			/* the class-object is immutable. */
			return currentValue == null ? (Class.forName(params)) : currentValue;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

}
