package net.noratargo.siJACK;

import net.noratargo.siJACK.annotations.ParameterDescription;
import net.noratargo.siJACK.interfaces.ConfigurationManager;
import net.noratargo.siJACK.interfaces.InstantiatorManager;
import net.noratargo.siJACK.interfaces.ParameterManager;
import net.noratargo.siJACK.util.DoubleHashMap;

import java.lang.reflect.Constructor;
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
	private final DoubleHashMap<Class<?>, String, FieldDetails<?>> cfParameters;

	/**
	 * Stores all parameters, grouped by prefix and name. This is used for setting values.
	 */
	private final DoubleHashMap<String, String, FieldDetails<?>> pnParameters;

	private final ArrayList<PrefixNameValueStorage> unsetValues;
	
	private final InstantiatorManager im;

	public ConfigurationStorage(String seperator, InstantiatorManager im) {
		this.seperator = seperator;
		this.im = im;
		
		cfParameters = new DoubleHashMap<Class<?>, String, FieldDetails<?>>();
		pnParameters = new DoubleHashMap<String, String, FieldDetails<?>>();
		unsetValues = new ArrayList<PrefixNameValueStorage>();
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
	public <T> void setParameter(String prefix, String name, String value) {
		@SuppressWarnings("unchecked")
		FieldDetails<T> parameter = (FieldDetails<T>) pnParameters.get(prefix, name);

		if (parameter == null) {
			/* store this change in a list... */
			unsetValues.add(new PrefixNameValueStorage(prefix, name, value));
		} else {
			/* Configure the parameter: */
			parameter.setCurrentValue(im.getNewInstanceFor(parameter.getFieldType(), value));
		}
	}

	@Override
	public <T> void applyConfiguration() {
		Iterator<PrefixNameValueStorage> iterator = unsetValues.iterator();

		while (iterator.hasNext()) {
			PrefixNameValueStorage unsetValue = iterator.next();
			
			@SuppressWarnings("unchecked")
			FieldDetails<T> parameter = (FieldDetails<T>) pnParameters.get(unsetValue.prefix, unsetValue.name);

			if (parameter != null) {
				/* Configure the parameter and remove the unsetValue (sinc eit now is set): */
				parameter.setCurrentValue(im.getNewInstanceFor(parameter.getFieldType(), unsetValue.value));
				iterator.remove();
			}
		}
	}
	
	@Override
	public Object getValueFor(Field f) {
		FieldDetails<?> p = cfParameters.get(f.getDeclaringClass(), f.getName());
		
		if (p == null) {
			throw new NoSuchElementException("The given field is no known parameter: "+ f);
		}
		
		return im.getNewInstanceFrom(p.getCurrentValue());
	}
	
	@Override
	public Object[] getValuesFor(Constructor<?> c) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Set<FieldDetails<?>> getParameters() {
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

	@Override
	public <T> void addField(Field f, T defaultValue) {
		if (f.getAnnotation(ParameterDescription.class) != null) {
			FieldDetails<T> p = new FieldDetails<T>(f, defaultValue, im);
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

	@Override
	public <T> void addConstructor(Constructor<T> constr) {
		// TODO Auto-generated method stub
		
	}
}
