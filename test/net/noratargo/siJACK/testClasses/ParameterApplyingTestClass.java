package net.noratargo.siJACK.testClasses;

import net.noratargo.siJACK.annotations.FieldDescription;

public class ParameterApplyingTestClass {

	@FieldDescription
	public Class<Long> c = Long.class;

	@FieldDescription
	public long longValue = 10L;

	@FieldDescription
	public int intValue = 234123;
}
