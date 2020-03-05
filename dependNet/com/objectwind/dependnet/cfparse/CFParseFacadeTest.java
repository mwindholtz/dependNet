package com.objectwind.dependnet.cfparse;

import java.util.Vector;

import com.ibm.toad.cfparse.ClassFile;

import junit.framework.TestCase;

public class CFParseFacadeTest extends TestCase
{
	CFParseFacade cfParse;
	String thisPackage =
		"com.objectwind.dependnet.cfparse.";
	String thisClass = "CFParseFacadeTest";

	public CFParseFacadeTest(String name)
	{
		super(name);
	}

	public void setUp()
	{
		cfParse = new CFParseFacade();
        cfParse.setBaseDir(
            "E:/eclipse/workspace/dependnet/");
	}

	public void testSlashes2dots()
	{
		String actual =
			cfParse.slashes2dots(
				"com/objectwind/alltests/Alltests.java");
		String expected =
			"com.objectwind.alltests.Alltests.java";
		assertEquals(expected, actual);
	}

	public static void main(String[] args)
	{
		junit.swingui.TestRunner.run(
			CFParseFacadeTest.class);
	}

	public void testIsNotAbstract()
	{
		assertTrue(!cfParse.isAbstract(thisClass));
	}

	public void testIsAbstract()
	{
		assertTrue(
			cfParse.isAbstract(
				"com.objectwind.dependnet.cfparse.AbstractClass"));
	}

	public void testGetDependancies()
	{
		Vector actual =
			cfParse.getDependancies(
				thisClass,
				"com/objectwind/dependnet/cfparse");
		Vector expected = new Vector();
		expected.addElement(
			"com/objectwind/dependnet/cfparse/CFParseFacadeTest.class");
		assertEquals(expected.get(0), actual.get(0));
	}

	public void testGetClassFile()
	{
		ClassFile cf =
			cfParse.getClassFile(thisPackage + thisClass);
		assertTrue("cannot be null", cf != null);
		assertEquals(
			"CFParseFacadeTest.java",
			cf.getSourceFilename());
	}
    
    public void testGetClassNames()
    {
        String[] actual = cfParse.getClassNames("com.objectwind.dependnet.*",false);
        assertTrue("cannot be null", actual != null);
        assertEquals("com.objectwind.dependnet.AcceptanceTest", actual[0]);
        assertEquals("com.objectwind.dependnet.AllTests", actual[1]);
    }

    public void testGetClassNamesAllTests()
    {
        cfParse.setBaseDir(
            "E:/eclipse/workspace/dependnet/");
        String[] actual = cfParse.getClassNames("alltests.*",false);
        assertTrue("cannot be null", actual != null);
        assertEquals("alltests.AllTests", actual[0]);
    }

}
