package com.objectwind.dependnet;

import java.util.*;

public class DClass
{
	private String name;
	private TreeSet uses;
	private TreeSet usedBy;
	private boolean failed;
	private boolean isAbstract;
	private DPackage pack;

	DependInfo info;

	void setPackage(DPackage pack)
	{
		this.pack = pack;
	}

	DPackage getPackage()
	{
		if (pack == null)
			throw new AppError("DClass.getPackage() null pack");
		return pack;
	}

	String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	void addUsedBy(String aClassName)
	{
		usedBy.add(aClassName);
	}

	void addUses(String aClassName)
	{
		uses.add(aClassName);
		//  make other node and add UsedBy to it.
		DClass otherClass =
			getPackage().getProject().getClass(aClassName);
		otherClass.addUsedBy(this.name);
	}

	public String packageNameOf(String fullClassName)
	{
		int lastDot = fullClassName.lastIndexOf('.');
		if (lastDot < 0)
		{
			info.println(
				"packageNameOf() of: (no dot) "
					+ fullClassName);
			return fullClassName;
		}
		String packageName =
			fullClassName.substring(0, lastDot);
		return packageName;
	}

	TreeSet getUsesClasses()
	{
		return uses;
	}

	TreeSet getUsedByClasses()
	{
		return usedBy;
	}

	Vector getUsesPackages()
	{
		TreeSet set = new TreeSet();
		Iterator i = uses.iterator();
		while (i.hasNext())
		{
			set.add(packageNameOf((String) i.next()));
		}
		return new Vector(set);
	}

	Vector getUsedByPackages()
	{
		TreeSet set = new TreeSet();
		Iterator i = usedBy.iterator();
		while (i.hasNext())
		{
			set.add(packageNameOf((String) i.next()));
		}
		return new Vector(set);
	}

	void analyze()
	{

		failed = info.fileExistsSearchJars(name);
		isAbstract = info.isAbstract(name);
		info.usesInvoke(this.getPackage(), name);
		String fullName = info.canonicalize(name);

		String thePackage = getPathName(fullName);
		String theClassName = getClassName(fullName);

		if (!info.fileExistsOutsideJars(fullName))
		{
			if (!info.fileExistsSearchJars(fullName))
			{
				// info.print("= not found: " + fullName);
			} else
			{
				// info.print("= found in jar: " + fullName);
			}

			// info.println(" -skip");
			getPackage().setSkipped();
			return;
		}

		Vector v = new Vector();
		try
		{
			v =
				info.getDependancies(
					theClassName,
					thePackage);
		} catch (Throwable t)
		{
			info.println("getFileDeps(): " + t);
			info.println(" -FILE NOT FOUND");
			getPackage().setSkipped();
			return;
		}

		Iterator i = v.iterator();
		while (i.hasNext())
		{
			String iname = (String) i.next();
			iname =
				iname.substring(0, iname.indexOf(".class"));
			addUses(info.slashes2dots(iname));
		}
	}

	String getPathName(String pathClassSuffix)
	{
		int lastSlash = pathClassSuffix.lastIndexOf('/');
		String path =
			pathClassSuffix.substring(0, lastSlash);
		return path;
	}

	public String getPackageName()
	{
		int lastDot = name.lastIndexOf('.');
		if (lastDot < 0)
		{
			return name;
		}
		String path = name.substring(0, lastDot);
		return path;
	}

	public String getClassName(String pathClassSuffix)
	{
		int lastSlash = pathClassSuffix.lastIndexOf('/');
		String className =
			pathClassSuffix.substring(
				lastSlash + 1,
				pathClassSuffix.length());
		return className;
	}

	public DClass(DependInfo info_in)
	{
		info = info_in;
		uses = new TreeSet();
		usedBy = new TreeSet();
		failed = false;
		isAbstract = false;
	}

	public DProject getProject()
	{
		return getPackage().getProject();
	}

	void printAll()
	{
	}
}