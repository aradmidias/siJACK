package net.noratargo.applicationFramework;

import static org.junit.Assert.*;
import net.noratargo.applicationFramework.testClasses.ParameterApplyingTestClass;

import org.junit.Test;

public class TestParameterApplyling {

	@Test
	public final void testAlternativeParameterNames() {
		ConfigurationStorage cs = new ConfigurationStorage(":");
		Configurator c = new Configurator(cs);
		c.addConfigureable(ParameterApplyingTestClass.class);

		cs.setParameter("net.noratargo.applicationFramework.testClasses.ParameterApplyingTestClass:c", "java.lang.Long");
		cs.setParameter("net.noratargo.applicationFramework.testClasses.ParameterApplyingTestClass:longValue", "357951");
		cs.setParameter("net.noratargo.applicationFramework.testClasses.ParameterApplyingTestClass:intValue", "24685");

		ParameterApplyingTestClass patc = new ParameterApplyingTestClass();

		c.applyConfiguration(patc);
		assertEquals(Long.class, patc.c);
		assertEquals(357951L, patc.longValue);
		assertEquals(24685, patc.intValue);
	}

}
