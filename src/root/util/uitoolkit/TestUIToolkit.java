package root.util.uitoolkit;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import javafx.scene.control.Alert.AlertType;
import root.util.IOUtils;

public class TestUIToolkit {
	static UIToolkit toolkit;
	static final String APP_NAME = "Fibonacci Application";

	public static void main(String[] args) throws Exception {
		toolkit = UIToolkit.getUIToolkit();
		try
		{
			while (!login())
			{
				toolkit.showConfirmationDialog(APP_NAME, null, "Invalid Credentials! Like to try again ?", null, () ->
				{
					System.exit(-1);
				}).doWait();
			}

			String[] inputs = new String[1];
			while (true)
			{
				try
				{
					toolkit.showInputDialog(APP_NAME, null, "Enter a positive number to calculate fibonacci", "",
							input ->
							{
								inputs[0] = input;
							}).doWait();

					toolkit.showConfirmationDialog(APP_NAME,
							"Fibonacci of " + inputs[0] + ": "
									+ String.valueOf(fibonacciOf(Integer.parseInt(inputs[0]))),
							"Continue with another number?", null, () ->
							{
								System.exit(-1);
							}).doWait();
				} catch (NumberFormatException e)
				{
					toolkit.showAlert(APP_NAME, null, "Invalid Number", AlertType.ERROR).doWait();
				}
			}

		} finally
		{
			IOUtils.closeQuietly(toolkit);
		}
	}

	static int fibonacciOf(int num) {
		int[] cache = new int[num + 1];
		Arrays.fill(cache, -1);
		return fib(cache, num);
	}

	static int fib(int[] memo, int num) {
		if (memo[num] != -1)
			return memo[num];
		if (num == 0)
			return 0;
		if (num == 1 || num == 2)
			return 1;
		memo[num] = fib(memo, num - 1) + fib(memo, num - 2);
		return memo[num];
	}

	static boolean login() {
		AtomicBoolean loggedin = new AtomicBoolean(false);
		toolkit.showLoginDialog(APP_NAME, "Login to access application", (user, pass) ->
		{
			if (user.equals("root") && pass.equals("java"))
				loggedin.set(true);
		}).doWait();
		return loggedin.get();
	}
}
