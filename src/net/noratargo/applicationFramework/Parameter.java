package net.noratargo.applicationFramework;

import net.noratargo.applicationFramework.annotations.ParameterDescription;
import net.noratargo.applicationFramework.annotations.ParameterName;
import net.noratargo.applicationFramework.annotations.Prefix;
import net.noratargo.applicationFramework.interfaces.ParameterInstanciator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
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
	private final String defaultValueString;

	/**
	 * 
	 */
	private T currentValue;
	
	private String currentValueString;

	private final String description;

	private final String defaultParameterPrefix;

	private final String defaultParameterName;

	/**
	 * @param f
	 *            the field to represent.
	 * @param classPrefixes
	 *            a list of all class-based prefixes.
	 */
	public Parameter(Field f) {
		this(f, null);
	}

	@SuppressWarnings("unchecked")
	public Parameter(Field f, Object o) {
		/* enshure, that we may do a lot of fine stuff with this field: */
		f.setAccessible(true);

		/* set a few values, since they are easy to obtain: */
		declaringClassName = f.getDeclaringClass();
		fieldName = f.getName();
		fieldType = (Class<T>) f.getType();
		parameterNames = new HashSet<ParameterPrefixNamePair>();

		/* we now have at least one prefix. Now let's get the Describing annotation: */
		ParameterDescription paramDescription = f.getAnnotation(ParameterDescription.class);
		description = paramDescription.value().equals("") ? paramDescription.description() : paramDescription.value();

		/*
		 * if f is not static and o is null, the @ParameterDescription annotation MUST have a defaultValue Section. In
		 * this case, we NEED TO use an apropriate PaarameterInstanciator-Implementation to instantiate the actual
		 * default object. Otherwise, the defautl value is null and a warning will be printed out.
		 */

		/* try to determine the default value: */
		T v = null;
		try {
			if (o == null && !Modifier.isStatic(f.getModifiers())) {
				/*
				 * we can not access a non-static field with a null-object. So we try to create one (if this does not
				 * work, we can continue outside of this try-catch-block, since we could not obtain the value by this
				 * way, anyway):
				 */
				o = declaringClassName.newInstance();
			}

			v = (T) f.get(o);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}

		if (v == null) {
			/* look for a default value inside the Annotation: */
			String value = paramDescription.defaultValue();

			if ("".equals(value)) {
				/* it is posible, that we should use an empty string as default value. */
				if (paramDescription.doNotComplainAboutDefaultParameter()) {
					System.err
							.println("Warning: Maybe there is no explicit default value set for field: "
									+ declaringClassName.getName()
									+ "."
									+ fieldName
									+ " I will try to continue using "
									+ (paramDescription.isDefaultValueNull() ? "null" : "an empty String\"\"")
									+ " instead. "
									+ "If you want to have null as default, set the isDefaultValueNull parameter to true. If you do not want to see this message again, set doNotComplainAboutNullParameter to true!");
				}

				value = paramDescription.isDefaultValueNull() ? null : "";
			}

			defaultValueString = value;
			v = Configurator.getInstanciator(fieldType).createNewInstance(value, null);
		} else {
			defaultValueString = null;
		}

		defaultValue = v;

		/* create the prefix and name pairs for this Parameter (and also try to obtain the default prefix and name): */

		/* obtain the current Field's Prefix-annotation: */
		Prefix prefix = f.getAnnotation(Prefix.class);
		if (prefix == null) {
			/* look for a prefix at the defining class: */
			prefix = declaringClassName.getAnnotation(Prefix.class);

			if (prefix == null) {
				prefix = createPrefix(new String[] {}, 0, true);
			}
		}
		
		ParameterName[] index = paramDescription.index();
		int defaultIndex = paramDescription.defaultParameterNameIndex();
		
		/* identify the default prefix and name: */
		if (defaultIndex == -1) {
			/* no defautl prefix and/or name: */
			defaultParameterPrefix = "";
			defaultParameterName = "";
		} else if (defaultIndex >= index.length || defaultIndex < -1) {
			throw new IndexOutOfBoundsException("the index you specified is either too small or too large. There is no element with the index "+ defaultIndex +"\n"+
					"You must choose from a value IN [-1, "+ (index.length - 1) +"] for "+ declaringClassName +"."+ fieldName);
		} else {
			ParameterName i = index[defaultIndex];
			
			/* try to finde the prefix: */
			String def = getDefaultPrefix(i.prefix(), declaringClassName);
			
			/* if there is no default prefix given (def==null) look for a prefix in the field-prefix: */
			def = (def == null && i.applyFieldPrefix() ? getDefaultPrefix(prefix, declaringClassName) : def);
			defaultParameterPrefix = (def == null ? "" : def);
			
			/* try to find the name: */
			defaultParameterName = getDefaultName(i, f);
		}
		
		/* create the prefix-name-set: */
		for (ParameterName pn : paramDescription.index()) {
			fillParameterNames(pn, prefix);
		}
		
		
		/* now we have got a default value, so we can use it as it is: */
		currentValue = defaultValue;
		currentValueString = defaultValueString;
	}
	
	private void fillParameterNames(ParameterName pn, Prefix p) {
		/* stores all collected prefixes for later combindings. */
		HashSet<String> prefixes = new HashSet<String>();

		/* stores all collected names for later combindings. */
		HashSet<String> names = new HashSet<String>();
		
		/* collect all prefixes: */
		if (pn.applyFieldPrefix()) {
			addPrefixesToSet(prefixes, p);
		}
		
		addPrefixesToSet(prefixes, pn.prefix());
		
		/* collect all names: */
		for (String name : pn.value()) {
			names.add(name);
		}
		
		if (pn.addFieldNameToValues()) {
			names.add(fieldName);
		}
		
		/* Create the pairing and add them to the parameterNames set: */
		for (String prefix : prefixes) {
			for (String name : names) {
				parameterNames.add(new ParameterPrefixNamePair(prefix, name));
			}
		}
	}
	
	private void addPrefixesToSet(Set<String> s, Prefix p) {
		for (String prefix : p.value()) {
			s.add(prefix);
		}
		
		if (p.addFullQualifiedClassNameToPrefixList()) {
			s.add(declaringClassName.getName());
		}
	}
	
	/**
	 * 
	 * @param p
	 * @param c
	 * @return <code>null</code> if no default prefix is given, or could not be determined, else a {@link String} is returned.
	 */
	private String getDefaultPrefix(Prefix p, Class<?> c) {
		int defaultPrefixIndex = p.defaultValue();
		
		if (defaultPrefixIndex == -1) {
			return null;
		} else if (defaultPrefixIndex < -1 || (p.addFullQualifiedClassNameToPrefixList() ? defaultPrefixIndex > p.value().length : defaultPrefixIndex >= p.value().length)) {
			throw new IndexOutOfBoundsException("the index you specified is either too small or too large. There is no element with the index "+ defaultPrefixIndex +"\n"+
					"You must choose from a value IN [-1, "+ (p.value().length - (p.addFullQualifiedClassNameToPrefixList() ? 0 : 1)) +"] for "+ declaringClassName +"."+ fieldName);
		} else {
			return defaultPrefixIndex == p.value().length ? c.getName() : p.value()[defaultPrefixIndex];
		}
	}

	/**
	 * @param pm
	 * @param c
	 * @return The default name for the given field. If the returned Sting is an empty String (""), then there is no
	 *         default name.
	 */
	private String getDefaultName(ParameterName pm, Field f) {
		int defaultNameIndex = pm.defaultValue();

		if (defaultNameIndex == -1) {
			return "";
		} else if (defaultNameIndex < -1
				|| (pm.addFieldNameToValues() ? defaultNameIndex > pm.value().length : defaultNameIndex >= pm.value().length)) {
			throw new IndexOutOfBoundsException(
					"the index you specified is either too small or too large. There is no element with the index "
							+ defaultNameIndex + "\n" + "You must choose from a value IN [-1, "
							+ (pm.value().length - (pm.addFieldNameToValues() ? 0 : 1)) + "] for "+ declaringClassName +"."+ fieldName);
		} else {
			return defaultNameIndex == pm.value().length ? f.getName() : pm.value()[defaultNameIndex];
		}
	}

	private Prefix createPrefix(final String[] value, final int defaultValue, final boolean add) {
		return new Prefix() {

			@Override
			public Class<? extends Annotation> annotationType() {
				return null;
			}

			@Override
			public String[] value() {
				return value;
			}

			@Override
			public int defaultValue() {
				return defaultValue;
			}

			@Override
			public boolean addFullQualifiedClassNameToPrefixList() {
				return add;
			}
		};
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
		ParameterInstanciator<T> instanciator = Configurator.getInstanciator(fieldType);
		f.set(o, instanciator.createNewInstance(currentValueString, currentValue));
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

	public String getDefaultValueString() {
		return this.defaultValueString;
	}

	public void setCurrentValue(T currentValue) {
		if (currentValue == null) {
			//note: this does no make sense: the currentValue can be null, if defaultValue is null, but we do not allow null-values to be set!? Why not???
			throw new NullPointerException("The value you try to set must not be null!");
		}

		this.currentValue = currentValue;
		this.currentValueString = null;
	}
	
	/**
	 * Sets the (new) current value for this property. In contrast to {@link #setCurrentValue(Object)} this version trys to create the actual object out of the 
	 * @param value
	 */
	public void setCurrentValueAsString(String value) {
		currentValue = Configurator.getInstanciator(fieldType).createNewInstance(value, null);
		currentValueString = value;
	}
}
