package fr.lteconsulting.mvnrun;

import java.io.IOException;

import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.MavenResolvedArtifact;

/**
 * Hello world!
 */
public class App
{
	public static void main( String[] args )
	{
		if(args==null||args.length!=2)
		{
			System.out.println("Usage:\r\njava -jar mvnrun.jar groupId:artifactId:version main-class-fqn");
			return;
		}
		
		String artifact = args[0];
		String mainClassFqn = null;
		if(args.length>=2)
			mainClassFqn = args[1];

		System.out.println( "Resolving artifacts..." );

		StringBuilder sb = new StringBuilder();
		String sep = "";

		MavenResolvedArtifact[] resolveds = Maven.resolver().resolve( artifact ).withTransitivity().asResolvedArtifact();
		for( MavenResolvedArtifact ra : resolveds )
		{
			sb.append( sep );
			sep = ";";

			String path = ra.asFile().getAbsolutePath();
			sb.append( path );
			// System.out.println( path );
		}

		String commandLine = "java -cp " + sb.toString() + " " + (mainClassFqn!=null?mainClassFqn:"");
		
		//System.out.println( commandLine );
		
		try
		{
			Process process = Runtime.getRuntime().exec(commandLine);
			System.out.println("OK process is launched !");
			process.waitFor();
		}
		catch( IOException e )
		{
			e.printStackTrace();
		}
		catch( InterruptedException e )
		{
			e.printStackTrace();
		}
	}
}
