package net.noratargo.siJACK;

import net.noratargo.siJACK.annotations.DefaultConstructor;
import net.noratargo.siJACK.annotations.Description;
import net.noratargo.siJACK.annotations.PartialConstructor;
import net.noratargo.siJACK.interfaces.ImmutableConstructor;
import net.noratargo.siJACK.interfaces.ImmutableParameter;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class ConstructorInformation<T> implements ImmutableConstructor<T> {

	private final String description;
	
	private final Class<T> declaringClass;
	
	private final List<ImmutableParameter<?>> parameters;
	
	private final boolean isPartial;
	
	private final boolean isDefault;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ConstructorInformation(Constructor<T> c, Parameter<?>[] parameters) {
		Description descriptionAnnotation = c.getAnnotation(Description.class);
		description = (descriptionAnnotation == null) ? "" : descriptionAnnotation.value();
		
		declaringClass = c.getDeclaringClass();

		isPartial = (c.getAnnotation(PartialConstructor.class) != null);
		isDefault = (c.getAnnotation(DefaultConstructor.class) != null);
		
		this.parameters = new ArrayList<ImmutableParameter<?>>();
		Class<?>[] signature = c.getParameterTypes();
		
		for (int i = 0; i < signature.length; i++) {
			this.parameters.add(parameters[i] == null ? new Parameter(signature[i]) : parameters[i]);
		}
	}
	
	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public Class<T> getDeclaringClass() {
		return declaringClass;
	}

	@Override
	public List<ImmutableParameter<?>> getParameters() {
		return parameters;
	}

	@Override
	public boolean isParital() {
		return isPartial;
	}

	@Override
	public boolean isDefault() {
		return isDefault;
	}
}
