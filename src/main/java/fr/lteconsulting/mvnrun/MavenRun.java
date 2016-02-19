package fr.lteconsulting.mvnrun;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.MavenResolvedArtifact;
import org.jboss.shrinkwrap.resolver.api.maven.MavenResolverSystem;

public class MavenRun
{
	private static MavenResolverSystem resolver()
	{
		return Maven.resolver();
	}

	private static String join(List<String> list, String inside)
	{
		StringBuilder sb = new StringBuilder();
		if (list != null)
		{
			boolean add = false;
			for (String item : list)
			{
				if (add)
					sb.append(inside);
				else
					add = true;
				sb.append(item);
			}
		}
		return sb.toString();
	}

	public static void run(String artifact, String[] args, String className)
	{
		System.out.println("Resolving artifacts...");

		MavenResolverSystem resolver = resolver();

		List<String> classPath = new ArrayList<>();

		MavenResolvedArtifact[] resolveds = resolver.resolve(artifact).withTransitivity().asResolvedArtifact();
		for (MavenResolvedArtifact ra : resolveds)
			classPath.add(ra.asFile().getAbsolutePath());

		File jarFile = resolver.resolve(artifact).withoutTransitivity().asSingleFile();
		if (jarFile == null)
		{
			System.out.println("Unable to find a jar file to launch for artifact " + artifact + ". Aborting.");
			return;
		}

		String commandLine = "";
		if (className == null)
		{
			commandLine = "java -cp " + join(classPath, ";") + " " + " -jar " + jarFile.getAbsolutePath();
		}
		else
		{
			classPath.add(jarFile.getAbsolutePath());
			commandLine = "java -cp " + join(classPath, ";") + " " + " " + className;
		}

		for (int i = 0; i < args.length; i++)
			commandLine += " " + args[i];

		System.out.println("Command line : " + commandLine);

		try
		{
			System.out.println("");
			System.out.println("Launching process...");

			Process process = Runtime.getRuntime().exec(commandLine);

			System.out.println("");
			System.out.println("Process launched.");

			InputStream stdin = process.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(stdin));

			InputStream stderr = process.getErrorStream();
			BufferedReader reader2 = new BufferedReader(new InputStreamReader(stderr));

			String line, lineErr = null;
			do
			{
				line = reader.readLine();
				lineErr = reader2.readLine();
				if (line == null && lineErr == null)
					break;
				if (line != null)
					System.out.println(line);
				if (lineErr != null)
					System.err.println(lineErr);
			}
			while (true);

			System.out.println("Process terminated.");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
