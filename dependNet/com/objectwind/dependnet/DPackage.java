package com.objectwind.dependnet;

import java.util.*;

public class DPackage
{

	static String indent = "         ";

	String name;
	DProject project;
	Hashtable classes;
	boolean skipped;
	boolean hasRun;
	boolean DprimeTransDone;
	int usesCount;
	int usedByCount;
	boolean usesInvoke = false;
	boolean usesNewInstance = false;

	float I;
	float A;
	float Dprime;
	float DprimeTrans;
	int abstractCount;
	int Ca;
	int Ce;

	DependInfo info;

	String getName()
	{
		return name;
	}
	void setName(String name)
	{
		this.name = name;
	}

	void collectClasses()
	{
		Enumeration loop = getAllClassNames().elements();
		while (loop.hasMoreElements())
		{
			project.newClass((String) loop.nextElement());
		}
	}

	void run()
	{

//		info.print(
//			"Package: "
//				+ name
//				+ "  classes found: "
//				+ classes.size());
//		info.println("");

		Enumeration e = classes.elements();
		while (e.hasMoreElements())
		{
			DClass c = (DClass) e.nextElement();
			c.analyze();
		}
		hasRun = true;
	}

	float calculateDPrimeTrans()
	{

		// can't get any worse than 1.0 
		// so stop looking.
		if (Dprime == 1.0)
		{
			return DprimeTrans = 1;
		}

		// have we already calculated this sub-tree?
		if (DprimeTransDone)
		{
			return (DprimeTrans > Dprime)
				? DprimeTrans
				: (DprimeTrans = Dprime);
		}

		// short curcuit dependency loops
		DprimeTransDone = true;

		Vector v = getUsesPackages();

		// No more dependencies 
		// so return this package Instability
		// This also covers the case where DPackage was
		// not processed, since v would then have size == 0.
		//
		if (v.size() == 0)
		{
			return DprimeTrans = Dprime;
		}

		// start worstI with this package Instability
		float worstD = Dprime;
		Enumeration e = v.elements();
		while (e.hasMoreElements())
		{
			String packName = (String) e.nextElement();
			DPackage pack =
				getProject().getPackage(packName);
			float otherD = pack.calculateDPrimeTrans();
			if (otherD > worstD)
				worstD = otherD;
		}
		DprimeTrans = worstD;
		return DprimeTrans;
	}

	boolean hasRun()
	{
		if (hasRun)
			return true;
		if (skipped)
			return true;
		return false;
	}

	void setProject(DProject project)
	{
		this.project = project;
	}

	public void setUsesInvoke(boolean flag)
	{
		usesInvoke = flag;
	}
	public void setUsesNewInstance(boolean flag)
	{
		usesNewInstance = flag;
	}

	void setSkipped()
	{
		skipped = true;
	}

	public DProject getProject()
	{
		if (project == null)
			info.println(
				"DPackage.getProject() null project");
		return project;
	}

	float getDprimeTrans()
	{
		return DprimeTrans;
	}

	void add(DClass aClass)
	{
		aClass.setPackage(this);
		classes.put(aClass.getName(), aClass);
	}

	DClass get(String className)
	{
		Object o = classes.get(className);
		if (o == null)
		{
			return null;
		} else
		{
			return (DClass) o;
		}
	}

	void calculateMetrics()
	{

		Ca = getUsedByClassesCount();
		Ce = getUsesClassesCount();

		float denom = Ca + Ce;
		if (denom == 0.0)
		{
			I = 0;
			info.println(
				"(???) unattached package: "
					+ getName()
					+ " Ca:"
					+ Ca
					+ " Ce:"
					+ Ce);
		} else
		{
			I = Ce / denom;
		}

		// System.out.println("-- calc: " + getName() +" Ce:" + Ce +" over:" + denom + " => I:" + I);

		int classCount = getClassCount();
		if (classCount == 0)
		{
			A = 0;
			info.println(
				"(???) empty package: " + getName());
		} else
		{
			A = ((float) getAbstractCount()) / classCount;
		}

		if ((A == 0) && (I == 0))
		{
			Dprime = 0;
		} else
		{
			Dprime = A + I - 1;
		}

		if (Dprime < 0.0)
			Dprime = Dprime * (-1);
	}

	Vector getUsesPackages()
	{
		TreeSet set = new TreeSet();
		Enumeration eClass = classes.elements();
		while (eClass.hasMoreElements())
		{
			DClass c = (DClass) eClass.nextElement();
			Enumeration ePack =
				c.getUsesPackages().elements();
			while (ePack.hasMoreElements())
			{
				set.add((String) ePack.nextElement());
			}
		}
		set.remove(this.getName());
		usesCount = set.size();
		return new Vector(set);
	}

	Vector getUsedByPackages()
	{
		TreeSet set = new TreeSet();
		Enumeration eClass = classes.elements();
		while (eClass.hasMoreElements())
		{
			DClass c = (DClass) eClass.nextElement();
			Enumeration ePack =
				c.getUsedByPackages().elements();
			while (ePack.hasMoreElements())
			{
				set.add((String) ePack.nextElement());
			}
		}
		set.remove(this.getName());
		usedByCount = set.size();
		return new Vector(set);
	}

