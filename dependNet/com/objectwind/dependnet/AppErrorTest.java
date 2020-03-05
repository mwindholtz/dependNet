package com.objectwind.dependnet;

public class AppErrorTest extends junit.framework.TestCase
{
	public AppErrorTest(String name)
	{
		super(name);
	}
	private void shouldThrow()
	{
		throw new AppError();
	}
	private void shouldThrowSubException()
	{
		try
		{
			String s = null;
			s.length();
		} catch (Exception x)
		{
			throw new AppError(x);
		}
	}
	public void testEmbeddedException()
	{
		try
		{
			shouldThrowSubException();
		} catch (AppError a)
		{
			Throwable t = a.getOriginalThrownObject();
			assertTrue(t instanceof NullPointerException);
			return;
		}
		fail("Expected AppError Exception");

	}
	public void testOne()
	{
		try
		{
			shouldThrow();
		} catch (AppError a)
		{
			return;
		}
		fail("Expected AppError Exception");

	}
	public void testToString()
	{
		AppError error = new AppError();
		assertEquals(
			"Application Error: \n",
			error.toString());

	}
	public void testToString2()
	{
		AppError error = new AppError("Error Test");
		assertEquals(
			"Application Error: \nMessage: Error Test\n",
			error.toString());

	}
	public void testToString3()
	{

		String expected =
			"Application Error: \n"
				+ "Original thrown object message: Application Error: \n"
				+ "Message: Error Test\n"
				+ "\n"
				+ "Stack trace:Application Error: \n"
				+ "Message: Error Test\n";
		AppError error =
			new AppError(new AppError("Error Test"));
		assertTrue(
			expected.equals(
				error.toString().substring(0, 145)));

	}
}
