package net.noratargo.applicationFramework;

import static org.junit.Assert.*;
import net.noratargo.applicationFramework.testClasses.AlternatePrefixTestClass;
import net.noratargo.applicationFramework.testClasses.ValueSettingTestClass;

import org.junit.Test;

public class TestInstantiation {

	@Test
	public final void testConfigurationStorage() {
		ConfigurationStorage cs = new ConfigurationStorage(":");
		Configurator c = new Configurator(cs);

		c.addConfigureable(new ValueSettingTestClass());

		cs.setParameter("net.noratargo.applicationFramework.testClasses.ValueSettingTestClass", "name", "admin");
		ValueSettingTestClass vstc = new ValueSettingTestClass();

		c.applyConfiguration(vstc);

		assertEquals("admin", vstc.name);
	}

	@Test
	public final void testConfigurationBeforeAddingConfigureable() {
		ConfigurationStorage cs = new ConfigurationStorage(":");
		Configurator c = new Configurator(cs);

		cs.setParameter("net.noratargo.applicationFramework.testClasses.ValueSettingTestClass", "name", "admin");
		c.addConfigureable(new ValueSettingTestClass());
		ValueSettingTestClass vstc = new ValueSettingTestClass();

		c.applyConfiguration(vstc);

		assertEquals("admin", vstc.name);
	}

	@Test
	public final void testAlternativeParameterNames() {
		ConfigurationStorage cs = new ConfigurationStorage(":");
		Configurator c = new Configurator(cs);
		cs.setParameter("current preix:alternatePrefixParam", "prefix_");

		c.addConfigureable(AlternatePrefixTestClass.class);

		cs.setParameter("some.other.package.path:dateiname", "/etc/fstab");
		cs.setParameter("some.other.package.path:username", "root");

		AlternatePrefixTestClass iwp = new AlternatePrefixTestClass();

		c.applyConfiguration(iwp);
		assertEquals("prefix_", iwp.getAlternatePrefixParam());
		assertEquals("/etc/fstab", iwp.getFilename());
		assertEquals("root", iwp.getUsername());
//		assertEquals("", iwp.getAlternatePrefixParam());
//		assertEquals("", iwp.getAlternatePrefixParam());
//		assertEquals("", iwp.getAlternatePrefixParam());
	}

}
