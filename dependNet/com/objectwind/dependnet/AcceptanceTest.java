package com.objectwind.dependnet;

import java.io.*;
import java.io.PrintStream;
import java.util.Vector;

import junit.framework.TestCase;

public class AcceptanceTest extends TestCase
{
	DProject project;
	DependInfo dependInfo;
	String[] args = { "alltests" };
    String root = "./";
  

	public AcceptanceTest(String name)
	{
		super(name);
	}

	public void setUp()
	{
		dependInfo =
			new com
				.objectwind
				.dependnet
				.cfparse
				.CFParseFacade();

		project = new DProject(dependInfo);
        dependInfo.setOut(System.out);
        if (!DProject.checkAndLoadArgs(args))
        {
            project.usage();
            return;
        }

        dependInfo.setBaseDir(root);

	}

	public void testFunctionTest_01()
	{
		System.out.println("");
		System.out.println(
			"Output file is: " + setupOutputFile(dependInfo));
		System.out.println("");

		project.printHeader();
		project.printFormulas();
		project.printStatus();

		project.collectPackages(DProject.packageName);
		project.run();
		project.printAll();
		project.loopAnalyis();
		project.printLoops();

		Vector expected =
			readFileToVector(
				root + "out_alltests.expected");
		Vector actual =
			readFileToVector(root + "out_alltests.txt");

		assertEquals(
			"ObjectWind - DependNet v 2.2",
			actual.get(1));
		assertEquals(expected.size(), actual.size());
		assertEquals(expected.get(1), actual.get(1));
		assertEquals(
			expected.lastElement(),
			actual.lastElement());

	}

    public void idletestFunctionTest_02()
    {
  
        setupOutputFile(dependInfo);
        project.collectPackages(DProject.packageName);
        project.run();
        
    }

	public String setupOutputFile(DependInfo mainInfo)
	{
		String outputFile =
			mainInfo.getBaseDir()
				+ "out_"
				+ DProject.packageName
				+ ".txt";
		try
		{
			mainInfo.setOut(
				new PrintStream(
					new FileOutputStream(outputFile)));
		} catch (Exception x)
		{
			System.out.println(
				"Failed to open output file: "
					+ outputFile);
			x.printStackTrace();
		}
		return outputFile;
	}

	private Vector readFileToVector(String filename)
	{
		Vector result = new Vector();

		try
		{
			FileReader reader = new FileReader(filename);
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

}
