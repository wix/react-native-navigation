package com.reactnativenavigation.viewcontrollers.overlay;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.SimpleViewController;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.presentation.OverlayManager;
import com.reactnativenavigation.utils.CommandListener;
import com.reactnativenavigation.utils.CommandListenerAdapter;
import com.reactnativenavigation.viewcontrollers.ChildControllersRegistry;

import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class OverlayManagerTest extends BaseTest {
    private static final String OVERLAY_ID_1 = "OVERLAY_1";
    private static final String OVERLAY_ID_2 = "OVERLAY_2";

    private OverlayManager uut;
    private SimpleViewController overlay1;
    private SimpleViewController overlay2;
    private FrameLayout contentLayout;
    private FrameLayout overlayContainer;

    @Override
    public void beforeEach() {
        Activity activity = newActivity();
        contentLayout = new FrameLayout(activity);
        contentLayout.layout(0, 0, 1000, 1000);
        activity.setContentView(contentLayout);
        overlayContainer = new FrameLayout(activity);
        contentLayout.addView(overlayContainer);

        ChildControllersRegistry childRegistry = new ChildControllersRegistry();
        overlay1 = spy(new SimpleViewController(activity, childRegistry, OVERLAY_ID_1, new Options()));
        overlay2 = spy(new SimpleViewController(activity, childRegistry, OVERLAY_ID_2, new Options()));
        uut = new OverlayManager();
    }

    @Test
    public void show_attachesOverlayContainerToContentLayout() {
        uut.show(overlayContainer, overlay1, new CommandListenerAdapter());
        assertThat(overlayContainer.getParent()).isEqualTo(contentLayout);
        uut.show(overlayContainer, overlay2, new CommandListenerAdapter());
    }

    @Test
    public void show() {
        CommandListenerAdapter listener = spy(new CommandListenerAdapter());
        uut.show(overlayContainer, overlay1, listener);
        verify(listener).onSuccess(OVERLAY_ID_1);
        verify(overlay1).onViewDidAppear();
        assertThat(overlay1.getView().getParent()).isEqualTo(overlayContainer);
        assertMatchParent(overlay1.getView());
    }

    @Test
    public void dismiss() {
        uut.show(overlayContainer, overlay1, new CommandListenerAdapter());
        assertThat(uut.size()).isOne();
        CommandListener listener = spy(new CommandListenerAdapter());
        uut.dismiss(overlayContainer, overlay1.getId(), listener);
        assertThat(uut.size()).isZero();
        verify(listener, times(1)).onSuccess(OVERLAY_ID_1);
        verify(overlay1, times(1)).destroy();
    }

    @Test
    public void dismiss_rejectIfOverlayNotFound() {
        CommandListener listener = spy(new CommandListenerAdapter());
        uut.dismiss(overlayContainer, overlay1.getId(), listener);
        verify(listener, times(1)).onError(any());
    }

    @Test
    public void dismiss_onViewReturnedToFront() {
        uut.show(overlayContainer, overlay1, new CommandListenerAdapter());
        uut.show(overlayContainer, overlay2, new CommandListenerAdapter());
        verify(overlay1, times(0)).onViewBroughtToFront();

        uut.dismiss(overlayContainer, OVERLAY_ID_2, new CommandListenerAdapter());
        verify(overlay1, times(1)).onViewBroughtToFront();
    }

    @Test
    public void dismiss_overlayContainerIsHiddenIfAllOverlaysAreDismissed() {
        uut.show(overlayContainer, overlay1, new CommandListenerAdapter());
        uut.show(overlayContainer, overlay2, new CommandListenerAdapter());

        uut.dismiss(overlayContainer, OVERLAY_ID_2, new CommandListenerAdapter());
        assertThat(overlayContainer.getVisibility()).isEqualTo(View.VISIBLE);
        assertThat(overlayContainer.getParent()).isEqualTo(contentLayout);
        uut.dismiss(overlayContainer, OVERLAY_ID_1, new CommandListenerAdapter());
        assertThat(overlayContainer.getVisibility()).isEqualTo(View.GONE);
    }
}
