package net.noratargo.siJACK;

import net.noratargo.siJACK.annotationHelper.AnnotationHelper;
import net.noratargo.siJACK.annotations.DefaultConstructor;
import net.noratargo.siJACK.annotations.DefaultValue;
import net.noratargo.siJACK.annotations.Description;
import net.noratargo.siJACK.annotations.Name;
import net.noratargo.siJACK.annotations.PartialConstructor;
import net.noratargo.siJACK.annotations.Prefix;
import net.noratargo.siJACK.interfaces.ConfigurationManager;
import net.noratargo.siJACK.interfaces.ImmutableConstructor;
import net.noratargo.siJACK.interfaces.ImmutableParameter;
import net.noratargo.siJACK.interfaces.InstantiatorManager;
import net.noratargo.siJACK.interfaces.ParameterManager;
import net.noratargo.siJACK.util.DoubleHashMap;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.naming.ConfigurationException;

public class ConfigurationStorage implements ParameterManager, ConfigurationManager {

	private final String prefixNameSeperator;

	private final String nameValueSeperator;

	/**
	 * Stores all parameters, grouped by declaring class and the field's name.
	 */
	private final HashMap<Field, Parameter<?>> fpParameters;

	private final HashMap<Constructor<?>, Parameter<?>[]> cpParameters;

	private final HashMap<Class<?>, Constructor<?>> cDefaultConstructors;

	private final HashMap<Class<?>, Constructor<?>> cPartialConstructors;

	/**
	 * Stores all parameters, grouped by prefix and name. This is used for setting values.
	 */
	private final DoubleHashMap<String, String, Set<Parameter<?>>> pnParameters;

	private final ArrayList<PrefixNameValueStorage> unsetValues;

	private final InstantiatorManager im;

	public ConfigurationStorage(InstantiatorManager im) {
		this(":", "=", im);
	}

	public ConfigurationStorage(String prefixNameSeperator, InstantiatorManager im) {
		this(prefixNameSeperator, "=", im);
	}

	public ConfigurationStorage(String prefixNameSeperator, String nameValueSeperator, InstantiatorManager im) {
		assert prefixNameSeperator != null : "The parameter prefixNameSeperator must not be null!";
		assert nameValueSeperator != null : "The parameter nameValueSeperator must not be null!";
		assert im != null : "The parameter im must not be null!";

		this.prefixNameSeperator = prefixNameSeperator;
		this.nameValueSeperator = nameValueSeperator;
		this.im = im;

		fpParameters = new HashMap<Field, Parameter<?>>();
		cpParameters = new HashMap<Constructor<?>, Parameter<?>[]>();
		cDefaultConstructors = new HashMap<Class<?>, Constructor<?>>();
		cPartialConstructors = new HashMap<Class<?>, Constructor<?>>();
		pnParameters = new DoubleHashMap<String, String, Set<Parameter<?>>>();
		unsetValues = new ArrayList<PrefixNameValueStorage>();
	}

	@Override
	public void setValue(String pns) throws ConfigurationException {
		int prefixNameSeperatorIndex = pns.indexOf(prefixNameSeperator);
		int nameValueSeperatorIndex = pns.indexOf(nameValueSeperator, prefixNameSeperatorIndex);

		if (prefixNameSeperatorIndex == -1) {
			prefixNameSeperatorIndex = 0;
		}

		if (nameValueSeperatorIndex == -1) {
			nameValueSeperatorIndex = prefixNameSeperatorIndex + prefixNameSeperator.length();
		}

		String prefix = pns.substring(0, prefixNameSeperatorIndex);
		String name = pns.substring(prefixNameSeperatorIndex + prefixNameSeperator.length(), nameValueSeperatorIndex);
		String value = pns.substring(nameValueSeperatorIndex + nameValueSeperator.length());

		if (!value.equals("") && !name.equals("")) {
			setValue(prefix, name, value);
		} else {
			throw new ConfigurationException(
					"The name of the property to set, or the value to set (or both) are missing in configuration string: "
							+ pns);
		}
	}

	@Override
	public void setValue(String name, String value) throws ConfigurationException {
		int index = name.indexOf(prefixNameSeperator);

		if (index == -1) {
			/* no seperator, therefor no prefix present */
			setValue("", name, value);
		} else if (index == 0) {
			/* the seperatoris at the beginnig - so still no prefix */
			setValue("", name, value);
		} else if (index + prefixNameSeperator.length() >= name.length()) {
			/* no name present - do nothing here, since in this case, no element can be adressed, by now. */
		} else {
			/* We have both, prefix and key. */
			setValue(name.substring(0, index), name.substring(index + prefixNameSeperator.length(), name.length()),
					value);
		}
	}

