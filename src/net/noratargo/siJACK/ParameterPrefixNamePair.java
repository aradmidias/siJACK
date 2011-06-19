package net.noratargo.siJACK;

public final class ParameterPrefixNamePair {

	private final String prefix;
	
	private final String name;
	
	public ParameterPrefixNamePair(String prefix, String name) {
		if (prefix == null || name == null) {
			throw new NullPointerException("Neither prefix nor name must be null.");
		}
		
		this.prefix = prefix;
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
	
	public String getPrefix() {
		return this.prefix;
	}
	
	@Override
	public int hashCode() {
		return prefix.hashCode() + name.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ParameterPrefixNamePair) {
			ParameterPrefixNamePair ppnp = (ParameterPrefixNamePair) obj;
			return this == ppnp || prefix.equals(ppnp.prefix) && name.equals(ppnp.name);
		}
		return false;
	}
	
}
