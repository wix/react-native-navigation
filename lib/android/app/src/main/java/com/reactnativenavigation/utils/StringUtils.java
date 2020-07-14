package com.reactnativenavigation.utils;

import androidx.annotation.Nullable;

public class StringUtils {

	@SuppressWarnings("StringEquality")
	public static boolean isEqual(String s1, String s2) {
		if (s1 == null || s2 == null) {
			return s1 == s2;
		}
		return s1.equals(s2);
	}

    public static boolean isEmpty(@Nullable CharSequence s) {
        return s == null || s.length() == 0;
    }
}
