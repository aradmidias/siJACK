package net.noratargo.applicationFramework;

import net.noratargo.applicationFramework.instanciators.ClassInstantiator;
import net.noratargo.applicationFramework.instanciators.IntegerInstantiator;
import net.noratargo.applicationFramework.instanciators.LongInstantiator;
import net.noratargo.applicationFramework.instanciators.StringInstantiator;
import net.noratargo.applicationFramework.interfaces.ParameterInstanciator;
import net.noratargo.applicationFramework.interfaces.ParameterManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages the configuration for the various elements.
 * 
 * @author HMulthaupt
 */
public class Configurator {

	private ParameterManager pm;

	private static Map<Class<?>, ParameterInstanciator<?>> instantiators = new HashMap<Class<?>, ParameterInstanciator<?>>();
	
	static {
		addInstantiator(new StringInstantiator());
		addInstantiator(new IntegerInstantiator(), new Class<?>[] {Integer.class, int.class});
		addInstantiator(new LongInstantiator(), new Class<?>[] {Long.class, long.class});
		addInstantiator(new ClassInstantiator());
	}
	
	public Configurator(ParameterManager pm) {
		this.pm = pm;
	}

	@SuppressWarnings("unchecked")
	public static <T> ParameterInstanciator<T> getInstanciator(Class<T> c) {
		if (! instantiators.containsKey(c)) {
			throw new RuntimeException("There is no instantiator for "+c +" can not continue!");
		}
		
		/* here, i assume (hope?), that this will always be the correct type: */
		return (ParameterInstanciator<T>) instantiators.get(c);
	}
	
	public static void addInstantiator(ParameterInstanciator<?> i) {
		instantiators.put(i.getInstanceType(), i);
	}
	
	private static void addInstantiator(ParameterInstanciator<?> i, Class<?>[] classes) {
		for (Class<?> c : classes) {
			instantiators.put(c, i);
		}
	}

	/**
	 * Applies the current configuration to the given object. If no configuration has been set for a specific field, its
	 * default value will be used. If no default value is applicable, a runtime exception might occur, if setting am
	 * empty string is not permitted.
	 * 
	 * @param o
	 *            The object to apply the configuration to.
	 */
	public void applyConfiguration(Object o) {
		pm.configureObject(o, o.getClass(), this);
	}

	public void addConfigureable(Object o) {
		addConfigureable(o.getClass(), o);
	}

	public <T> void addConfigureable(final Class<T> oClass) {
		try {
			addConfigureable(oClass, oClass.newInstance());
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public <T> void addConfigureable(final Class<T> c, Object o) {
		pm.addClass(c, o);
		pm.applyConfiguration();
	}

}
