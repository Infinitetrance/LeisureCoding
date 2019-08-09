package root.remoteConsole;
import java.io.File;

public class Dir
{

	public static void main(String[] args)
	{
		if (args.length == 0 || args.length > 1)
		{
			System.out.println("Usage: java Dir DirectoryPath");
			return;
		}

		String directoryPathString = args[0];

		File folder = new File(directoryPathString);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++)
		{
			if (listOfFiles[i].isFile())
			{
				System.out.println("File: " + listOfFiles[i].getName());
			} else if (listOfFiles[i].isDirectory())
			{
				System.out.println("Directory: " + listOfFiles[i].getName());
			}
		}
	}

}