	@Override
	public <T> void setValue(String prefix, String name, String value) throws ConfigurationException {
		Set<Parameter<?>> parameters = pnParameters.get(prefix, name);

		if (parameters == null) {
			/* store this change in a list... */
			unsetValues.add(new PrefixNameValueStorage(prefix, name, value));
		} else {
			/* Configure the parameter: */
			for (Parameter<?> param : parameters) {
				@SuppressWarnings("unchecked")
				Parameter<T> p = (Parameter<T>) param;
				p.setCurrentValue(im.getNewInstanceFor(p.getValueClassType(), value));

			}
		}
	}

	@Override
	public <T> void applyConfiguration() {
		Iterator<PrefixNameValueStorage> iterator = unsetValues.iterator();

		while (iterator.hasNext()) {
			PrefixNameValueStorage unsetValue = iterator.next();

			Set<Parameter<?>> parameters = pnParameters.get(unsetValue.prefix, unsetValue.name);

			if (parameters != null) {
				/* Configure the parameter and remove the unsetValue (sinc eit now is set): */
				for (Parameter<?> param : parameters) {
					@SuppressWarnings("unchecked")
					Parameter<T> p = (Parameter<T>) param;
					p.setCurrentValue(im.getNewInstanceFor(p.getValueClassType(), unsetValue.value));

				}
			}
		}
	}

	public Set<ParameterPrefixNamePair> getUnapliedConfiguration() {
		Set<ParameterPrefixNamePair> ppnps = new HashSet<ParameterPrefixNamePair>();

		for (PrefixNameValueStorage pnvs : unsetValues) {
			if (!pnParameters.contains(pnvs.prefix, pnvs.name)) {
				ppnps.add(new ParameterPrefixNamePair(pnvs.prefix, pnvs.name));
			}
		}

		return ppnps;
	}

	@Override
	public <T> T getValueFor(Field f) {
		@SuppressWarnings("unchecked")
		Parameter<T> p = (Parameter<T>) fpParameters.get(f);

		if (p == null) {
			throw new NoSuchElementException("The given field is no known parameter: " + f);
		}

		return im.getNewInstanceFrom(p.getValueClassType(), p.getCurrentValue());
	}

	@Override
	public boolean hasValueFor(Field f) {
		Parameter<?> p = fpParameters.get(f);

		if (p == null) {
			throw new NoSuchElementException("The given field is no known parameter: " + f);
		}

		return p.hasDefaultValue() || p.hasValueChanged();
	}

	@Override
	public String getPrefixNameSeperator() {
		return prefixNameSeperator;
	}

	@Override
	public <T> void addField(Field f) {
		addField(f, null, false);
	}

	@Override
	public <T> void addField(Field f, T defaultValue) {
		addField(f, defaultValue, true);
	}

	private <T> void addField(Field f, T defaultValue, boolean wasFieldAccessable) {
		if (!fpParameters.containsKey(f)) {
			Parameter<T> p = AnnotationHelper.createParameter(f, defaultValue, wasFieldAccessable, im);

			if (p != null) {
				/* only insert the Parameter, if it is not added, yet. */
				fpParameters.put(f, p);

				addPNParameters(p);
			}
		}
	}

	@Override
	public boolean hasField(Field f) {
		return fpParameters.containsKey(f);
	}

	@Override
	public <T> void addConstructor(Constructor<T> constr) {
		if (!cpParameters.containsKey(constr)) {
			Parameter<?>[] parameters = AnnotationHelper.createParametersFromConstructor(constr, im);

			if (parameters != null) {
				cpParameters.put(constr, parameters);

				for (Parameter<?> p : parameters) {
					if (p != null) {
						addPNParameters(p);
					}
				}
			}

			/* Handle default cosntructor */
			DefaultConstructor defConstr = constr.getAnnotation(DefaultConstructor.class);

			if (defConstr != null) {
				if (cDefaultConstructors.containsKey(constr.getDeclaringClass())) {
					throw new RuntimeException("You must not specify multiple default construcotrs for one class!");
				}

				cDefaultConstructors.put(constr.getDeclaringClass(), constr);
			}

			/* handle partial constructor: */
			PartialConstructor pConstr = constr.getAnnotation(PartialConstructor.class);

			if (pConstr != null) {
				if (cPartialConstructors.containsKey(constr.getDeclaringClass())) {
					throw new RuntimeException("You must not specify multiple partial construcotrs for one class!");
				}

				cPartialConstructors.put(constr.getDeclaringClass(), constr);
			}
		}
	}

