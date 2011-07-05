package net.noratargo.siJACK;

import java.util.HashMap;
import java.util.Map;

import net.noratargo.siJACK.instanciators.BooleanInstantiator;
import net.noratargo.siJACK.instanciators.ByteInstantiator;
import net.noratargo.siJACK.instanciators.CharacterInstantiator;
import net.noratargo.siJACK.instanciators.ClassInstantiator;
import net.noratargo.siJACK.instanciators.DoubleInstantiator;
import net.noratargo.siJACK.instanciators.FloatInstantiator;
import net.noratargo.siJACK.instanciators.IntegerInstantiator;
import net.noratargo.siJACK.instanciators.LongInstantiator;
import net.noratargo.siJACK.instanciators.ShortInstantiator;
import net.noratargo.siJACK.instanciators.StringInstantiator;
import net.noratargo.siJACK.interfaces.InstantiatorManager;
import net.noratargo.siJACK.interfaces.ParameterInstanciator;

public class InstantiatorStorage implements InstantiatorManager {

	private final Map<Class<?>, ParameterInstanciator<?>> instantiators = new HashMap<Class<?>, ParameterInstanciator<?>>();
	
	public InstantiatorStorage() {
		/* native datatypes: */
		ParameterInstanciator<?> i;
		i = new BooleanInstantiator();
		addInstantiator(i, Boolean.class);
		addInstantiator(i, boolean.class);

		i = new ByteInstantiator();
		addInstantiator(i, Byte.class);
		addInstantiator(i, byte.class);
		
		i = new ShortInstantiator();
		addInstantiator(i, Short.class);
		addInstantiator(i, short.class);

		i = new CharacterInstantiator();
		addInstantiator(i, Character.class);
		addInstantiator(i, char.class);
		
		i = new IntegerInstantiator();
		addInstantiator(i, Integer.class);
		addInstantiator(i, int.class);
		
		i = new LongInstantiator();
		addInstantiator(i, Long.class);
		addInstantiator(i, long.class);
		
		i = new FloatInstantiator();
		addInstantiator(i, Float.class);
		addInstantiator(i, float.class);
		
		i = new DoubleInstantiator();
		addInstantiator(i, Double.class);
		addInstantiator(i, double.class);
		
		/* Additional "standard" types: */
		addInstantiator(new StringInstantiator());
		addInstantiator(new ClassInstantiator());

	}

	@Override
	public <T> T getNewInstanceFor(Class<T> c, String stringRepresentation) {
		ParameterInstanciator<T> p = getInstanciator(c);
		return p.createNewInstanceFromString(stringRepresentation);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getNewInstanceFrom(T originalObject) {
		return originalObject == null ? null : getInstanciator((Class<T>) originalObject.getClass()).createNewInstance(originalObject);
	}

	@SuppressWarnings("unchecked")
	private <T> ParameterInstanciator<T> getInstanciator(Class<T> c) {
		if (! instantiators.containsKey(c)) {
			System.err.println("net.noratargo.siJACK.InstantiatorStorage.getInstanciator(Class<T>) There is no instantiator for "+c +". This might result in problems!");
			
			return new ParameterInstanciator<T>() {

				@Override
				public Class<T> getInstanceType() {
					return null;
				}

				@Override
				public T createNewInstanceFromString(String param) {
					return null; //we can not create a new instance, here.
				}

				@Override
				public T createNewInstance(T originalValue) {
					return originalValue;
				}
			};
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
