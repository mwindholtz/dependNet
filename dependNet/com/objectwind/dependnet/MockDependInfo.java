package com.objectwind.dependnet;
public class MockDependInfo
	implements com.objectwind.dependnet.DependInfo
{
	public String canonicalize(String name)
	{
		return "mil/msg/inbound_interface/ProcessorFactory.class";
	}

	public boolean fileExistsOutsideJars(String name)
	{
		return false;
	}

	public boolean fileExistsSearchJars(String name)
	{
		return false;
	}

	public String getBaseDir()
	{
		return "c:\\temp\\";
	}

	public java.lang.String[] getClassNames(
		String fileSpec,
		boolean aBoolean)
	{

		String[] array = new String[1];
		array[0] = "AllTests";

		return array;
	}

	public java.util.Vector getDependancies(
		String theClassName,
		String thePackage)
	{
		return null;
	}

	public boolean isAbstract(String name)
	{
		return false;
	}

	public void print(String out)
	{
	}

	public void println(String out)
	{
	}

	public void setBaseDir(String newBaseDir)
	{
	}

	public void setOut(java.io.PrintStream output)
	{
	}

	public String slashes2dots(String name)
	{
		return null;
	}

	public void usesInvoke(
		com.objectwind.dependnet.DPackage aPackage,
		String name)
	{
	}
}