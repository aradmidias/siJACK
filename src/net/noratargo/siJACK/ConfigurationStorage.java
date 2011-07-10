package net.noratargo.siJACK;

import net.noratargo.siJACK.annotationHelper.AnnotationHelper;
import net.noratargo.siJACK.annotations.DefaultConstructor;
import net.noratargo.siJACK.annotations.DefaultValue;
import net.noratargo.siJACK.annotations.Description;
import net.noratargo.siJACK.annotations.Name;
import net.noratargo.siJACK.annotations.PartialConstructor;
import net.noratargo.siJACK.annotations.Prefix;
import net.noratargo.siJACK.interfaces.ConfigurationManager;
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

public class ConfigurationStorage implements ParameterManager, ConfigurationManager {

	private final String seperator;

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

	public ConfigurationStorage(String seperator, InstantiatorManager im) {
		this.seperator = seperator;
		this.im = im;

		fpParameters = new HashMap<Field, Parameter<?>>();
		cpParameters = new HashMap<Constructor<?>, Parameter<?>[]>();
		cDefaultConstructors = new HashMap<Class<?>, Constructor<?>>();
		cPartialConstructors = new HashMap<Class<?>, Constructor<?>>();
		pnParameters = new DoubleHashMap<String, String, Set<Parameter<?>>>();
		unsetValues = new ArrayList<PrefixNameValueStorage>();
	}

	@Override
	public void setValue(String name, String value) {
		int index = name.indexOf(seperator);

		if (index == -1) {
			/* no seperator, therefor no prefix present */
			setValue("", name, value);
		} else if (index == 0) {
			/* the seperatoris at the beginnig - so still no prefix */
			setValue("", name, value);
		} else if (index + seperator.length() >= name.length()) {
			/* no key present */
			// TODO think about throwing an exception instead!?
			return;
		} else {
			/* We have both, prefix and key. */
			setValue(name.substring(0, index), name.substring(index + seperator.length(), name.length()), value);
		}
	}

	@Override
	public <T> void setValue(String prefix, String name, String value) {
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

	@Override
	public Object getValueFor(Field f) {
		Parameter<?> p = fpParameters.get(f);

		if (p == null) {
			throw new NoSuchElementException("The given field is no known parameter: " + f);
		}

		return im.getNewInstanceFrom(p.getCurrentValue());
	}

	@Override
	public String getPrefixNameSeperator() {
		return seperator;
	}

	@Override
	public <T> void addField(Field f, T defaultValue) {
		if (!fpParameters.containsKey(f)) {
			Parameter<T> p = AnnotationHelper.createParameter(f, defaultValue, im);

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
				
				
				
				cpParameters.put(constr, parameters);

				for (Parameter<?> p : parameters) {
					if (p != null) {
						addPNParameters(p);
					}
				}
			}
		}
	}
	
	/**
	 * Adds the given parameter to all its ParameterPreferixNamePairs.
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
	public <T> Constructor<T> getParitalConstructor(Class<T> c) {
		return (Constructor<T>) (cPartialConstructors.containsKey(c) ? cPartialConstructors.get(c) : null);
	}
	
	@Override
	public Object[] getValuesFor(Constructor<?> c) {
		Parameter<?>[] pList = cpParameters.get(c);
		Object[] o = null;
		
		//TODO: find out, when and why this can be null!
		if (pList != null) {
			o = new Object[pList.length];
			int i = 0;
			
			for (Parameter<?> p : pList) {
				o[i] = p == null ? null : im.getNewInstanceFrom(p.getCurrentValue());
				i++;
			}
		} else {
			o = new Object[0];
		}
		
		return o;
	}
	
	@Override
	public Object[] getValuesFor(Constructor<?> c, Object... parameters) {
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
			
			newValues[i] = (isAnnotated && values[i] != null) ? values[i] : getCompatibleValueFor(c.getParameterTypes()[i], parameters);
			
			i++;
		}
		
		return newValues;
	}
	
	/**
	 * 
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


