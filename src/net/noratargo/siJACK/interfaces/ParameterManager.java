package net.noratargo.siJACK.interfaces;

import net.noratargo.siJACK.Configurator;
import net.noratargo.siJACK.Parameter;
import net.noratargo.siJACK.annotations.ParameterDescription;

import java.lang.reflect.Field;

/**
 * The ParameterManager holds all Parameters, that can be configured.
 * 
 * 
 * The configuration Storage holds all known Configurations and provides ways to apply values to parameters, that are
 * not known at that time.
 * <p>
 * if you are adding parametert, you HAVE TO call {@link #applyConfiguration()} when you are done. Otherwise, the
 * 
 * @author HMulthaupt
 */
public interface ParameterManager {

	/**
	 * Adds all Fields of the given class, that are annotated with {@link ParameterDescription} to the list of
	 * parameters available for configiration, if this class has not already been added to the ParameterManager.
	 * <p>
	 * Values, that might have been set, <b>MUST NOT</b> be reset!
	 * 
	 * @param c
	 *            The class-Object to use.
	 */
	public <T> void addClass(Class<T> c, Object o);

	/**
	 * Applies the current configuration to all {@link Parameter} objects.
	 * <p>
	 * This is supposed to be used for parameters, to which a value has been assigned to, but that have not yet been
	 * added by the {@link #addParameter(Parameter)} method.
	 */
	public void applyConfiguration();

	/**
	 * Sets the value of the given field to the same that the corresponding {@link Parameter} has got.
	 * <p>
	 * 
	 * @see Parameter#apply(Field, Object)
	 * @param f
	 * @param o
	 */
	public void configureObject(Object o, Class<?> c, Configurator cfg);
}
