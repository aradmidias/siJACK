package net.noratargo.applicationFramework.testClasses;

import net.noratargo.applicationFramework.annotations.ParameterDescription;

public class ParameterApplyingTestClass {

	@ParameterDescription
	public Class<Long> c = Long.class;

	@ParameterDescription
	public long longValue = 10L;

	@ParameterDescription
	public int intValue = 234123;
}
