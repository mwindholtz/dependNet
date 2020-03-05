package com.objectwind.dependnet;

import java.util.*;
import junit.framework.*;

import java.io.*;
import com.objectwind.dependnet.*;
public class DProjectTest extends TestCase
{
	DProject theProject;

	public DProjectTest(String name)
	{
		super(name);
	}

	public String expected()
	{
		StringWriter sw = new StringWriter();
		sw.write("\n");
		sw.write("\n");
		sw.write(
			"|============ Key for the metrics ==============================================| \n");
		sw.write(
			"| Ca:  (UsedBy) Num of classes which are _UsedBy_ classes in an external package|\n");
		sw.write(
			"| Ce:  (Uses) Num of external classes which this package _Uses_                 |\n");
		sw.write(
			"| I:   usesClasses / ( usedByClasses + usesClasses )                            |\n");
		sw.write(
			"| A:   AbstractClasses / TotalClasses                                           |\n");
		sw.write(
			"| D':  Abs( A + I - 1 )                                                         |\n");
		sw.write(
			"|===============================================================================|\n");
		sw.write("\n");
		sw.write(
			"=============================================\n");
		sw.write(
			" Processing Package: com.objectwind.dependnet.sample\n");
		sw.write(
			"=============================================\n");
		sw.write(
			"getAllClassNames() for: com.objectwind.dependnet.sample.*\n");
		sw.write("\n");
		sw.write("<Iteration:  1>\n");
		sw.write(
			"Package: com.objectwind.dependnet.sample  classes found: 0\n");
		sw.write(
			"(???) unattached package: com.objectwind.dependnet.sample Ca:0 Ce:0\n");
		sw.write(
			"(???) empty package: com.objectwind.dependnet.sample\n");
		sw.write("Project: Project01\n");
		sw.write("\n");
		sw.write(
			"-----------------------------------------------------\n");
		sw.write(
			"Package: [ com.objectwind.dependnet.sample ]\n");
		sw.write("(Uses Packages: 0)\n");
		sw.write("(UsedBy Packages: 0)\n");
		sw.write("Abstract Classes:           0\n");
		sw.write("Total Classes:              0\n");
		sw.write("Ca (UsedBy Classes):        0\n");
		sw.write("Ce (Uses Classes):          0\n");
		sw.write("A  (Package Abstractness):  0.0\n");
		sw.write("I  (Package Instability):   0.0\n");
		sw.write("D' (Package Distance)      <0.0>\n");
		sw.write("tD'(Package Distance)      <0.0>\n");
		sw.write("\n");
		sw.write("\n");
		return sw.toString();
	}

	public Vector expectedAsVector()
	{

		LineNumberReader lreader =
			new LineNumberReader(
				new StringReader(expected()));

		Vector expected = new Vector();
		try
		{
			String aLine = lreader.readLine();
			while (aLine != null)
			{
				expected.addElement(aLine);
				aLine = lreader.readLine();
			}

		} catch (IOException x)
		{
			x.printStackTrace();
		}
		return expected;
	}

	private Vector readFileToVector(String filename)
	{
		Vector result = new Vector();

		try
		{
			FileReader reader =
				new FileReader("c:/export/" + filename);
			LineNumberReader lni =
				new LineNumberReader(reader);
			String line = lni.readLine();
			while (line != null)
			{
				result.addElement(line);
				line = lni.readLine();
			}
		} catch (IOException x)
		{
			x.printStackTrace();
			assertTrue(false);
		}
		return result;
	}

	public void setUp()
	{

		DependInfo mainInfo = new MockDependInfo();
		mainInfo.setBaseDir("c:\\temp\\");

		try
		{
			mainInfo.setOut(
				new PrintStream(
					new ByteArrayOutputStream()));
		} catch (Exception x)
		{
			x.printStackTrace();
		}

		theProject = new DProject(mainInfo);
	}

	public void testGetClass()
	{

		DClass class1 =
			theProject.getClass("com.objectwind.AllTest");
		DClass class2 =
			theProject.getClass("com.objectwind.AllTest");
		assertTrue(class1 == class2);
	}

	public void testGetPackage()
	{

		DPackage pack1 =
			theProject.getPackage("com.objectwind");
		DPackage pack2 =
			theProject.getPackage("com.objectwind");
		assertTrue(pack1 == pack2);
	}

	public void testJustRunTheProject()
	{

		theProject.printHeader();
		theProject.printFormulas();
		theProject.printStatus();
		theProject.collectPackages(
			"com.objectwind.dependnet.sample");
		theProject.run();
		theProject.printAll();
		theProject.loopAnalyis();
		theProject.printLoops();
	}
}