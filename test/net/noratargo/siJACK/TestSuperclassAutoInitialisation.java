package net.noratargo.siJACK;

import static org.junit.Assert.*;
import net.noratargo.siJACK.interfaces.InstantiatorManager;
import net.noratargo.siJACK.testClasses.AbstractParametrisedClass;
import net.noratargo.siJACK.testClasses.ParametrisedClass;

import org.junit.Test;

import javax.naming.ConfigurationException;

public class TestSuperclassAutoInitialisation {

	@Test
	public final void testConfigurationStorage() throws ConfigurationException {
		InstantiatorManager im = new InstantiatorStorage();
		ConfigurationStorage cs = new ConfigurationStorage(":", im);
		Configurator c = new Configurator(cs);

		c.addConfigureable(new ParametrisedClass());

		cs.setValue(AbstractParametrisedClass.class.getName(), "s", "new value for a field of an uninstanciable class.");
		cs.setValue(ParametrisedClass.class.getName(), "subString", "new value for a field of an instanciable class.");
		ParametrisedClass vstc = new ParametrisedClass();

		c.getConfigurationForObject(vstc);

//		for (Orig_FieldDetails<?> p : cs.getParameters()) {
//			String s = p.getDefaultParameterPrefix() + cs.getPrefixNameSeperator() + p.getDefaultParameterName();
//			System.out.println("TestSuperclassAutoInitialisation.testConfigurationStorage() c["+ s +"]  = "+ p.getCurrentValue());
//			System.out.println("TestSuperclassAutoInitialisation.testConfigurationStorage() c["+ s +"] := "+ p.getDefaultValue());
			
//			for (ParameterPrefixNamePair ppnp : p.getParameterNames()) {
//				System.out.println("TestSuperclassAutoInitialisation.testConfigurationStorage() c["+ s +"] available as: "+ ppnp.getPrefix() + cs.getPrefixNameSeperator() + ppnp.getName());
//			}
//		}
		
		assertEquals("new value for a field of an uninstanciable class.", vstc.s);
		assertEquals("new value for a field of an instanciable class.", vstc.subString);
	}

}
