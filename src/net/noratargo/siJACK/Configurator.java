package net.noratargo.siJACK;

import net.noratargo.siJACK.annotations.DefaultConstructor;
import net.noratargo.siJACK.annotations.DefaultValue;
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
 * @author HMulthaupt TODO: If no object is given - but (only) a class-object, then do not create an instance and
 *         therefore do NOT analyse the non-static members (except constructors). TODO: Also do NOT add fields, after
 *         the constructor's creation. This might break the configuration's default values.
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
	public <T> void addConfigureable(T classInstance) {
		/* I assume, that o.getClass() always returns the Class<T>-object if called on an object of type T. */
		addConstructors(classInstance.getClass());
		addFields((Class<T>) classInstance.getClass(), classInstance);
		markClassAsAdded(classInstance.getClass());
		pm.applyConfiguration();
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
		addConfigureable(c, true);
	}

	/**
	 * Adds the given class to the ParameterManager, so that its fields and constructors can be configured.
	 * <p>
	 * If <code>tryToInstantiate</code> is <code>false</code> then all <i>non-static</i> fields <b>MUST BE</b> annotated
	 * with a {@link DefaultValue} annotation (since no instance should be created and therefore the non-static fields
	 * can not be initialised - so their values can not be obtained).
	 * <p>
	 * TODO: Note: It might be, that this behavior changes in future relaeses: The following possibility exists: If a
	 * class is given and no instance should be created, the non-static fields are still visible (but not yet set with
	 * values). Therefore these fields could be added as parameters, having the {@link Parameter#hasDefaultValue()}
	 * method returning <code>false</code> to indicate, that there is no default value. But it may still have a
	 * (non-default) value. This scenario would lead to the following conclusion:
	 * 
	 * @param <T>
	 *            The type of the object, being created.
	 * @param c
	 *            The class to add.
	 * @param tryToInstantiate
	 *            if <code>true</code>, then the method will try to create a new instance. If <code>false</code> then
	 *            <code>null</code> will be used as object instance and therefore only constructors and static fields
	 *            will be analysed, unless the non-static fields do have a {@link DefaultValue} annotation.
	 */
	public <T> void addConfigureable(Class<T> c, boolean tryToInstantiate) {
		addConstructors(c);

		T instance = null;

		if (tryToInstantiate) {
			try {
				/* use the parameterless construcotr for instantiation: */
				instance = newInstanceFromSignature(c, false);
			} catch (RuntimeException re) {
				/* do nothing - the instantiation just failed. */
			}
		}

		addFields(c, instance);
		markClassAsAdded(c);
		pm.applyConfiguration();
	}

	/**
	 * Adds all fields of the given class. This method works recursiv up to the first class, that is already in
	 * {@link #knownClasses}.
	 * 
	 * @param <T>
	 *            any sub-type of Object - only used for sme type-safety.
	 * @param c
	 *            The class to add the fields from.
	 * @param o
	 *            An instanco of <code>c</code>, if available.
	 * @param applyConfiguration
	 */
	private <T extends Object> void addFields(Class<T> c, T o) {
		if (!knownClasses.contains(c)) {
			if (c.getSuperclass() != null) {
				addFields(c.getSuperclass(), o);
			}

			for (Field f : c.getDeclaredFields()) {
				try {
					f.setAccessible(true);

					/*
					 * do not obtain the field's value, if it is not static AND o is null - in that case we would run
					 * into a NullPointerException.
					 */
					if (o == null && !Modifier.isStatic(f.getModifiers())) {
						pm.addField(f);
					} else {
						pm.addField(f, f.get(o));
					}
				} catch (Exception e) {
					/* print a stack trace and continue with the next field. */
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Adds all construcotrs of class c. This method works recursiv up to the first class, that is already in
	 * {@link #knownClasses}.
	 * 
	 * @param c
	 *            The class to add all constructors from.
	 */
	private void addConstructors(Class<?> c) {
		if (!knownClasses.contains(c)) {
			if (c.getSuperclass() != null) {
				addConstructors(c.getSuperclass());
			}

			Constructor<?>[] constructors = c.getDeclaredConstructors();

			for (Constructor<?> constr : constructors) {
				constr.setAccessible(true);
				pm.addConstructor(constr);
			}
		}
	}

	private void markClassAsAdded(Class<?> c) {
		if (c != null && !knownClasses.contains(c)) {
			markClassAsAdded(c.getSuperclass());
			knownClasses.add(c);
		}
	}

	/**
	 * Creates a new class from a partially parametrised constructor.
	 * <p>
	 * You may specify more params then the constructr can accept, since only the missing parameters will be added when
	 * creating the instance.
	 * <p>
	 * There has to be a constructor, annotated with
	 * 
	 * @param <T>
	 * @param c
	 *            The class to create a new instance from.
	 * @param params
	 *            One or more parameters to use for instanciation. If one or more parameters are null, they will be
	 *            ignored.
	 * @return
	 */
	public <T> T newInstanceFromPartialConstructor(Class<T> c, boolean autoConfigure, Object... params) {
		if (!knownClasses.contains(c)) {
			addConstructors(c);
			pm.applyConfiguration();
		}

		try {
			Constructor<T> paritalConstructor = pm.getPartialConstructor(c);
			return newInstance(paritalConstructor, autoConfigure, pm.getValuesFor(paritalConstructor, params));
		} catch (Exception e) {
			/* the instantiation failed - so we have to take care of adding the fields and marking the classes: */
			addFields(c, null);
			markClassAsAdded(c);
			pm.applyConfiguration();
			
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns a new instance, using the default constructor.
	 * <p>
	 * The default constructor, is marked with the {@link DefaultConstructor} annotation.
	 * 
	 * @param <T>
	 *            The type of the created class.
	 * @param c
	 *            The class to create an object from, by using
	 * @param autoConfigure
	 *            if the created object should be configured, using the {@link #getConfiguration(Object, Class)} method.
	 * @return The created classe's instance.
	 */
	public <T> T newInstanceFromDefaultConstructor(Class<T> c, boolean autoConfigure) {
		if (!knownClasses.contains(c)) {
			addConstructors(c);
		}

		return newInstanceFromConstructor(pm.getDefaultConstructor(c), autoConfigure);
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
	public <T> T newInstanceFromSignature(Class<T> c, Class<?>... signature) {
		try {
			return newInstanceFromConstructor(c.getDeclaredConstructor(signature), true);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @param <T>
	 *            The type of the object to create.
	 * @param c
	 *            The class to create a new instance from.
	 * @param autoConfigure
	 *            if <code>true</code> then the {@link #getConfigurationForObject(Object)} method will be called on this
	 *            object.
	 * @param signature
	 *            the signature of the constructor to use for creation
	 * @return The instanciated object.
	 * @throws RuntimeException
	 *             If the new object could not be instantiated.
	 */
	public <T> T newInstanceFromSignature(Class<T> c, boolean autoConfigure, Class<?>... signature) {
		try {
			return newInstanceFromConstructor(c.getDeclaredConstructor(signature), autoConfigure);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Creates a new instance of the type <code>T</code> by using the given <code>constructor</code>. The
	 * autoconfigure-flag allows to automatically call {@link #getConfigurationForObject(Object)}, using the newly
	 * created object.
	 * 
	 * @param <T>
	 *            The type of the object to create.
	 * @param constructor
	 *            The constructor to use for creation.
	 * @param autoConfigure
	 *            if <code>true</code> then the {@link #getConfigurationForObject(Object)} method will be called on this
	 *            object.
	 * @return The instanciated object.
	 * @throws RuntimeException
	 *             If the new object could not be instantiated.
	 * @see #getConfigurationForObject(Object)
	 */
	public <T> T newInstanceFromConstructor(Constructor<T> constructor, boolean autoConfigure) {
		if (!knownClasses.contains(constructor.getDeclaringClass())) {
			addConstructors(constructor.getDeclaringClass());
			pm.applyConfiguration();
		}

		try {
			return newInstance(constructor, autoConfigure, pm.getValuesFor(constructor));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private <T> T newInstance(Constructor<T> constructor, boolean autoConfigure, Object... params)
			throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
		constructor.setAccessible(true);
		T instance = constructor.newInstance(params);

		/* add the fields and so on, here: */
		if (!knownClasses.contains(constructor.getDeclaringClass())) {
			addConstructors(constructor.getDeclaringClass());
			addFields(constructor.getDeclaringClass(), instance);
			markClassAsAdded(constructor.getDeclaringClass());
			pm.applyConfiguration();
		}

		/* apply the configuration if desired: */
		if (autoConfigure) {
			getConfigurationForObject(instance);
		}

		return instance;
	}

	/**
	 * Applies the current configuration to the given object. If no configuration has been set for a specific field, its
	 * default value will be used. If no default value is applicable, a runtime exception might occur, if setting am
	 * empty string is not permitted.
	 * 
	 * @param o
	 *            The object to apply the configuration to.
	 */
	public <T> void getConfigurationForObject(T o) {
		@SuppressWarnings("unchecked")
		Class<T> c = (Class<T>) o.getClass();

		if (!knownClasses.contains(c)) {
			/* let's be merciful to people, who forgot to add */
			addConfigureable(o);
		}

		getConfiguration(o, c);
	}

	public void getConfigurationForClass(Class<?> c) {
		if (!knownClasses.contains(c)) {
			/* let's be merciful to people, who forgot to add */
			addConfigureable(c, false);
		}

		getConfiguration(null, c);
	}

	/**
	 * Applies the configuration to all fields, as far as they are marked with the {@link FieldDescription} annotation.
	 * 
	 * @param o
	 *            The object to apply the current values to.
	 * @param c
	 *            The class object, from which to take the
	 */
	private void getConfiguration(Object o, Class<?> c) {
		if (c.getSuperclass() != null) {
			getConfiguration(o, c.getSuperclass());
		}

		for (Field f : c.getDeclaredFields()) {
			if (pm.hasField(f)) {
				try {
					f.setAccessible(true);

					if (Modifier.isStatic(f.getModifiers())) {
						f.set(o, pm.getValueFor(f));
					} else if (o != null && pm.hasValueFor(f)) {
						f.set(o, pm.getValueFor(f));
					} else {
						System.err
								.println("net.noratargo.siJACK.Configurator.applyConfiguration(Object, Class<?>) skipping field \""
										+ f.toGenericString()
										+ "\" because there is no instance available, to that this field's current value could be applied to.");
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
