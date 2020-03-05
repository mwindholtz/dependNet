package com.objectwind.dependnet;

import java.util.*;

public interface DependInfo
{

	String canonicalize(String name);
	boolean fileExistsOutsideJars(String name);
	boolean fileExistsSearchJars(String name);
	String getBaseDir();
	String[] getClassNames(
		String fileSpec,
		boolean aBoolean);
	Vector getDependancies(
		String theClassName,
		String thePackage);
	boolean isAbstract(String name);
	void print(String out);
	void println(String out);
	void setBaseDir(String newBaseDir);
	void setOut(java.io.PrintStream output);
	String slashes2dots(String name);
	void usesInvoke(DPackage aPackage, String name);
}
