package net.noratargo.siJACK.deprecated;
//package net.noratargo.applicationFramework;
//
//
//import java.lang.reflect.Field;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Iterator;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//import java.util.NoSuchElementException;
//import java.util.Set;
//
//public class GroupingConfigurationStorage implements ParameterManager, ConfigurationManager {
//
//	/** THis is the String, being used to seperate prefix and key from each other (if they were given in one string). */
//	private final String prefixKeySeperator;
//
//	/** Stores all parameters, that have been added to this Storage. */
//	private final Set<Parameter> uniqueParameters;
//
//	/** Provides a mapping to the actual parameter object - regardless, how many prefix and/or key aliases it has. */
//	private final Map<String, HashMap<String, Parameter>> indexedParameters;
//
//	/** Stores the configuration for parameters, that have not yet been added. */
//	private final List<Parameter> undefinedParameters;
//
//	public GroupingConfigurationStorage(String prefixKeySeperator) {
//		this.prefixKeySeperator = prefixKeySeperator;
//
//		uniqueParameters = new HashSet<Parameter>();
//		indexedParameters = new HashMap<String, HashMap<String, Parameter>>();
//
//		/*
//		 * A linked list is used since there might be a lot of append-at-the-end and remove-somewhere-in-the-middle
//		 * operations. Therefore an ArrayList might produce too much overhead
//		 */
//		undefinedParameters = new LinkedList<Parameter>();
//	}
//
//
//	@Override
//	public void addParameter(Parameter p) {
//		// TODO Auto-generated method stub
//		
//		HashMap<String, Parameter> map = indexedParameters.get(prefix);
//
//		if (map == null) {
//			map = new HashMap<String, Parameter>();
//			indexedParameters.put(prefix, map);
//		}
//
//		if (map.containsKey(key)) {
//			throw new RuntimeException(
//					"There are two parameters, that are adressed under the same prefix and key. This must be avoided!");
//		}
//
//		map.put(key, pi);
//	}
//
//	@Override
//	public void applyConfiguration() {
//		Iterator<Parameter> iterator = undefinedParameters.iterator();
//
//		while (iterator.hasNext()) {
//			Parameter param = iterator.next();
//			HashMap<String, Parameter> map = indexedParameters.get(param.defaultPrefix);
//
//			if (map != null) {
//				Parameter actualParam = map.get(param.defaultKey);
//
//				if (actualParam != null) {
//					actualParam.actualValue = param.actualValue;
//					iterator.remove();
//				}
//			}
//		}
//	}
//
//	@Override
//	public void configureField(Field f, Object o, Configurator cfg) {
//		// TODO Auto-generated method stub
//		
//	}
//	
//	@Override
//	public void setParameter(String key, String value) {
//		int index = key.indexOf(prefixKeySeperator);
//
//		if (index == -1) {
//			/* no seperator, therefor no prefix present */
//			setParameter("", key, value);
//		} else if (index == 0) {
//			/* the seperatoris at the beginnig - so still no prefix */
//			setParameter("", key, value);
//		} else if (index + prefixKeySeperator.length() >= key.length()) {
//			/* no key present */
//			// TODO think about throwing an exception instead!?
//			return;
//		} else {
//			/* We have both, prefix and key. */
//			setParameter(key.substring(0, index), key.substring(index + prefixKeySeperator.length(), key.length()),
//					value);
//		}
//	}
//
//	@Override
//	public void setParameter(String prefix, String key, String value) {
//		if (value == null) {
//			throw new IllegalArgumentException("null is not permitted as value. Use \"\" as alternative.");
//		}
//		
//		HashMap<String,Parameter> map = indexedParameters.get(prefix);
//		if (map == null) {
//			/* no parameter added, to the curent value to. */
//			undefinedParameters.add(new ParameterInformation("", "", value, key, prefix));
//		} else {
//			Parameter pi = map.get(key);
//			
//			if (pi == null) {
//				/* no parameter added, to the curent value to. */
//				undefinedParameters.add(new ParameterInformation("", "", value, key, prefix));
//			} else {
//				pi.actualValue = value;
//			}
//		}
//	}
//
////	public String getParameter(String key) {
////		int index = key.indexOf(prefixKeySeperator);
////
////		if (index == -1) {
////			/* no seperator, therefor no prefix present */
////			return getParameter("", key);
////		} else if (index == 0) {
////			/* the seperatoris at the beginnig - so still no prefix */
////			return getParameter("", key);
////		} else if (index + prefixKeySeperator.length() >= key.length()) {
////			/* no key present */
////			// TODO think about throwing an exception instead!?
////			return "";
////		} else {
////			/* We have both, prefix and key. */
////			return getParameter(key.substring(0, index - 1),
////					key.substring(index + prefixKeySeperator.length(), key.length()));
////		}
////	}
////
////	/**
////	 * Returns the value for the addressed parameter.
////	 * 
////	 * @param prefix
////	 * @param key
////	 * @return
////	 * @throws NoSuchElementException if there is no parameter under the given key or value pair.
////	 */
////	public String getParameter(String prefix, String key) {
////		HashMap<String,Parameter<?>> map = indexedParameters.get(prefix);
////		
////		if (map == null) {
////			throw new NoSuchElementException("There is no element with the prefix "+ prefix);
////		}
////		
////		Parameter<?> pi = map.get(key);
////		
////		if (pi == null) {
////			throw new NoSuchElementException("There is no parameter with the name "+ key +" to be found unter the prefix "+ prefix);
////		}
////		
////		return pi.actualValue;
////	}
//
//	@Override
//	public String getPrefixNameSeperator() {
//		return prefixKeySeperator;
//	}
//	
//}
