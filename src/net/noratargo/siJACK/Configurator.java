package net.noratargo.siJACK;

import net.noratargo.siJACK.interfaces.ParameterManager;

/**
 * Manages the configuration for the various elements.
 * 
 * @author HMulthaupt
 */
public class Configurator {

	private ParameterManager pm;

	public Configurator(ParameterManager pm) {
		this.pm = pm;
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

	public <T> T createNewInstance(Class<T> from, Object... o) {
		return null;
	}
	
}
