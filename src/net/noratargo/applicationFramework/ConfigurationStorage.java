package net.noratargo.applicationFramework;

import net.noratargo.applicationFramework.annotations.ParameterDescription;
import net.noratargo.applicationFramework.interfaces.ConfigurationManager;
import net.noratargo.applicationFramework.interfaces.ParameterManager;
import net.noratargo.applicationFramework.util.DoubleHashMap;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class ConfigurationStorage implements ParameterManager, ConfigurationManager {

	private final String seperator;

	/**
	 * Stores all parameters, grouped by declaring class and the field's name.
	 */
	private final DoubleHashMap<Class<?>, String, Parameter<?>> cfParameters;

	/**
	 * Stores all parameters, grouped by prefix and name. This is used for setting values.
	 */
	private final DoubleHashMap<String, String, Parameter<?>> pnParameters;

	private final ArrayList<PrefixNameValueStorage> unsetValues;

	public ConfigurationStorage(String seperator) {
		this.seperator = seperator;

		cfParameters = new DoubleHashMap<Class<?>, String, Parameter<?>>();
		pnParameters = new DoubleHashMap<String, String, Parameter<?>>();
		unsetValues = new ArrayList<PrefixNameValueStorage>();
	}

	@Override
	public <T> void addClass(Class<T> c, Object o) {
		if (c.getSuperclass() != null) {
			addClass(c.getSuperclass(), o);
		}
		
		if (!cfParameters.hasSuperKey(c)) {
			for (Field f : c.getDeclaredFields()) {
				if (f.getAnnotation(ParameterDescription.class) != null) {
					//TODO: this might crash!
					Parameter<T> p = new Parameter<T>(f, o);
					if (!cfParameters.contains(p.getDeclaringClassName(), p.getFieldName())) {
						/* only insert the Parameter, if it is not added, yet. */
						cfParameters.put(p.getDeclaringClassName(), p.getFieldName(), p);

						for (ParameterPrefixNamePair pn : p.getParameterNames()) {
							if (pnParameters.contains(pn.getPrefix(), pn.getName())) {
								throw new RuntimeException();
							}

							pnParameters.put(pn.getPrefix(), pn.getName(), p);
						}
					}
				}
			}
		}
	}

	@Override
	public void setParameter(String name, String value) {
		int index = name.indexOf(seperator);

		if (index == -1) {
			/* no seperator, therefor no prefix present */
			setParameter("", name, value);
		} else if (index == 0) {
			/* the seperatoris at the beginnig - so still no prefix */
			setParameter("", name, value);
		} else if (index + seperator.length() >= name.length()) {
			/* no key present */
			// TODO think about throwing an exception instead!?
			return;
		} else {
			/* We have both, prefix and key. */
			setParameter(name.substring(0, index), name.substring(index + seperator.length(), name.length()), value);
		}
	}

	@Override
	public void setParameter(String prefix, String name, String value) {
		Parameter<?> parameter = pnParameters.get(prefix, name);

		if (parameter == null) {
			/* store this change in a list... */
			unsetValues.add(new PrefixNameValueStorage(prefix, name, value));
		} else {
			/* Configure the parameter: */
			parameter.setCurrentValueAsString(value);
		}
	}

	@Override
	public void configureObject(Object o, Class<?> c, Configurator cfg) {
		if (c.getSuperclass() != null) {
			configureObject(o, c.getSuperclass(), cfg);
		}
		
		for (Field f : c.getDeclaredFields()) {
			if (f.getAnnotation(ParameterDescription.class) != null) {
				Parameter<?> parameter = cfParameters.get(f.getDeclaringClass(), f.getName());

				if (parameter == null) {
					throw new NoSuchElementException(
							"There is no field defined for configuration. This field should be configured: "
									+ f.getDeclaringClass() + "." + f.getName());
				}

				try {
					parameter.apply(f, o);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void applyConfiguration() {
		Iterator<PrefixNameValueStorage> iterator = unsetValues.iterator();

		while (iterator.hasNext()) {
			PrefixNameValueStorage unsetValue = iterator.next();
			Parameter<?> parameter = pnParameters.get(unsetValue.prefix, unsetValue.name);

			if (parameter != null) {
				/* Configure the parameter and remove the unsetValue (sinc eit now is set): */
				parameter.setCurrentValueAsString(unsetValue.value);
				iterator.remove();
			}
		}
	}

	@Override
	public Set<Parameter<?>> getParameters() {
		return cfParameters.getValueSet();
	}

	@Override
	public String getPrefixNameSeperator() {
		return seperator;
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
