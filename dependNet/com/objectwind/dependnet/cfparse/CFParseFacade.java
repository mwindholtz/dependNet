package com.objectwind.dependnet.cfparse;

import java.io.*;

import com.ibm.toad.cfparse.*;
import com.ibm.toad.cfparse.utils.*;
import java.util.*;

public class CFParseFacade
	implements com.objectwind.dependnet.DependInfo
{
	String baseDir;
	PrintStream out;
	public String canonicalize(String name)
	{
		return CFUtils.canonicalize(name);
	}
	public boolean fileExistsOutsideJars(String name)
	{
		Object o =
			CFUtils.forName(name, CFUtils.CLASSFILE, false);
		if (o == null)
		{
			return false;
		}
		return true;
	}
	public boolean fileExistsSearchJars(String name)
	{
		Object o =
			CFUtils.forName(name, CFUtils.CLASSFILE, true);
		if (o == null)
		{
			return false;
		}
		return true;
	}

	public java.lang.String getBaseDir()
	{
		return baseDir;
	}
    
	
    /*package*/ ClassFile getClassFile(String name)
	{
		return (ClassFile) CFUtils.forName(
			name,
			CFUtils.CLASSFILE,
			true);
	}

	public String[] getClassNames(
		String fileSpec,
		boolean aBoolean)
	{
  		return CFUtils.getClassNames( fileSpec, aBoolean);
	}

	public Vector getDependancies(
		String theClassName,
		String thePackage)
	{

		return CFUtils.getFileDeps(
			getBaseDir() + thePackage + "/",
			theClassName,
			true);
	}

	public boolean isAbstract(String name)
	{
		ClassFile cf = getClassFile(name);
		if (cf == null)
		{
			return false;
		}
		if (Access.isAbstract(cf.getAccess()))
		{
			return true;
		}
		return false;
	}

	public void print(String message)
	{

		out.print(message);
	}

	public void println(String message)
	{

		out.println(message);
	}

	public void setBaseDir(java.lang.String newBaseDir)
	{
		baseDir = newBaseDir;
	}

	public void setOut(PrintStream output)
	{
		out = output;
	}

	public String slashes2dots(String name)
	{

		return CPUtils.slashes2dots(name);
	}

	public void usesInvoke(
		com.objectwind.dependnet.DPackage aPackage,
		String name)
	{

		ClassFile cf = getClassFile(name);
		if (cf == null)
		{
			return;
		}

		ConstantPool cp = cf.getCP();
		for (int i = 0; i < cp.length(); i++)
		{
			if (cp.getType(i)
				== ConstantPool.CONSTANT_Methodref)
			{
				String meth = cp.getAsJava(i);

				if (meth
					.equals("java.lang.Object java.lang.Class.newInstance(java.lang.Object)"))
				{
					aPackage.setUsesNewInstance(true);
				}

				if (meth
					.equals("java.lang.Object java.lang.reflect.Method.invoke(java.lang.Object, java.lang.Object[])"))
				{
					aPackage.setUsesInvoke(true);
				}
			}
		}
	}
}
