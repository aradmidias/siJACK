package net.noratargo.applicationFramework;

import static org.junit.Assert.*;
import net.noratargo.applicationFramework.testClasses.ParametrisedClass;

import org.junit.Test;

public class TestSuperclassAutoInitialisation {

	@Test
	public final void testConfigurationStorage() {
		ConfigurationStorage cs = new ConfigurationStorage(":");
		Configurator c = new Configurator(cs);

		c.addConfigureable(new ParametrisedClass());

		cs.setParameter("net.noratargo.applicationFramework.testClasses.AbstractParametrisedClass", "s", "new value for a field of an uninstanciable class.");
		cs.setParameter("net.noratargo.applicationFramework.testClasses.ParametrisedClass", "subString", "new value for a field of an instanciable class.");
		ParametrisedClass vstc = new ParametrisedClass();

		c.applyConfiguration(vstc);

		for (Parameter<?> p : cs.getParameters()) {
			String s = p.getDefaultParameterPrefix() + cs.getPrefixNameSeperator() + p.getDefaultParameterName();
			System.out.println("TestSuperclassAutoInitialisation.testConfigurationStorage() c["+ s +"]  = "+ p.getCurrentValue());
			System.out.println("TestSuperclassAutoInitialisation.testConfigurationStorage() c["+ s +"] := "+ p.getDefaultValue());
			
			for (ParameterPrefixNamePair ppnp : p.getParameterNames()) {
				System.out.println("TestSuperclassAutoInitialisation.testConfigurationStorage() c["+ s +"] available as: "+ ppnp.getPrefix() + cs.getPrefixNameSeperator() + ppnp.getName());
			}
		}
		
		assertEquals("new value for a field of an uninstanciable class.", vstc.s);
		assertEquals("new value for a field of an instanciable class.", vstc.subString);
	}

}
