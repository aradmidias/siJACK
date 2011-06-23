package net.noratargo.siJACK;

import net.noratargo.siJACK.annotations.ParameterDescription;
import net.noratargo.siJACK.interfaces.ParameterManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

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

	public void addConfigureable(Object o) {
		addConfigureable(o.getClass(), o);
	}

	public <T> void addConfigureable(final Class<T> oClass) {
		try {
			addConfigureable(oClass, oClass.newInstance());
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public <T> void addConfigureable(final Class<T> c, Object o) {
		addConfigureable(c, o, true);
	}

	private <T> void addConfigureable(final Class<T> c, Object o, boolean applyConfiguration) {
		if (c.getSuperclass() != null) {
			addConfigureable(c.getSuperclass(), o, false);
		}

		for (Field f : c.getDeclaredFields()) {
			try {
				f.setAccessible(true);
				pm.addField(f, f.get(o));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		for (Constructor<?> constr : c.getConstructors()) {
			pm.addConstructor(constr);
		}

		if (applyConfiguration) {
			pm.applyConfiguration();
		}
	}

	/**
	 * Creates a new instance of the given class-object by using the constructor, identified by signature, and using the
	 * parameters as given from the {@link ParameterManager}.
	 * 
	 * @param <T>
	 *            The type of the object to create.
	 * @param c
	 * @param signature
	 * @return
	 * @throws RuntimeException
	 *             If anything goes wrong.
	 */
	public <T> T createNewInstance(Class<T> c, Class<?>... signature) {
		try {
			Constructor<T> constructor = c.getConstructor(signature);
			Object[] values = pm.getValuesFor(constructor);

			return constructor.newInstance(values);
		} catch (Exception e) {
			throw new RuntimeException(e);
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
		applyConfiguration(o, o.getClass());
	}

	/**
	 * Applies the configuration to all fields, as far as they are marked with the {@link ParameterDescription}
	 * annotation.
	 * 
	 * @param o The object to apply the current values to.
	 * @param c The class object, from which to take the 
	 */
	private void applyConfiguration(Object o, Class<?> c) {
		if (c.getSuperclass() != null) {
			applyConfiguration(o, c.getSuperclass());
		}

		for (Field f : c.getDeclaredFields()) {
			if (f.getAnnotation(ParameterDescription.class) != null) {
				try {
					f.setAccessible(true);
					f.set(o, pm.getValueFor(f));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
