package net.noratargo.siJACK;

import java.util.Set;

public class Parameter<T> {

	private T defaultValue;
	private T currentValue;
	private Class<T> valueClassType;
	private Set<ParameterPrefixNamePair> parameterPrefixPairs;
	private String defaultPrefix;
	private String defaultName;
	private String description;

	public Parameter(T defaultValue, Class<T> valueClassType, Set<ParameterPrefixNamePair> parameterPrefixPairs, String defaultPrefix,
			String defaultName, String description) {
		this.defaultValue = defaultValue;
		this.currentValue = defaultValue;
		this.valueClassType = valueClassType;
		this.parameterPrefixPairs = parameterPrefixPairs;
		this.defaultPrefix = defaultPrefix;
		this.defaultName = defaultName;
		this.description = description;
	}

	public T getDefaultValue() {
		return defaultValue;
	}

	public T getCurrentValue() {
		return currentValue;
	}

	public Class<T> getValueClassType() {
		return valueClassType;
	}
	
	public void setCurrentValue(T currentValue) {
		this.currentValue = currentValue;
	}

	public Set<ParameterPrefixNamePair> getParameterPrefixNamePairs() {
		return parameterPrefixPairs;
	}

	public String getDefaultPrefix() {
		return defaultPrefix;
	}

	public String getDefaultName() {
		return defaultName;
	}

	public String getDescription() {
		return description;
	}

	//TODO: think about, when two parameters are equal!!! Or can an equality never be achieved?
}
