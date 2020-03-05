package com.objectwind.dependnet;

import java.util.*;

import java.io.*;
public class DProject
{

	Hashtable packages;
	Vector loopList;

	static String skipfile;
	static String packageName;
	DependInfo info;
	static String outfile;

    static final public String getVersion()
    {
        return "2.2";
    }


	public void usage()
	{
		info.println("Usage: ");
		info.println(
			"java -classpath dependNet.jar;.  com.objectwind.dependnet.DProject packageName");
	}

	public void printFormulas()
	{
		info.println("");
		info.println(
			"|===== KEY FOR METRICS ================================================");
		info.println("|     ");
		info.println(
			"| Ca:  (UsedBy) Num of classes which are _UsedBy_ classes");
		info.println("|       in an external package");
		info.println(
			"| Ce:  (Uses) Num of external classes which this package _Uses_");
		info.println(
			"| I:   usesClasses / ( usedByClasses + usesClasses )  ");
		info.println(
			"| A:   AbstractClasses / TotalClasses                 ");
		info.println("|");
		info.println(
			"| For D' and tD':  0.0 == GOOD, 1.0 == Bad");
		info.println(
			"| --------------------------------------------------");
		info.println("| D':  Abs( A + I - 1 )   ");
		info.println(
			"| tD': Highest of 'D and tD' of all 'Uses' packages   ");
		info.println("|");
		info.println(
			"|                                                     ");
		info.println(
			"|======================================================================");
		info.println("");
		info.println("Limitations: ");
		info.println(
			" (1) Requires cfparse.jar available from www.ibm.alphaworks.com");
		info.println(
			" (2) Cannot process classes or packages inside JAR files.");
		info.println(
			" (3) Cannot follow dependencies created dynamically by Method.invoke()");
		info.println(
			" (4) must execute in the directory under the target class");
		info.println(
			"     this is a CFParse bug.  I hope to have a work around soon.");
		info.println("");

	}

	public static boolean checkAndLoadArgs(String[] args)
	{

		if (args.length == 1)
		{
			packageName = args[0];
			skipfile = "[None]";
			System.out.println("pack set: " + packageName);
			return true;
		}

		if (args.length == 2)
		{
			packageName = args[1];
			System.out.println("pack set: " + packageName);
			return true;
		}

		return false;
	}

	public void printStatus()
	{
		info.println(
			"=============================================");
		info.println(" Processing Package: " + packageName);
		info.println(
			"=============================================");
	}

	public static void main(String[] args)
	{

		DependInfo mainInfo =
			new com
				.objectwind
				.dependnet
				.cfparse
				.CFParseFacade();

		DProject p = new DProject(mainInfo);
		mainInfo.setOut(System.out);
		if (!checkAndLoadArgs(args))
		{
			p.usage();
			return;
		}

		mainInfo.setBaseDir("./");

		String outputFile =
			mainInfo.getBaseDir()
				+ "out_"
				+ packageName
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

		System.out.println("");
		System.out.println("Output file is: " + outputFile);
		System.out.println("");

		p.printHeader();
		p.printFormulas();
		p.printStatus();

		p.collectPackages(packageName);
		p.run();
		p.printAll();
		p.loopAnalyis();
		p.printLoops();
	}

	public void run()
	{
		int i = 0;
		while (moreToRun())
			runIteration(++i);
		calculateMetrics();
	}

	public boolean moreToRun()
	{
		Enumeration e = packages.elements();
		while (e.hasMoreElements())
		{
			DPackage pack = (DPackage) e.nextElement();
			if (!pack.hasRun())
			{
				return true;
			}
		}
		return false;
	}

	public void runIteration(int iteration)
	{
		// iterate thru hashtable values 
		Enumeration e = packages.elements();
		while (e.hasMoreElements())
		{
			DPackage pack = (DPackage) e.nextElement();
			pack.run();
		}
	}

	public void calculateMetrics()
	{
		Enumeration e = packages.elements();
		while (e.hasMoreElements())
		{
			DPackage pack = (DPackage) e.nextElement();
			pack.calculateMetrics();
		}
		calculateDPrimeTrans();
	}

	public void calculateDPrimeTrans()
	{
		Enumeration e = packages.elements();
		while (e.hasMoreElements())
		{
			DPackage pack = (DPackage) e.nextElement();
			pack.calculateDPrimeTrans();
		}
	}

	public DPackage collectPackages(String packageName)
	{
		DPackage pack = new DPackage(info);
		pack.setName(packageName);
		pack.setProject(this);
		packages.put(pack.getName(), pack);
		pack.collectClasses();
		return pack;
	}

	public DPackage getPackage(String packageName)
	{
		Object o = packages.get(packageName);
		if (o == null)
		{
			return collectPackages(packageName);
		} else
		{
			return (DPackage) o;
		}
	}

	public DClass getClass(String className)
	{
		DClass tempClass = new DClass(info);
		tempClass.setName(className);
		DPackage pack =
			getPackage(tempClass.getPackageName());
		tempClass = null;
		DClass c = pack.get(className);
		if (c == null)
		{
			c = newClass(className);
		}
		return c;
	}

	public DClass newClass(String className)
	{
		DClass c = new DClass(info);
		c.setName(className);
		DPackage pack = getPackage(c.getPackageName());
		pack.add(c);
		return c;
	}

	public void loopAnalyis()
	{
		info.println("");
		info.println("");
		// For each package:
		// 	check each dependency for a loop
		Enumeration e = packages.elements();
		while (e.hasMoreElements())
		{
			DPackage pack = (DPackage) e.nextElement();

			Vector aLoop = new Vector();
			if (pack.getDependencyLoop(aLoop))
			{
				loopList.add(aLoop);
				return; // found  and exit
			}
		}
		return; // found none and exit
	}

	public void printLoops()
	{
		Enumeration e = loopList.elements();
		while (e.hasMoreElements())
		{
			Vector oneLoop = (Vector) e.nextElement();
			printOneLoop(oneLoop);
		}
	}

	public void printOneLoop(Vector oneLoop)
	{

		boolean first = true;
		String indent = "     ";

		String lastName =
			((DPackage) oneLoop.lastElement()).getName();
		Enumeration e = oneLoop.elements();
		int i = 0;
		while (e.hasMoreElements())
		{
			i++;
			DPackage pack = (DPackage) e.nextElement();
			if (!first)
			{
				info.print(indent + " |--> ");
			}
			info.print(pack.getName());
			if (lastName != pack.getName())
			{
				info.println("");
			} else if (i == oneLoop.size())
			{
				info.println(" ----^ ");
			} else
			{
				info.println(" <----\\ ");
			}
			first = false;
		}
		info.println("");
	}

	public DProject(DependInfo mainInfo)
	{
		info = mainInfo;
		packages = new Hashtable();
		loopList = new Vector();
	}


	public void printAll()
	{
		Enumeration e = packages.elements();
		while (e.hasMoreElements())
		{
			DPackage pack = (DPackage) e.nextElement();
			pack.printAll();
		}
	}

	public void printHeader()
	{
		info.println("");
		info.println(
			"ObjectWind - DependNet v " + getVersion());
		info.println(
			"~ Martin Metrics are described at: www.ObjectMentor.com");
		info.println(
			"~ DependNet updates can be found at: www.ObjectWind.com");
		info.println("");
	}
}