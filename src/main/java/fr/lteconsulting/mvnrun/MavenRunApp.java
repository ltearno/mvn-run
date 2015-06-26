package fr.lteconsulting.mvnrun;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.MavenResolvedArtifact;

/**
 * This is the main entry point of the maven run application.
 * 
 * MaenRun allows to run any artifact from the command line. Downloading the
 * artifact and its runtime dependencies are MavenRun's job !
 */
public class MavenRunApp
{
	public static void main( String[] args )
	{
		if( args == null || args.length < 1 )
		{
			System.out.println( "Usage:\r\njava -jar mvnrun.jar groupId:artifactId:version parameters" );
			return;
		}

		System.out.println( "Resolving artifacts..." );

		StringBuilder sb = new StringBuilder();
		String sep = "";

		String artifact = args[0];
		MavenResolvedArtifact[] resolveds = Maven.resolver().resolve( artifact ).withTransitivity().asResolvedArtifact();
		for( MavenResolvedArtifact ra : resolveds )
		{
			sb.append( sep );
			sep = ";";

			String path = ra.asFile().getAbsolutePath();
			sb.append( path );
		}

		File jarFile = Maven.resolver().resolve( artifact ).withoutTransitivity().asSingleFile();
		if( jarFile == null )
		{
			System.out.println( "Unable to find a jar file to launch for artifact " + artifact + ". Aborting." );
			return;
		}

		String commandLine = "java -cp " + sb.toString() + " " + " -jar " + jarFile.getAbsolutePath();
		for( int i = 1; i < args.length; i++ )
			commandLine += " " + args[i];

		System.out.println( "Command line : " + commandLine );

		try
		{
			Process process = Runtime.getRuntime().exec( commandLine );

			System.out.println( "" );
			System.out.println( "Process launched." );

			InputStream stdin = process.getInputStream();
			BufferedReader reader = new BufferedReader( new InputStreamReader( stdin ) );

			InputStream stderr = process.getErrorStream();
			BufferedReader reader2 = new BufferedReader( new InputStreamReader( stderr ) );

			String line, lineErr = null;
			do
			{
				line = reader.readLine();
				lineErr = reader2.readLine();
				if( line == null && lineErr == null )
					break;
				if( line != null )
					System.out.println( line );
				if( lineErr != null )
					System.err.println( lineErr );
			}
			while( true );

			System.out.println( "Process terminated." );
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
}
