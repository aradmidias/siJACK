package net.noratargo.siJACK.instanciators;

import net.noratargo.siJACK.interfaces.ParameterInstanciator;

/**
 * This instantiator can create {@link Class} instances for a fully qualified class - as far as it is accessible
 * 
 * @author HMulthaupt
 */
/* Supressing the warnings was necessary, since I was not allowed to cast the Class.class Element to Class<Class<?>> */
@SuppressWarnings("rawtypes")
public class ClassInstantiator implements ParameterInstanciator<Class> {

	@Override
	public Class<Class> getInstanceType() {
		return Class.class;
	}

	@Override
	public Class createNewInstanceFromString(String param) {
		try {
			/* the class-object is immutable. */
			return Class.forName(param);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Class createNewInstance(Class originalValue) {
		return originalValue;
	}

}
