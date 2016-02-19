# mvn-run

Runs maven artifacts from the command line

## Usage :

There are two ways using mvnrun.

### Using the `mvn-run` mojo plugin

You can type that in any directory (which will become the current directory of the launched application) :

	mvn fr.lteconsulting:mvnrun-maven-plugin:1.0:run   \
		-Dmvnrun.artifact=groupId:artifactId:version   \
		-Dmvnrun.classname=classToLaunchFqn            \
		-Dmvnrun.parameters=hello

The previous command will :

- download and launch the `mvn-run` plugin, which will :
- download the artifact specified in the `mvnrun.artifact` maven parameter,
- download all this artifact's runtime dependencies,
- launch either the class specified in the `mvnrun.classname` maven parameter or launch the previous artifact as an executable jar if the `mvnrun.classname` is not specified.
- in every situation, is `mvnrun.parameters` is set, it will be passed as the launched application command line parameters.

This command will only run is maven is installed on your machine. It will use your local maven settings and configuration.

### Using the `mvn-run` java application

You can also run `mvn-run` as a standalone Java application.

First you need to download `mvnrun.jar` (**at this moment**, there is no downlink provided so you'll have to build it with a classic `mvn clean install`).

Then you can run a maven artifact with this :

	java -jar mvnrun.jar ARTIFACT_TO_LAUNCH [OPTIONS]

on Windows, you can type

	mvnrun ARTIFACT_TO_RUN [OPTIONS]

