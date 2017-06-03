package com.reactnativenavigation.utils;

import android.view.View;

import com.reactnativenavigation.BaseTest;

import org.junit.Test;
import org.robolectric.shadow.api.Shadow;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class UiUtilsTest extends BaseTest {
	@Test
	public void runOnPreDrawOnce() throws Exception {
		View view = Shadow.newInstanceOf(View.class);
		Runnable task = mock(Runnable.class);
		verifyZeroInteractions(task);

		UiUtils.runOnPreDrawOnce(view, task);
		view.getViewTreeObserver().dispatchOnPreDraw();
		verify(task, times(1)).run();
	}
}
