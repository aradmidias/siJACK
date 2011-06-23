package net.noratargo.siJACK.interfaces;

import net.noratargo.siJACK.Configurator;
import net.noratargo.siJACK.Parameter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

/**
 * The ParameterManager holds all Parameters (Fields as well as parameters for constructors), that can be configured.
 * From the {@link Configurator}'s Point of view, the implementing class is responsible for a) managing the specified
 * fields and constructors and b) providing the current values for requested field or constructor.
 * <p>
 * if you are adding parameter, you HAVE TO call {@link #applyConfiguration()} when you are done. Otherwise, the
 * 
 * @author HMulthaupt
 */
public interface ParameterManager {

	/**
	 * @param <T>
	 * @param f
	 *            The field to add.
	 * @param defaultValue
	 *            This field's default value, if it could be obtained. May be <code>null</code>, if for whatever reason
	 *            the default value's determination resulted in <code>null</code>.
	 */
	public <T> void addField(Field f, T defaultValue);

	/**
	 * @param <T>
	 * @param constr
	 */
	public <T> void addConstructor(Constructor<T> constr);

	/**
	 * Applies the current configuration to all {@link Parameter} objects.
	 * <p>
	 * This is supposed to be used for parameters, to which a value has been assigned to, but that have not yet been
	 * added by the {@link #addParameter(Parameter)} method.
	 */
	public <T> void applyConfiguration();

	/**
	 * Returns the vlue for the given field.
	 * 
	 * @param f The field 
	 * @return
	 */
	public Object getValueFor(Field f);

	/**
	 * Returns all known parameters for the given constructor.
	 * 
	 * @param c
	 * @return
	 */
	public Object[] getValuesFor(Constructor<?> c);
}
