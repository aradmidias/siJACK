package net.noratargo.siJACK;

import net.noratargo.siJACK.annotationHelper.FieldDescriptionHelper;
import net.noratargo.siJACK.annotations.FieldDescription;
import net.noratargo.siJACK.interfaces.InstantiatorManager;

import java.lang.reflect.Field;
import java.util.Set;

public class FieldDetails<T> {

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

	/**
	 * @param f
	 *            the field to represent.
	 * @param o
	 *            The object instance to use for determinating the default value.
	 * @param classPrefixes
	 *            a list of all class-based prefixes.
	 */
	@SuppressWarnings("unchecked")
	public FieldDetails(Field f, T givenDefaultValue, InstantiatorManager im) {
		/* enshure, that we may do a lot of fine stuff with this field: */
		f.setAccessible(true);

		/* set a few values, since they are easy to obtain: */
		declaringClassName = f.getDeclaringClass();
		fieldName = f.getName();
		fieldType = (Class<T>) f.getType();

		/* we now have at least one prefix. Now let's get the Describing annotation: */
		FieldDescription paramDescription = f.getAnnotation(FieldDescription.class);
		description = FieldDescriptionHelper.getDescription(paramDescription);

		/*
		 * if f is not static and o is null, the @ParameterDescription annotation MUST have a defaultValue Section. In
		 * this case, we NEED TO use an apropriate PaarameterInstanciator-Implementation to instantiate the actual
		 * default object. Otherwise, the defautl value is null and a warning will be printed out.
		 */

		/* try to determine the default value: */
		defaultValue = FieldDescriptionHelper.getDefaultValue(f, givenDefaultValue, paramDescription, im);

		/* create the prefix and name pairs for this Parameter (and also try to obtain the default prefix and name): */

		/* obtain the current Field's Prefix-annotation: */
		ParameterPrefixNamePair defaultPrefixNamePair = FieldDescriptionHelper.getDefaultPrefixNamePair(paramDescription, f);
		defaultParameterPrefix = defaultPrefixNamePair.getPrefix();
		defaultParameterName = defaultPrefixNamePair.getName();

		parameterNames = FieldDescriptionHelper.getParameterPrefixNamePairSet(paramDescription, f);

		/* now we have got a default value, so we can use it as it is: */
		currentValue = defaultValue;
	}

	@Override
	public int hashCode() {
		return declaringClassName.hashCode() + fieldName.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FieldDetails<?>) {
			FieldDetails<?> p = (FieldDetails<?>) obj;

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

	public Class<T> getFieldType() {
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
}
