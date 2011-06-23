package net.noratargo.siJACK;

import net.noratargo.siJACK.annotationHelper.ParameterDescriptionHelper;
import net.noratargo.siJACK.annotations.ParameterDescription;
import net.noratargo.siJACK.interfaces.InstantiatorManager;

import java.lang.reflect.Field;
import java.util.Set;

public class Parameter<T> {

	/**
	 * Stores the class, that is declaring this object.
	 */
	private final Class<?> declaringClassName;

	/**
	 * Stores the name of the field, whose value should be applied by {@link #apply(Object)}
	 */
	private final String fieldName;

	/**
	 * Holds the classtype of this field.
	 */
	private final Class<T> fieldType;

	/**
	 * Holds all prefix-parametername pairs.
	 */
	private final Set<ParameterPrefixNamePair> parameterNames;

	/**
	 * Holds the default value. This acts as a basis for new instances, but is never returned as
	 */
	private final T defaultValue;

	/**
	 * 
	 */
	private T currentValue;

	private final String description;

	private final String defaultParameterPrefix;

	private final String defaultParameterName;

	private final InstantiatorManager im;

	/**
	 * @param f
	 *            the field to represent.
	 * @param o
	 *            The object instance to use for determinating the default value.
	 * @param classPrefixes
	 *            a list of all class-based prefixes.
	 */
	@SuppressWarnings("unchecked")
	public Parameter(Field f, Object o, InstantiatorManager im) {
		/* enshure, that we may do a lot of fine stuff with this field: */
		f.setAccessible(true);

		/* set a few values, since they are easy to obtain: */
		declaringClassName = f.getDeclaringClass();
		fieldName = f.getName();
		fieldType = (Class<T>) f.getType();
		this.im = im;

		/* we now have at least one prefix. Now let's get the Describing annotation: */
		ParameterDescription paramDescription = f.getAnnotation(ParameterDescription.class);
		description = ParameterDescriptionHelper.getDescription(paramDescription);

		/*
		 * if f is not static and o is null, the @ParameterDescription annotation MUST have a defaultValue Section. In
		 * this case, we NEED TO use an apropriate PaarameterInstanciator-Implementation to instantiate the actual
		 * default object. Otherwise, the defautl value is null and a warning will be printed out.
		 */

		/* try to determine the default value: */
		defaultValue = ParameterDescriptionHelper.getDefaultValue(f, o, paramDescription, im);

		/* create the prefix and name pairs for this Parameter (and also try to obtain the default prefix and name): */

		/* obtain the current Field's Prefix-annotation: */
		ParameterPrefixNamePair defaultPrefixNamePair = ParameterDescriptionHelper.getDefaultPrefixNamePair(paramDescription, f);
		defaultParameterPrefix = defaultPrefixNamePair.getPrefix();
		defaultParameterName = defaultPrefixNamePair.getName();

		parameterNames = ParameterDescriptionHelper.getParameterPrefixNamePairSet(paramDescription, f);

		/* now we have got a default value, so we can use it as it is: */
		currentValue = defaultValue;
	}

	/**
	 * Applies the {@link #currentValue} to the given field of the given object.
	 * 
	 * @param f
	 *            The field to apply the value to
	 * @param o
	 *            The object to apply the value to
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public void apply(Field f, Object o) throws IllegalArgumentException, IllegalAccessException {
		f.setAccessible(true);
		f.set(o, im.getNewInstanceFrom(currentValue));
	}

	@Override
	public int hashCode() {
		return declaringClassName.hashCode() + fieldName.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Parameter<?>) {
			Parameter<?> p = (Parameter<?>) obj;

			return declaringClassName.equals(p.declaringClassName) && fieldName.equals(p.fieldName);
		}

		return false;
	}

	public Class<?> getDeclaringClassName() {
		return this.declaringClassName;
	}

	public Object getCurrentValue() {
		return this.currentValue;
	}

	public Object getDefaultValue() {
		return this.defaultValue;
	}

	public String getDescription() {
		return this.description;
	}

	public String getFieldName() {
		return this.fieldName;
	}

	public Class<?> getFieldType() {
		return this.fieldType;
	}

	public Set<ParameterPrefixNamePair> getParameterNames() {
		return this.parameterNames;
	}

	public String getDefaultParameterName() {
		return this.defaultParameterName;
	}

	public String getDefaultParameterPrefix() {
		return this.defaultParameterPrefix;
	}

	public void setCurrentValue(T currentValue) {
		this.currentValue = currentValue;
	}

	/**
	 * Sets the (new) current value for this property. In contrast to {@link #setCurrentValue(Object)} this version trys
	 * to create the actual object out of the
	 * 
	 * @param value
	 */
	public void setCurrentValueAsString(String value) {
		currentValue = im.getNewInstanceFor(fieldType, value);
	}
}
