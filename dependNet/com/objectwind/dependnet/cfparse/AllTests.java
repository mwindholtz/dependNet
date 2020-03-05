package com.objectwind.dependnet.cfparse;

import junit.framework.*;

public class AllTests
{

	public static void main(String[] args)
	{
		junit.swingui.TestRunner.run(AllTests.class);
	}

	public static Test suite()
	{
		TestSuite suite =
			new TestSuite("Test for com.objectwind.dependnet.cfparse");
		//$JUnit-BEGIN$
		suite.addTest(
			new TestSuite(CFParseFacadeTest.class));
		//$JUnit-END$
		return suite;
	}
}