	/**
	 * Adds the given parameter to all its ParameterPreferixNamePairs.
	 * 
	 * @param p
	 */
	private void addPNParameters(Parameter<?> p) {
		for (ParameterPrefixNamePair pn : p.getParameterPrefixNamePairs()) {
			Set<Parameter<?>> pSet = pnParameters.get(pn.getPrefix(), pn.getName());

			if (pSet == null) {
				pSet = new HashSet<Parameter<?>>();
				pnParameters.put(pn.getPrefix(), pn.getName(), pSet);
			}

			pSet.add(p);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Constructor<T> getDefaultConstructor(Class<T> c) {
		return (Constructor<T>) (cDefaultConstructors.containsKey(c) ? cDefaultConstructors.get(c) : null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Constructor<T> getPartialConstructor(Class<T> c) {
		return (Constructor<T>) (cPartialConstructors.containsKey(c) ? cPartialConstructors.get(c) : null);
	}

	@Override
	public <T> Object[] getValuesFor(Constructor<?> c) {
		Parameter<?>[] pList = cpParameters.get(c);
		Object[] o = null;

		/*
		 * pList can be null, if
		 * net.noratargo.siJACK.annotationHelper.AnnotationHelper.createParametersFromConstructor(Constructor<?>,
		 * InstantiatorManager) returns null.
		 */
		if (pList == null) {
			throw new RuntimeException(
					"You try to instantiate a constructor, that has 1 or more arguments, but it is neither marked as ParitalConstructor, nor has all elements annotated.");
		}

		o = new Object[pList.length];
		int i = 0;

		for (Parameter<?> p : pList) {
			@SuppressWarnings("unchecked")
			Parameter<T> pt = (Parameter<T>) p;
			o[i] = p == null ? null : im.getNewInstanceFrom(pt.getValueClassType(), pt.getCurrentValue());
			i++;
		}

		return o;
	}

	@Override
	public <T> Object[] getValuesFor(Constructor<?> c, Object... parameters) {
		Object[] values = getValuesFor(c);
		Object[] newValues = new Object[values.length];

		/* Apply the given objects (in o) to the leaks in the parameter's constructor: */
		Annotation[][] pAnnotations = c.getParameterAnnotations();
		int i = 0;

		for (Annotation[] annotations : pAnnotations) {
			boolean isAnnotated = false;

			for (Annotation a : annotations) {
				if (a instanceof Name || a instanceof Description || a instanceof DefaultValue || a instanceof Prefix) {
					isAnnotated = true;
				}
			}

			newValues[i] = (isAnnotated && values[i] != null) ? values[i] : getCompatibleValueFor(
					c.getParameterTypes()[i], parameters);

			i++;
		}

		return newValues;
	}

	/**
	 * @param <T>
	 * @param parameterType
	 * @param parameters
	 * @return
	 */
	private Object getCompatibleValueFor(Class<?> parameterType, Object... parameters) {
		int i = 0;

		/* look for an element in o, that is not null, that fits to the given parameter. */
		for (Object param : parameters) {
			if (parameterType.isInstance(param)) {
				Object o = parameters[i];
				parameters[i] = null;
				return o;
			}

			i++;
		}

		return null;
	}

	@Override
	public Set<ImmutableParameter<?>> getFields() {
		return new HashSet<ImmutableParameter<?>>(fpParameters.values());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public <T> Set<ImmutableConstructor<?>> getConstructors() {
		Set<ImmutableConstructor<?>> result = new HashSet<ImmutableConstructor<?>>();

		for (Constructor<?> c : cpParameters.keySet()) {
			result.add(new ConstructorInformation(c, cpParameters.get(c)));
		}

		return result;
	}

	private class PrefixNameValueStorage {
		public final String prefix;
		public final String name;
		public final String value;

		public PrefixNameValueStorage(String prefix, String name, String value) {
			this.prefix = prefix;
			this.name = name;
			this.value = value;
		}
	}

}
