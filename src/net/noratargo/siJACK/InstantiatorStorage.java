package net.noratargo.siJACK;

import java.util.HashMap;
import java.util.Map;

import net.noratargo.siJACK.instanciators.ClassInstantiator;
import net.noratargo.siJACK.instanciators.IntegerInstantiator;
import net.noratargo.siJACK.instanciators.LongInstantiator;
import net.noratargo.siJACK.instanciators.StringInstantiator;
import net.noratargo.siJACK.interfaces.InstantiatorManager;
import net.noratargo.siJACK.interfaces.ParameterInstanciator;

public class InstantiatorStorage implements InstantiatorManager {

	private final Map<Class<?>, ParameterInstanciator<?>> instantiators = new HashMap<Class<?>, ParameterInstanciator<?>>();
	
	public InstantiatorStorage() {
		addInstantiator(new StringInstantiator());
		addInstantiator(new ClassInstantiator());

		/* native datatypes: */
		ParameterInstanciator<?> i;
		i = new IntegerInstantiator();
		addInstantiator(i, Integer.class);
		addInstantiator(i, int.class);
		i = new LongInstantiator();
		addInstantiator(i, Long.class);
		addInstantiator(i, long.class);
	}

	@Override
	public <T> T getNewInstanceFor(Class<T> c, String stringRepresentation) {
		ParameterInstanciator<T> p = getInstanciator(c);
		return p.createNewInstance(stringRepresentation, null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getNewInstanceFrom(T originalObject) {
		return getInstanciator((Class<T>) originalObject.getClass()).createNewInstance(null, originalObject);
	}

	@SuppressWarnings("unchecked")
	private <T> ParameterInstanciator<T> getInstanciator(Class<T> c) {
		if (! instantiators.containsKey(c)) {
			throw new RuntimeException("There is no instantiator for "+c +" can not continue!");
		}
		
		/* here, i assume (hope?), that this will always be the correct type: */
		return (ParameterInstanciator<T>) instantiators.get(c);
	}
	
	@Override
	public void addInstantiator(ParameterInstanciator<?> i) {
		instantiators.put(i.getInstanceType(), i);
	}

	@Override
	public void addInstantiator(ParameterInstanciator<?> i, Class<?> c) {
		instantiators.put(c, i);
	}

}
