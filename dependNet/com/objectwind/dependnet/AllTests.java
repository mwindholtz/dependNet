package com.objectwind.dependnet;

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
			new TestSuite("Test for com.objectwind.dependnet");
		//$JUnit-BEGIN$
        suite.addTest(new TestSuite(AcceptanceTest.class));
		suite.addTest(new TestSuite(AppErrorTest.class));
		suite.addTest(new TestSuite(DClassTest.class));
		suite.addTest(new TestSuite(DPackageTest.class));
		suite.addTest(new TestSuite(DProjectTest.class));
		//$JUnit-END$
		return suite;
	}
}
