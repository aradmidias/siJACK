package net.noratargo.siJACK;

import net.noratargo.siJACK.testClasses.ParitalConstructorClass;

import org.junit.Test;
import static org.junit.Assert.*;


public class TestParitalConstructor {

	@Test
	public void testConstruction() {
		ConfigurationStorage cs = new ConfigurationStorage(":", new InstantiatorStorage());
		Configurator c = new Configurator(cs);
		
		c.addConfigureable(ParitalConstructorClass.class, false);
		
		ParitalConstructorClass pcc = c.createNewInstance(ParitalConstructorClass.class, true, cs);
		
		assertTrue(pcc.cm == cs);
		assertEquals(245, pcc.i);
		assertEquals("Lorem ipsum", pcc.s);
	}
	

	@Test
	public void testConstructionWithoutAddingToConfigureables() {
		ConfigurationStorage cs = new ConfigurationStorage(":", new InstantiatorStorage());
		Configurator c = new Configurator(cs);

		ParitalConstructorClass pcc = c.createNewInstance(ParitalConstructorClass.class, true, cs);
		
		assertTrue(pcc.cm == cs);
	}
	

	@Test
	public void testConstructionWithMultiplePossibleParameters() {
		ConfigurationStorage cs = new ConfigurationStorage(":", new InstantiatorStorage());
		Configurator c = new Configurator(cs);

		ParitalConstructorClass pcc = c.createNewInstance(ParitalConstructorClass.class, true, "hallo", cs, 123);
		
		assertTrue(pcc.cm == cs);
	}
	
	
}
