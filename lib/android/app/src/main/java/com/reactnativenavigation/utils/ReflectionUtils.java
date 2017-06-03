package com.reactnativenavigation.utils;

import android.support.annotation.Nullable;

import java.lang.reflect.Field;

public class ReflectionUtils {
	public static void setField(Object obj, String name, Object value) {
		try {
			Field field = getField(obj.getClass(), name);
			if (field == null) {
				return;
			}
			field.setAccessible(true);
			field.set(obj, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Nullable
	public static Object getDeclaredField(Object obj, String fieldName) {
		try {
			Field f = getField(obj.getClass(), fieldName);
			if (f == null) {
				return null;
			}
			f.setAccessible(true);
			return f.get(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static Field getField(Class clazz, String name) {
		try {
			return clazz.getDeclaredField(name);
		} catch (NoSuchFieldException nsfe) {
			return getField(clazz.getSuperclass(), name);
		} catch (Exception e) {
			return null;
		}
	}
}
