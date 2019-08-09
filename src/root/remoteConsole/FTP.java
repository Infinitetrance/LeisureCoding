package root.remoteConsole;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

import root.util.IOUtils;

public class FTP {

	/**
	 * 
	 * @param socket   - to WRITE
	 * @param filePath - to read
	 * @return
	 */
	public static Result send(Socket socket, String filePath, boolean showProgress) {
		BufferedOutputStream out = null;
		BufferedInputStream in = null;
		try
		{
			File file = new File(filePath);
			out = new BufferedOutputStream(socket.getOutputStream());
			in = new BufferedInputStream(new FileInputStream(file));
			long fileSize = Files.size(Paths.get(file.getAbsolutePath()));

			// writing 64 byte header.[ [FileName:56 bytes] [FileSize:8 bytes] ]
			out.write(ByteUtils.stringTo56Bytes(file.getName()));
			out.write(ByteUtils.longToBytes(fileSize));

			// writing file content
			copy(in, out, fileSize, showProgress);
			out.flush();

			return new Result(true, null);
		} catch (Exception e)
		{
			return new Result(false, e);
		} finally
		{
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(out);
		}
	}

	/**
	 * 
	 * @param socket   - to READ
	 * @param filePath - to write. Represents path in file system where to store the
	 *                 file, do not pass file name here
	 * @return
	 */
	public static Result read(Socket socket, String filePath, boolean showProgress) {
		BufferedOutputStream out = null;
		BufferedInputStream in = null;
		try
		{
			in = new BufferedInputStream(socket.getInputStream());

			// READ and PARSE file HEADER[NAME,SIZE] from socket
			String fileName = null;
			long fileSize;
			{
				// READ and PARSE FILE NAME
				byte[] fileNameBytes = new byte[56];
				for (byte i = 0; i < 56; i++)
				{
					fileNameBytes[i] = (byte) in.read();
					if (fileNameBytes[i] == -1)
					{
						throw new Exception("EOF encountered while reading FILE NAME");
					}
				}
				fileName = new String(fileNameBytes);
				filePath += File.separator + fileName;
				filePath = filePath.trim();

				// READ and PARSE FILE SIZE
				byte[] fileSizeBytes = new byte[8];
				for (byte i = 0; i < 8; i++)
				{
					fileSizeBytes[i] = (byte) in.read();
					if (fileSizeBytes[i] == -1)
					{
						throw new Exception("EOF encountered while reading FILE SIZE");
					}
				}
				fileSize = ByteUtils.BytesToLong(fileSizeBytes);
			}

			File file = new File(filePath);
			out = new BufferedOutputStream(new FileOutputStream(file));

			// read file content from socket
			copy(in, out, fileSize, showProgress);
			out.flush();
			return new Result(true, null);
		} catch (Exception e)
		{
			return new Result(false, e);
		} finally
		{
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(out);
		}
	}

	static void copy(InputStream in, OutputStream out, long fileSize, boolean showProgress) throws IOException {
		int len = 0;
		byte[] buf = new byte[1024 * 8];
		IOUtils.OneField progress = new IOUtils.OneField();

		Thread progressThread = new Thread() {
			public void run() {
				while (progress.value != fileSize)
				{
					System.out.print("\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b");
					System.out.printf("Progress : %05.2f %%", ((progress.value / fileSize) * 100));
				}
			}

		};
		progressThread.setName("FTP.copy.ProgressThread");
		progressThread.setDaemon(true);

		if (showProgress)
			progressThread.start();

		long elapsedTime = System.currentTimeMillis();
		while ((len = in.read(buf)) != -1)
		{
			out.write(buf, 0, len);
			progress.value += len;
		}
		elapsedTime = System.currentTimeMillis() - elapsedTime;
		System.out.println("\nElapsedTime : " + elapsedTime / 1000 + " Seconds");
		System.out.printf("DataSize : %.2f MB \n", (fileSize / (1024.0 * 1024.0)));
	}

}
