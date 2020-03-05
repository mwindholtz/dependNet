package com.objectwind.dependnet;

import com.objectwind.dependnet.*;
public class DClassTest extends junit.framework.TestCase
{
	/**
	 * DClassTest constructor comment.
	 * @param name java.lang.String
	 */
	public DClassTest(String name)
	{
		super(name);
	}
	public void testName()
	{

		DependInfo info = new MockDependInfo();
		DClass c = new DClass(info);
		c.setName(
			"com.objectwind.dependnet.tests.AllTests");
		assertEquals(
			"com.objectwind.dependnet.tests",
			c.getPackageName());

		assertEquals(
			"AllTests",
			c.getClassName(
				"com/objectwind/dependnet/tests/AllTests"));

		assertTrue(true);
	}
	public void testOne()
	{

		DependInfo info = new MockDependInfo();
		DClass c = new DClass(info);
		c.setName(
			"com.objectwind.dependnet.tests.AllTests");
		assertEquals(
			"com.objectwind.dependnet.tests",
			c.getPackageName());
		assertTrue(true);
	}
}
