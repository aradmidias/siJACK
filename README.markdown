siJACK - simple Java Application Configuraton Framework
=======================================================

## What is it?

This is a small framework, that can be used to configure java based applications by annotating the fields, that should be configureable.

The idea behind this is the ability to make a application configureable with a minimum effort. I did not want neither a command line interpreter-dependency, nor a configuration-file dependency. I wanted a pure, Application-based configuration system, that can be used in conjunction with Commandline Interpreters (CLI) and Configuration File Interpreters.

While the default classes will make you independant from 
In most cases, you will need to add a command line interpreter to use this too properly.

## How do I use it?

* Annotate the fields, you want to configure:

		class Sample {
			@ParamterDescription("This string describes, what this setting does")
			private Stirng iAmConfigureable = "this is my default value";
		}

* make all configureable clases known (choose one out of the following three):
 	This is necessary to tell the configurator which classes can be configured and what their default value and their description are.
	*	add a consturctor having the `Configureable` class as a parameter:
	
			class Sample {
				@ParamterDescription("This string describes, what this setting does")
				priate Stirng iAmConfigureable = "this is my default value";
				
				public Sample(Configureable cfg) {
					cfg.addConfigureable(this);
				}
			}
	
	*	Add the class to the Configureable from the outside:
	
				public void someMethod(Configurator cfg) {
					cfg.addConfigureable(this);
				}
		
	*	Use the static constructor and provide a parameterless constructor, if possible:
	
			class Sample {
			 	static {
			  		/* this constructor will be called when the class has been loaded - so it is executed only once. */
			  		SomeClassThatHoldsTheConfiguratorInstance.getConfigurator().addConfigureable(Sample.class);
			  	}
			  	
			  	private Sample() {
			  		/*
			  		 * This constructor does nothing, but creating an instance, setting up the default values.
			  		 * If you can - for whatever reason - not spcify a default constructor, you can specify the
			  		 * default value in the @ParameterDescription-Annotation. (See below)
			  		 */
		 	 	}
			}
	
* Applying the current configuration to a configureable Object:
	*	by using a constructor:
	
			class Sample {
				@ParamterDescription("This string describes, what this setting does")
				priate Stirng iAmConfigureable = "this is my default value";
				
				public Sample(Configureable cfg) {
					cfg.applyConfiguration(this);
				}
			}
  
	*	by applying the values from outside:
	
				public vid someMethod(Configurator cfg) {
					Sample sampleClassInstance = new Smaple();
					cfg.applyConfiguration(sampleClassInstance);
					/* now do something with you sample class */
				}

* Create a Configurator that can be used for adding and configuring the configureable objects:

			public void someOtherMethod() {
				/* We use the colon as a seperator between prefix and field name. I'll talk about the prefix a bit later. */
				
				/* Note, that the ConfigurationStorage Class implements both interfaces: ParameterManager and ConfigurationManager. */
				ConfigurationStorage cs = new ConfigurationStorage(":");
				Configurator cfg = new Configurator(cs);
			}

## Prefix-Annotation

By default, the field is equal to the classes full-qualified name.

If you want to change the prefix, you can do this by using the @Prefix annotation. It can be used to annotate either classes or fields. If both are annotated, only the field's @Prefix-Annotation will be used (it overwrites the Prefix-Settings, defined by the class).

TODO: document prefix usage.

## ParameterDescription-Annotation

TODO: describe the ParameterDescription-Annotation usage.

## ParameterName-Annotation

TODO: describe the ParameterName-Annotation usage.

## Failsafes

* Adding a class multiple times via `Configurator.addConfigureable(...)` does not result in an error.
* Adding a class multiple times via `Configurator.addConfigureable(...)` automatically adds all super-classes.
* If a field should be configured, that is not know, yet, its value-assignment will be added to an internal list. Everytime, a new configureable class is added, this list is iterated and the ConfigurationStorage class tries to apply these (currently not applied) values. This way, configurations can be set, even if the appropriate classes/fields are not known.

## Upcoming (planned) features

* A classloader or another mechanism to automatically add all configureable classes to a predefined Configurator
* Adding Methods, that allow to set parameters to specified object, rather then to String-values only. (This will still need the instanciators).

