package root.remoteConsole;
import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Executor
{

	public Runtime runTime = Runtime.getRuntime();

	public Process process;

	public Scanner in;
	public PrintWriter out;
	public Scanner err;

	public Exception exception;
	private Path tmpCmdFile;

	private static final String TMP_CMD_FILE_IDENTIFIER = "CMD_ARM9FXNW_8051.bat";

	/**
	 * @param cmd
	 * @param dir
	 * @return launch status of process, check "exception" when returns false
	 */
	public boolean execute(String cmd, String dir)
	{
		exception = null;
		try
		{
			if (dir != null)
				process = runTime.exec(cmd, null, new File(dir));
			else
				process = runTime.exec(cmd);

			in = new Scanner(process.getInputStream());
			out = new PrintWriter(process.getOutputStream(), true);
			err = new Scanner(process.getErrorStream());

		}
		catch (Exception e)
		{
			exception = new Exception("at Executor.execute()\n" + e);
			return false;
		}
		return true;
	}

	/**
	 * can execute commands that can not be executed directly using
	 * Runtime.exe()
	 * 
	 * @param cmd
	 * @param dir
	 * @return launch status of process, check "exception" when returns false
	 */
	//@formatter:off
	/*
	  
	  One more approach without creating temp file 
	 
	   public static void main(String[] args) throws IOException {
		String cmd = "echo cybage@123| sudo -S date -s \"6 OCT 2017 1:00:00\"";

		Process process = Runtime.getRuntime().exec(cmd);
		try (Scanner in = new Scanner(process.getInputStream())) {
			while (in.hasNextLine())
				System.out.println(">" + in.nextLine());
		}
		while (process.isAlive())
			;
		System.out.println(process.exitValue() == 0 ? String.format("Success[Command:%s]", cmd)
				: String.format("Failed[Command:%s]", cmd));

	}
	*/
	//@formatter:on

	public boolean executeBat(String cmd, String dir)
	{
		exception = null;
		try
		{
			Files.deleteIfExists(Paths.get(TMP_CMD_FILE_IDENTIFIER));

			tmpCmdFile = Files.createFile(Paths.get(TMP_CMD_FILE_IDENTIFIER));
			PrintWriter tmpCmdFileOut = new PrintWriter(tmpCmdFile.toFile());
			tmpCmdFileOut.println(cmd);
			tmpCmdFileOut.flush();
			tmpCmdFileOut.close();

			if (dir != null)
				process = runTime.exec(tmpCmdFile.toAbsolutePath().toString(), null, new File(dir));
			else
				process = runTime.exec(tmpCmdFile.toAbsolutePath().toString());

			in = new Scanner(process.getInputStream());
			out = new PrintWriter(process.getOutputStream(), true);
			err = new Scanner(process.getErrorStream());

		}
		catch (Exception e)
		{
			exception = new Exception("at Executor.execute()\n" + e);
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param in
	 *            argument will be either "in" or "err", for "in" it reads
	 *            output console of process, for "err" it reads error console of
	 *            process
	 * @return null if can not read console, check "exception" when returns null
	 */
	@SuppressWarnings("deprecation")
	public String readConsole(Scanner in)
	{
		exception = null;
		StringBuilder builder = new StringBuilder();
		Thread tryThis = new Thread(() ->
		{
			while (in.hasNextLine())
			{
				builder.append(in.nextLine());
				builder.append("\n");
			}
		});
		tryThis.setName("tryThis");
		tryThis.setDaemon(true);

		try
		{
			tryThis.start();
			tryThis.join(1000);
			tryThis.stop();

		}
		catch (Exception e)
		{
			exception = new Exception("at Executor.readConsole(Scanner in)\n" + e);
			return null;
		}
		return builder.toString();
	}

	/**
	 * 
	 * @return whether reset was successful or not, check "exception" when
	 *         returns false
	 */
	public boolean reset()
	{
		exception = null;
		try
		{
			process = null;

			if (in != null)
			{
				in.close();
				in = null;
			}

			if (err != null)
			{
				err.close();
				err = null;
			}

			if (out != null)
			{
				out.close();
				out = null;
			}

			if (tmpCmdFile != null)
			{
				Files.deleteIfExists(tmpCmdFile);
				tmpCmdFile = null;
			}

		}
		catch (Exception e)
		{
			exception = new Exception("at Executor.reset()\n" + e);
			return false;
		}
		return true;

	}
}
