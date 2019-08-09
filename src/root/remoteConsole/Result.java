package root.remoteConsole;
public class Result
{
	private boolean isOkay;
	private String exception;

	public Result(boolean isOkay, Exception exception)
	{
		this.isOkay = isOkay;
		if (exception != null)
			this.exception = exception.toString();
	}

	public boolean isOkay()
	{
		return isOkay;
	}

	public String getException()
	{
		return exception;
	}

	@Override
	public String toString()
	{
		return "Result [isOkay=" + isOkay + ", exception=" + exception + "]";
	}

}