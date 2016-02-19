package fr.lteconsulting.mvnrun;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.settings.Settings;

@Mojo(name = "run",
		requiresDirectInvocation = true,
		requiresProject = false)
public class MavenRunMojo extends AbstractMojo
{
	@Parameter(property = "mvnrun.artifact")
	private String artifact;

	@Parameter(property = "mvnrun.classname")
	private String className;

	@Parameter(property = "mvnrun.parameters")
	private String parameters;

	@Parameter(property = "mvnrun.settings", defaultValue = "${settings}")
	private Settings settings;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException
	{
		try
		{
			getLog()
					.info("Maven Run execution plugin\r\n" + artifact + "\r\n" + settings + "\r\n"
							+ settings.getClass().getName());

			if (artifact == null || artifact.isEmpty())
			{
				System.out
						.println("Usage:\r\nmvn fr.lteconsulting:mvnrun-maven-plugin:1.0-SNAPSHOT:run -Dmvnrun.artifact=fr.lteconsulting:pom-explorer:1.1-SNAPSHOT -Dmvnrun.classname=fr.lteconsulting.pomexplorer.PomExporerApp -Dmvnrun.parameters=hello");
				return;
			}

			String[] arguments = parameters != null ? parameters.split(" ") : new String[] {};

			MavenRun.run(artifact, arguments, className);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
