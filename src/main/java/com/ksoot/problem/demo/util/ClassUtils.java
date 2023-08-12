package com.ksoot.problem.demo.util;

public final class ClassUtils {

	public static boolean doesClassExist(final String className) {
		try {
			org.apache.commons.lang3.ClassUtils.getClass(className, false);
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}
	
	private ClassUtils() {
		throw new IllegalStateException("Just a utility class, not supposed to be instantiated");
	}
}
