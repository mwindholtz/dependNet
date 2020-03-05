package com.objectwind.dependnet;

import java.io.*;

public class AppError extends RuntimeException
{
	protected Throwable originalThrownObject;

	public AppError()
	{
		super();
	}
	public AppError(String s)
	{
		super(s);
	}

	public AppError(String s, Throwable t)
	{
		super(s);
		originalThrownObject = t;
	}

	public AppError(Throwable t)
	{
		super();
		originalThrownObject = t;
	}
	public Throwable getOriginalThrownObject()
	{
		return originalThrownObject;
	}
	public static String getStackTraceAsString(Throwable e)
	{
		ByteArrayOutputStream outputStream =
			new ByteArrayOutputStream();
		PrintWriter printWriter =
			new PrintWriter(outputStream);
		e.printStackTrace(printWriter);
		printWriter.flush();
		return outputStream.toString();
	}
	public String toString()
	{
		StringBuffer sb =
			new StringBuffer().append(
				"Application Error: \n");

		if (getMessage() != null)
		{
			sb.append("Message: ");
			sb.append(getMessage());
			sb.append("\n");
		}
		if (originalThrownObject != null)
		{
			sb.append("Original thrown object message: ");
			sb.append(originalThrownObject.toString());
			sb.append("\n");
			sb.append("Stack trace:");
			sb.append(
				getStackTraceAsString(originalThrownObject));
		}
		return sb.toString();
	}
}