	int getUsedByClassesCount()
	{
		TreeSet set = new TreeSet();
		Enumeration eClass = classes.elements();
		while (eClass.hasMoreElements())
		{
			DClass c = (DClass) eClass.nextElement();
			Iterator ePack =
				c.getUsedByClasses().iterator();
			while (ePack.hasNext())
			{
				String className = (String) ePack.next();
				if (className.startsWith(this.getName()))
				{ /* skip classes in same package */
				} else
				{
					set.add(className);
				}
			}
		}
		return set.size();
	}

	int getUsesClassesCount()
	{
		TreeSet set = new TreeSet();
		Enumeration eClass = classes.elements();
		while (eClass.hasMoreElements())
		{
			DClass c = (DClass) eClass.nextElement();
			Iterator ePack = c.getUsesClasses().iterator();
			while (ePack.hasNext())
			{
				String className = (String) ePack.next();
				if (className.startsWith(this.getName()))
				{ /* skip classes in same package */
				} else
				{
					set.add(className);
				}
			}
		}
		set.remove(this.getName());
		return set.size();
	}

	int getAbstractCount()
	{
		if (abstractCount != 0)
			return abstractCount;
		abstractCount = 0;
		Enumeration eClass = classes.elements();
		while (eClass.hasMoreElements())
		{
			DClass c = (DClass) eClass.nextElement();
			if (info.isAbstract(c.getName()))
			{
				abstractCount++;
			}
		}
		return abstractCount;
	}

	int getClassCount()
	{
		return classes.size();
	}

	Vector getAllClassNames()
	{
//		info.println(
//			"getAllClassNames() for: " + name + ".*");

		Vector vector = new Vector();
		String[] classesArray =
			info.getClassNames(name + ".*", false);

		for (int i = 0; i < classesArray.length; i++)
		{
			// System.out.println(classesArray[i]);
			vector.add(classesArray[i]);
		}
		return vector;
	}

	void printName()
	{
		info.println("");
		info.println(
			"-----------------------------------------------------");
		info.println("Package: [ " + name + " ]");
	}

	void printUses()
	{
		Enumeration e = getUsesPackages().elements();
		info.println("Uses Packages: " + usesCount );
		while (e.hasMoreElements())
		{
			String pack = (String) e.nextElement();
			info.print(indent + "-> " + pack);
			float dependentD =
				getProject()
					.getPackage(pack)
					.getDprimeTrans();

            info.print("  (" + dependentD + ") ");
			if (dependentD == DprimeTrans)
			{
				info.print("<=");
			}
            info.println("");
		}
	}

	void printUsedBy()
	{
		Enumeration e2 = getUsedByPackages().elements();
		info.println(
			"UsedBy Packages: " + usedByCount );
		while (e2.hasMoreElements())
		{
			String pack = (String) e2.nextElement();
			info.println(indent + "<- " + pack);
		}
	}

	void printMetrics()
	{
        String sep = ") ";
		info.print("(A#:" + getAbstractCount() + sep);
		info.print("(Classes#:" + getClassCount()+ sep);
		info.print("(<- Ca:" + usedByCount + sep);
		info.println("(-> Ce:" + usesCount + sep);
		info.print("(A:" + A + sep);
		info.println("(I:" + I + sep);
		info.print(  "(D':" + Dprime + sep);
        info.println("(tD':" + DprimeTrans  + sep );

		if (usesNewInstance)
		{
			info.println("* Uses newInstance()");
		}
		if (usesInvoke)
		{
			info.println ("* Uses invoke()");
		}
		if (skipped)
		{
			info.println("* Not Pocessed");
		}
	}

	boolean getDependencyLoop(Vector aLoop)
	{
		// printLoop(aLoop);
		if (aLoop.contains(this))
		{
			// add again as last element
			// to complete the loop
			aLoop.add(this);
			return true;
		}
		aLoop.add(this);
		Vector usesNames = getUsesPackages();
		Enumeration enum = usesNames.elements();

		// System.out.println("dependence of: " + getName());
		while (enum.hasMoreElements())
		{
			String packName = (String) enum.nextElement();
			// System.out.println("               " + packName);
			DPackage pack =
				getProject().getPackage(packName);
			if (pack.getDependencyLoop(aLoop))
				return true;
		}
		aLoop.remove(this);
		return false;
	}

	DPackage(DependInfo info_in)
	{
		info = info_in;
		name = "noName";
		classes = new Hashtable();
		DprimeTransDone = false;
	}

	void printAll()
	{
		printName();
        printMetrics();
		printUses();
		printUsedBy();
		Enumeration eClass = classes.elements();
		while (eClass.hasMoreElements())
		{
			DClass c = (DClass) eClass.nextElement();
			c.printAll();
		}
	}
}