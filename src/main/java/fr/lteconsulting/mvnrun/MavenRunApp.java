package fr.lteconsulting.mvnrun;

/**
 * This is the main entry point of the maven run application.
 * 
 * MaenRun allows to run any artifact from the command line. Downloading the artifact and its runtime dependencies are
 * MavenRun's job !
 */
public class MavenRunApp
{
	public static void main(String[] args)
	{
		if (args == null || args.length < 1)
		{
			System.out.println("Usage:\r\njava -jar mvnrun.jar groupId:artifactId:version parameters");
			return;
		}

		String artifact = args[0];
		String[] arguments = new String[args.length - 1];
		for (int i = 1; i < args.length; i++)
			arguments[i - 1] = args[i];

		MavenRun.run(artifact, arguments, null);
	}
}
