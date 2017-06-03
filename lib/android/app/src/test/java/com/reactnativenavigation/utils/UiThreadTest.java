package com.reactnativenavigation.utils;

import com.reactnativenavigation.BaseTest;

import org.junit.Test;
import org.robolectric.shadows.ShadowLooper;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class UiThreadTest extends BaseTest {
	@Test
	public void postOnUiThread() throws Exception {
		Runnable task = mock(Runnable.class);
		ShadowLooper.pauseMainLooper();
		UiThread.post(task);
		verifyZeroInteractions(task);
		ShadowLooper.runUiThreadTasks();
		verify(task, times(1)).run();
	}

	@Test
	public void postDelayedOnUiThread() throws Exception {
		Runnable task = mock(Runnable.class);
		UiThread.postDelayed(task, 1000);
		verifyZeroInteractions(task);
		ShadowLooper.runUiThreadTasksIncludingDelayedTasks();
		verify(task, times(1)).run();
	}
}
