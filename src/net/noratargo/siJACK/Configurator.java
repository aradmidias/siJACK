package net.noratargo.siJACK;

import net.noratargo.siJACK.interfaces.ParameterManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

/**
 * Manages the configuration for the various elements.
 * 
 * @author HMulthaupt
 */
public class Configurator {

	private ParameterManager pm;

	private Set<Class<?>> knownClasses;

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
		knownClasses = new HashSet<Class<?>>();
	}

	/**
	 * Adds the given object to the ParameterManager, so that its fields and constructors can be configured.
	 * 
	 * @param <T>
	 *            The type of the object, being created.
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
	 *            The type of the object, being created.
	 * @param c
	 *            The class to add.
	 */
	public <T> void addConfigureable(Class<T> c) {
		T instance = null;

		try {
			/*
			 * try to create a new instance. If this fails (e.g. because c is an instance of Class<?>, then we still got
			 * the empty
			 */
			Constructor<T> constructor = c.getConstructor();

			if (constructor != null) {
				constructor.setAccessible(true);
				instance = constructor.newInstance();
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		addConfigureable(c, instance);
	}

	/**
	 * Adds the given class to the ParameterManager, using the <code>classInstance</code> for obtaining the default
	 * values.
	 * 
	 * @param <T>
	 *            The type of the object, being created.
	 * @param c
	 *            The class to add.
	 * @param classInstance
	 *            The object, that can be configured. The default values for the parameters will be taken from this
	 *            object. If this is <code>null</code> only default values from static fields can be obtained.
	 */
	public <T extends Object> void addConfigureable(Class<T> c, T classInstance) {
		if (c == null) {
			throw new NullPointerException("The parameter c must not be null!");
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
		if (!knownClasses.contains(c)) {
			knownClasses.add(c);

			if (c.getSuperclass() != null) {
				addConfigureable(c.getSuperclass(), o, false);
			}

			for (Field f : c.getDeclaredFields()) {
				try {
					f.setAccessible(true);

					/*
					 * do not obtain the field's value, if it is not static AND o is null - in that case we would run
					 * into a NullPointerException.
					 */
					pm.addField(f, (o == null && !Modifier.isStatic(f.getModifiers()) ? null : f.get(o)));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}

			addConstructors(c.getConstructors());
		}

		if (applyConfiguration) {
			pm.applyConfiguration();
		}
	}

	private void addConstructors(Constructor<?>[] constructors) {
		for (Constructor<?> constr : constructors) {
			pm.addConstructor(constr);
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
			if (!knownClasses.contains(constructor.getDeclaringClass())) {
				/* only add the constructors, here: */
				addConstructors(constructor.getDeclaringClass().getConstructors());

				pm.applyConfiguration();
			}

			Object[] values = pm.getValuesFor(constructor);

			T instance;
			instance = constructor.newInstance(values);

			/* TODO: add the fields and so on, here: */
			if (!knownClasses.contains(constructor.getDeclaringClass())) {
				addConfigureable(constructor.getDeclaringClass(), instance, true);
			}

			/* apply the configuration if desired: */
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
		Class<?> c = o.getClass();

		if (!knownClasses.contains(c)) {
			/* let's be merciful to people, who forgot to add */
			addConfigureable(c);
		}

		applyConfiguration(o, c);
	}
	
	public void applyConfiguration(Class<?> c) {

		if (!knownClasses.contains(c)) {
			/* let's be merciful to people, who forgot to add */
			addConfigureable(c);
		}

		applyConfiguration(null, c);
	}

	/**
	 * Applies the configuration to all fields, as far as they are marked with the {@link FieldDescription} annotation.
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
					if (o != null || Modifier.isStatic(f.getModifiers())) {
						f.setAccessible(true);
						f.set(o, pm.getValueFor(f));
					} else {
						System.err.println("net.noratargo.siJACK.Configurator.applyConfiguration(Object, Class<?>) skipping field \""+ f.toGenericString() +"\" because there is no instance available, to that this field's current value could be applied to.");
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
