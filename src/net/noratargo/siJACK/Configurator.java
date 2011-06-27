package net.noratargo.siJACK;

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

	/**
	 * Creates a new Configurator, using the given {@link ParameterManager}.
	 * 
	 * @param pm
	 *            The ParameterManager to use for storing the configureable {@link Field}s and {@link Constructor}s.
	 */
	public Configurator(ParameterManager pm) {
		if (pm == null) {
			throw new NullPointerException("The parameter pm must not be null!");
		}

		this.pm = pm;
	}

	/**
	 * Adds the given object to the ParameterManager, so that its fields and constructors can be configured.
	 * 
	 * @param <T>
	 *            The  type of the object, being created.
	 * @param classInstance
	 *            The object, that can be configured. The default values for the parameters will be taken from this
	 *            object.
	 */
	@SuppressWarnings("unchecked")
	public <T extends Object> void addConfigureable(T classInstance) {
		/* I assume, that o.getClass() always returns the Class<T>-object if called on an object of type T. */
		addConfigureable((Class<T>) classInstance.getClass(), classInstance);
	}

	/**
	 * Adds the given class to the ParameterManager, so that its fields and constructors can be configured.
	 * <p>
	 * This method will fail, if there is no parameterless constructor, to use for initialisation.
	 * 
	 * @param <T>
	 *            The  type of the object, being created.
	 * @param c
	 *            The class to add.
	 */
	public <T> void addConfigureable(Class<T> c) {
		try {
			addConfigureable(c, c.newInstance());
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adds the given class to the ParameterManager, using the <code>classInstance</code> for obtaining the default
	 * values.
	 * 
	 * @param <T>
	 *            The  type of the object, being created.
	 * @param c
	 *            The class to add.
	 * @param classInstance
	 *            The object, that can be configured. The default values for the parameters will be taken from this
	 *            object.
	 */
	public <T extends Object> void addConfigureable(Class<T> c, T classInstance) {
		if (c == null) {
			throw new NullPointerException("The parameter c must not be null!");
		}

		if (classInstance == null) {
			throw new NullPointerException("The parameter classInstance must not be null.");
		}

		addConfigureable(c, classInstance, true);
	}

	/**
	 * @param <T>
	 *            any sub-type of Object - only used for sme type-safety.
	 * @param c
	 * @param o
	 * @param applyConfiguration
	 */
	private <T extends Object> void addConfigureable(Class<T> c, T o, boolean applyConfiguration) {
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
	 *            The class to create a new instance from.
	 * @param signature
	 *            the signature of the constructor to use for creation
	 * @return The instanciated object.
	 * @throws RuntimeException
	 *             If the new object could not be instantiated.
	 */
	public <T> T createNewInstance(Class<T> c, Class<?>... signature) {
		return createNewInstance(c, true, signature);
	}

	/**
	 * @param <T>
	 *            The type of the object to create.
	 * @param c
	 *            The class to create a new instance from.
	 * @param autoConfigure
	 *            if <code>true</code> then the {@link #applyConfiguration(Object)} method will be called on this
	 *            object.
	 * @param signature
	 *            the signature of the constructor to use for creation
	 * @return The instanciated object.
	 * @throws RuntimeException
	 *             If the new object could not be instantiated.
	 */
	public <T> T createNewInstance(Class<T> c, boolean autoConfigure, Class<?>... signature) {
		try {
			return createNewInstance(c.getConstructor(signature), autoConfigure);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Creates a new instance of the type <code>T</code> by using the given <code>constructor</code>. The
	 * autoconfigure-flag allows to automatically call {@link #applyConfiguration(Object)}, using the newly created
	 * object.
	 * 
	 * @param <T>
	 *            The type of the object to create.
	 * @param constructor
	 *            The constructor to use for creation.
	 * @param autoConfigure
	 *            if <code>true</code> then the {@link #applyConfiguration(Object)} method will be called on this
	 *            object.
	 * @return The instanciated object.
	 * @throws RuntimeException
	 *             If the new object could not be instantiated.
	 * @see #applyConfiguration(Object)
	 */
	public <T> T createNewInstance(Constructor<T> constructor, boolean autoConfigure) {
		try {
			Object[] values = pm.getValuesFor(constructor);

			T instance;
			instance = constructor.newInstance(values);

			if (autoConfigure) {
				applyConfiguration(instance);
			}

			return instance;
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
	 * Applies the configuration to all fields, as far as they are marked with the {@link FieldDescription}
	 * annotation.
	 * 
	 * @param o
	 *            The object to apply the current values to.
	 * @param c
	 *            The class object, from which to take the
	 */
	private void applyConfiguration(Object o, Class<?> c) {
		if (c.getSuperclass() != null) {
			applyConfiguration(o, c.getSuperclass());
		}

		for (Field f : c.getDeclaredFields()) {
			if (pm.hasField(f)) {
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
