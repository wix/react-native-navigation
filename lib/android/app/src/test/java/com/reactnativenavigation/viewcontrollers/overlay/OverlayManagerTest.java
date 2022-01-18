package com.reactnativenavigation.viewcontrollers.overlay;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.SimpleViewController;
import com.reactnativenavigation.options.Options;
import com.reactnativenavigation.options.OverlayAttachOptions;
import com.reactnativenavigation.options.OverlayOptions;
import com.reactnativenavigation.options.params.Text;
import com.reactnativenavigation.react.CommandListener;
import com.reactnativenavigation.react.CommandListenerAdapter;
import com.reactnativenavigation.viewcontrollers.child.ChildControllersRegistry;
import com.reactnativenavigation.viewcontrollers.component.ComponentViewController;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController;
import com.reactnativenavigation.views.component.ComponentLayout;
import com.reactnativenavigation.views.overlay.ViewTooltip;

import org.junit.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OverlayManagerTest extends BaseTest {
    private static final String OVERLAY_ID_1 = "OVERLAY_1";
    private static final String OVERLAY_ID_2 = "OVERLAY_2";

    private OverlayManager uut;
    private SimpleViewController overlay1;
    private SimpleViewController overlay2;
    private FrameLayout contentLayout;
    private FrameLayout overlayContainer;
    private ViewController<ViewGroup> hostViewController;
    private ViewTooltip.TooltipView tooltipView;
    private ViewTooltip.TooltipView tooltipView2;
    private View anchorView;
    private ViewGroup hostRoot;
    @Override
    public void beforeEach() {
        super.beforeEach();
        Activity activity = newActivity();
        contentLayout = new FrameLayout(activity);
        contentLayout.layout(0, 0, 1000, 1000);
        activity.setContentView(contentLayout);
        overlayContainer = new FrameLayout(activity);
        contentLayout.addView(overlayContainer);

        hostViewController = Mockito.mock(ViewController.class);
        hostRoot = spy(new FrameLayout(activity));
        anchorView = spy(new View(activity));
        tooltipView = spy(new ViewTooltip.TooltipView(activity));
        tooltipView2 = spy(new ViewTooltip.TooltipView(activity));
        Mockito.when(hostViewController.showAnchoredOverlay(any(), any(), any()))
                .thenReturn(tooltipView, tooltipView2);
        Mockito.when(hostViewController.getView()).thenReturn(hostRoot);
        ChildControllersRegistry childRegistry = new ChildControllersRegistry();
        overlay1 = spy(new SimpleViewController(activity, childRegistry, OVERLAY_ID_1, new Options()));
        overlay2 = spy(new SimpleViewController(activity, childRegistry, OVERLAY_ID_2, new Options()));
        uut = new OverlayManager();
        uut.setMainOverlayContainer(overlayContainer);
        uut.setFindController((id) -> hostViewController);
        uut.setFindAnchorView((options) -> anchorView);
    }

    @Test
    public void show_shouldAttachToProvidedLayoutId() {
        OverlayOptions options = new OverlayOptions();
        options.overlayAttachOptions = new OverlayAttachOptions();
        options.overlayAttachOptions.layoutId = new Text("maLayout");
        CommandListener listener = Mockito.mock(CommandListener.class);
        uut.show(overlay1, options, listener);
        idleMainLooper();
        verify(overlay1).onViewDidAppear();
        verify(hostRoot).addView(any(),any());
        verify(listener).onSuccess(overlay1.getId());
    }

    @Test
    public void show_shouldAttachToProvidedLayoutIdAndAnchor() {
        OverlayOptions options = new OverlayOptions();
        options.overlayAttachOptions = new OverlayAttachOptions();
        options.overlayAttachOptions.layoutId = new Text("maLayout");
        options.overlayAttachOptions.anchorId = new Text("anchor");
        options.overlayAttachOptions.gravity = new Text("top");
        CommandListener listener = Mockito.mock(CommandListener.class);
        uut.show(overlay1, options, listener);
        idleMainLooper();
        verify(overlay1).onViewDidAppear();
        verify(hostViewController).showAnchoredOverlay(anchorView,options.overlayAttachOptions,overlay1);
        verify(listener).onSuccess(overlay1.getId());
    }

    @Test
    public void show_shouldIgnoreInsetsWhenAnchored() {
        OverlayOptions options = new OverlayOptions();
        options.overlayAttachOptions = new OverlayAttachOptions();
        options.overlayAttachOptions.layoutId = new Text("maLayout");
        options.overlayAttachOptions.anchorId = new Text("anchor");
        options.overlayAttachOptions.gravity = new Text("top");
        CommandListener listener = Mockito.mock(CommandListener.class);
        ComponentViewController controller = Mockito.mock(ComponentViewController.class);
        when(controller.getId()).thenReturn("1");
        when(controller.getView()).thenReturn(Mockito.mock(ComponentLayout.class));
        uut.show(controller, options, listener);
        idleMainLooper();
        verify(controller).onViewDidAppear();
        verify(controller).ignoreInsets(true);
    }

    @Test
    public void dismiss_shouldCallCloseNowOnTooltipView() {
        OverlayOptions options = new OverlayOptions();
        options.overlayAttachOptions = new OverlayAttachOptions();
        options.overlayAttachOptions.layoutId = new Text("maLayout");
        options.overlayAttachOptions.anchorId = new Text("anchor");
        options.overlayAttachOptions.gravity = new Text("top");
        CommandListener listener1 = Mockito.mock(CommandListener.class);
        CommandListener listener2 = Mockito.mock(CommandListener.class);
        uut.show(overlay1, options, listener1);
        idleMainLooper();
        uut.dismiss(overlay1.getId(),listener2);
        verify(listener2).onSuccess(overlay1.getId());
        verify(tooltipView).closeNow();
        assertThat(uut.size()).isEqualTo(0);
    }

    @Test
    public void dismissAll_shouldClearAll() {
        OverlayOptions options = new OverlayOptions();
        options.overlayAttachOptions = new OverlayAttachOptions();
        options.overlayAttachOptions.layoutId = new Text("maLayout");
        options.overlayAttachOptions.anchorId = new Text("anchor");
        options.overlayAttachOptions.gravity = new Text("top");
        CommandListener listener1 = Mockito.mock(CommandListener.class);
        CommandListener listener2 = Mockito.mock(CommandListener.class);
        uut.show(overlay1, options, listener1);
        uut.show(overlay2, Options.EMPTY.overlayOptions, listener1);
        idleMainLooper();
        assertThat(uut.size()).isEqualTo(2);
        uut.dismissAll();
        assertThat(uut.size()).isEqualTo(0);
    }

    @Test
    public void onHostPause_shouldCallLifecycleEventsProperly() {
        OverlayOptions options = new OverlayOptions();
        options.overlayAttachOptions = new OverlayAttachOptions();
        options.overlayAttachOptions.layoutId = new Text("maLayout");
        options.overlayAttachOptions.anchorId = new Text("anchor");
        options.overlayAttachOptions.gravity = new Text("top");
        CommandListener listener1 = Mockito.mock(CommandListener.class);
        CommandListener listener2 = Mockito.mock(CommandListener.class);
        uut.show(overlay1, options, listener1);
        idleMainLooper();
        verify(overlay1).onViewDidAppear();
        uut.onHostPause();
        idleMainLooper();
        verify(overlay1).onViewDisappear();
        verify(overlay1,never()).onViewWillAppear();
    }
    @Test
    public void show_attachesOverlayContainerToContentLayout() {
        uut.show(overlay1, Options.EMPTY.overlayOptions, new CommandListenerAdapter());
        assertThat(overlayContainer.getParent()).isEqualTo(contentLayout);
        uut.show(overlay2, Options.EMPTY.overlayOptions, new CommandListenerAdapter());
    }

    @Test
    public void show() {
        CommandListenerAdapter listener = spy(new CommandListenerAdapter());
        uut.show(overlay1, Options.EMPTY.overlayOptions, listener);
        idleMainLooper();
        verify(listener).onSuccess(OVERLAY_ID_1);
        verify(overlay1).onViewDidAppear();
        assertThat(overlay1.getView().getParent()).isEqualTo(overlayContainer);
        assertMatchParent(overlay1.getView());
    }

    @Test
    public void dismiss() {
        uut.show(overlay1, Options.EMPTY.overlayOptions, new CommandListenerAdapter());
        assertThat(uut.size()).isOne();
        CommandListener listener = spy(new CommandListenerAdapter());
        uut.dismiss(overlay1.getId(), listener);
        assertThat(uut.size()).isZero();
        verify(listener, times(1)).onSuccess(OVERLAY_ID_1);
        verify(overlay1, times(1)).destroy();
    }

    @Test
    public void dismiss_rejectIfOverlayNotFound() {
        CommandListener listener = spy(new CommandListenerAdapter());
        uut.dismiss(overlay1.getId(), listener);
        verify(listener, times(1)).onError(any());
    }

    @Test
    public void dismiss_onViewReturnedToFront() {
        uut.show(overlay1, Options.EMPTY.overlayOptions, new CommandListenerAdapter());
        uut.show(overlay2, Options.EMPTY.overlayOptions, new CommandListenerAdapter());
        idleMainLooper();
        verify(overlay1, never()).onViewBroughtToFront();

        uut.dismiss(OVERLAY_ID_2, new CommandListenerAdapter());
        idleMainLooper();
        verify(overlay1).onViewBroughtToFront();
    }

    @Test
    public void dismiss_overlayContainerIsHiddenIfAllOverlaysAreDismissed() {
        uut.show(overlay1, Options.EMPTY.overlayOptions, new CommandListenerAdapter());
        uut.show(overlay2, Options.EMPTY.overlayOptions, new CommandListenerAdapter());

        uut.dismiss(OVERLAY_ID_2, new CommandListenerAdapter());
        assertThat(overlayContainer.getVisibility()).isEqualTo(View.VISIBLE);
        assertThat(overlayContainer.getParent()).isEqualTo(contentLayout);
        uut.dismiss(OVERLAY_ID_1, new CommandListenerAdapter());
        assertThat(overlayContainer.getVisibility()).isEqualTo(View.GONE);
    }

    private OverlayAttachOptions newAttachOptions() {
        final OverlayAttachOptions attachOptions = new OverlayAttachOptions();
        attachOptions.layoutId = new Text("layoutId");
        attachOptions.anchorId = new Text("anchorId");
        attachOptions.gravity = new Text("top");
        return attachOptions;
    }
}
