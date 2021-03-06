package net.noratargo.siJACK.interfaces;

import net.noratargo.siJACK.Configurator;
import net.noratargo.siJACK.annotations.DefaultConstructor;
import net.noratargo.siJACK.annotations.DefaultValue;

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
	 * Adds the specified field to the Collection of known fields. This variation is for adding fields, where no default
	 * value could be read from the field itself. The field may still have a {@link DefaultValue} annotation.
	 * <p>
	 * If the field's value is accessible use {@link #addField(Field, Object)} (even if the field's value is <code>null</code>)
	 * <p>
	 * An implementing class has to check the previously mentioned annotation and use it - if necessary - to determine
	 * the default value.
	 * <p>
	 * A field may be revoked, if it does not match certain criterias (e.g. it has already been added, or it is not
	 * annotated with a {@link FieldDescription} annotation).
	 * <p>
	 * TODO: think about what to do, if a field is being revoked.
	 * 
	 * @param <T>
	 *            The type of the default value.
	 * @param defaultValue
	 *            This field's default value, if it could be obtained. May be <code>null</code>, if for whatever reason
	 *            the default value's determination resulted in <code>null</code>.
	 */
	public <T> void addField(Field f);

	/**
	 * Adds the specified field to the Collection of known fields. The currentValue is the value, that the field
	 * currently carries. If it is inaccessible, use {@link #addFIeld(Field)}
	 * <p>
	 * An implementing method (not the calling one!) has to check the {@link DefaultValue} annotation and use it - if necessary - to determine
	 * the default value.
	 * <p>
	 * A field may be revoked, if it does not match certain criterias (e.g. it has already been added, or it is not
	 * annotated with a {@link FieldDescription} annotation).
	 * <p>
	 * TODO: think about what to do, if a field is being revoked.
	 * 
	 * @param <T>
	 *            The type of the default value.
	 * @param f
	 *            The field to add.
	 * @param currentValue
	 *            This field's current value. It might be used as deault value, if no {@link DefaultValue} annotation is present. If it could not determined, use {@link #addFIeld(Field)}.
	 *            <code>null</code> is allowed, if the field's default value is <code>null</code>.
	 */
	public <T> void addField(Field f, T currentValue);

	/**
	 * Adds the specified construcotr to the Collection of known constructors.
	 * <p>
	 * A constructor may be revoked, if it does not match certain criterias (e.g. it has already been added, or it is
	 * not annotated with a {@link ConstructorDescription} annotation).
	 * <p>
	 * TODO: think about what to do, if a constructor is being revoked.
	 * 
	 * @param <T>
	 *            The type of the default value.
	 * @param constructor
	 *            The constructor to add.
	 */
	public <T> void addConstructor(Constructor<T> constructor);

	/**
	 * Tells the implementation, that configurations, that could not yet applied to (previously unknown) fields or
	 * constructors should now be probed again.
	 * <p>
	 * If the implementation does not support settings values for unknown fields or constructors, then this method
	 * simply does nothing.
	 */
	public <T> void applyConfiguration();

	/**
	 * Returns the vlue for the given field.
	 * <p>
	 * If the given field has not yet been added to this manager, the implementation <b>should</b> add it to its
	 * internal list of configureable fields and call {@link #applyConfiguration()}. If the implementation does not
	 * whish to add the field to its internal collection, then it has to thrwo a {@link RuntimeException}.
	 * <p>
	 * If the given Field has no {@link FieldDescription} annotation, a RuntimeExceptions has to be thrown.
	 * 
	 * @param f
	 *            The field for that the configuration should be returned.
	 * @return A new instance of the object, that is set as the current value for the given field. <code>null</code> may
	 *         be returned, if that is the current value.
	 * @throws RuntimeException
	 *             if the given field is not annotated with the {@link FieldDescription} annotation.
	 */
	public <T> T getValueFor(Field f) throws RuntimeException;

	/**
	 * Returns all known parameters for the given constructor.
	 * <p>
	 * If the constructor has not yet been aded to this nanager, the implementation <b>should</b> add it to its internal
	 * list of configureable constructors and call {@link #applyConfiguration()}. If the implementation does not whish
	 * to add the constructor to its internal collection, then it has to thrwo a {@link RuntimeException}.
	 * <p>
	 * If the constructor is not Annotated with a {@link ConstructorDescription}, a RuntimeException has to be thrown.
	 * 
	 * @param c
	 *            The field for that the configuration should be returned.
	 * @return An array of new instances for the given constructor. The number of parameters must match the number of
	 *         parameters of the given constructor. If the constructor has not parameters, an empty array has to be
	 *         returned. Any element in the returned array may be <code>null</code>, if that is the current default
	 *         value for the given Parameter.
	 * @throws RuntimeException
	 *             if the given constructor is not annotated with a {@link ConstructorDescription} annotation.
	 */
	public <T> Object[] getValuesFor(Constructor<?> c);

	/**
	 * Returns all known parameters for the given constructor, merging the given parameters into the default value of
	 * the given constructor.
	 * <p>
	 * If the constructor has not yet been aded to this nanager, the implementation <b>should</b> add it to its internal
	 * list of configureable constructors and call {@link #applyConfiguration()}. If the implementation does not whish
	 * to add the constructor to its internal collection, then it has to thrwo a {@link RuntimeException}.
	 * <p>
	 * If the constructor is not Annotated with a {@link ConstructorDescription}, a RuntimeException has to be thrown.
	 * 
	 * @param c
	 *            The field for that the configuration should be returned.
	 * @return An array of new instances for the given constructor. The number of parameters must match the number of
	 *         parameters of the given constructor. If the constructor has not parameters, an empty array has to be
	 *         returned. Any element in the returned array may be <code>null</code>, if that is the current default
	 *         value for the given Parameter.
	 * @throws RuntimeException
	 *             if the given constructor is not annotated with a {@link ConstructorDescription} annotation.
	 */
	public <T> Object[] getValuesFor(Constructor<?> c, Object... o);

	/**
	 * Returns the default constructor or <code>null</code> of the specified class object.
	 * 
	 * @param <T>
	 *            The type that the constructor will create.
	 * @param c
	 *            The class to obtain the default constructor form.
	 * @return The default constructor or <code>null</code> if there is no constructor marked with the
	 *         {@link DefaultConstructor} annotation.
	 */
	public <T> Constructor<T> getDefaultConstructor(Class<T> c);

	/**
	 * Returns the parital constructor of the specified class object, or <code>null</code> if there is no parital
	 * constructor.
	 * 
	 * @param <T>
	 *            The type that the constructor will create.
	 * @param c
	 *            The class to obtain the default constructor form.
	 * @return The default constructor or <code>null</code> if there is no constructor marked with the
	 *         {@link DefaultConstructor} annotation.
	 */
	public <T> Constructor<T> getPartialConstructor(Class<T> c);

	/**
	 * Returns <code>true</code>, if the given Field is known and a value can be returned for it - even if that value
	 * will then be <code>null</code>
	 * 
	 * @param f
	 * @return
	 */
	public boolean hasField(Field f);

	/**
	 * returns <code>true</code> if this configuration manager has a value for this field.
	 * 
	 * @param f
	 * @return
	 */
	public boolean hasValueFor(Field f);

}
