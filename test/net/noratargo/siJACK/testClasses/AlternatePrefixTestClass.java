package net.noratargo.siJACK.testClasses;


import net.noratargo.siJACK.Configurator;
import net.noratargo.siJACK.annotations.Description;
import net.noratargo.siJACK.annotations.Name;
import net.noratargo.siJACK.annotations.Prefix;

import java.io.File;

@Prefix("some.other.package.path")
public class AlternatePrefixTestClass {

	@Name(value = "dateiname")
	@Description("the filename's description")
	private String filename = "geheim.dat";

	@Description("Use this username, if no other name has been specified. The usage of names like admin or root is discouraged!")
	private String username = "nobody";

	private String noParam = "stupid";

	@Prefix({ "current preix", "very alternative!" })
	@Description("This parameter has been moved here from a different location.")
	private String alternatePrefixParam;

	// @Parameter("/home/aradmidias/")
	private File file;

	@Override
	public String toString() {
		return "filename: " + filename + " username: " + username + " noParam: " + noParam + " alternatePrefixParam: "
				+ alternatePrefixParam + " file: " + file;
	}

	public AlternatePrefixTestClass(Configurator cfg) {
		cfg.getConfigurationForObject(this);
	}

	public AlternatePrefixTestClass() {
		// TODO Auto-generated constructor stub
	}
	
	public String getAlternatePrefixParam() {
		return this.alternatePrefixParam;
	}
	
	public File getFile() {
		return this.file;
	}
	
	public String getFilename() {
		return this.filename;
	}
	
	public String getNoParam() {
		return this.noParam;
	}
	
	public String getUsername() {
		return this.username;
	}
}
