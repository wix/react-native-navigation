package com.reactnativenavigation.viewcontrollers.modal;

import android.app.Activity;
import android.widget.FrameLayout;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.SimpleViewController;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.utils.CommandListenerAdapter;
import com.reactnativenavigation.viewcontrollers.Navigator.CommandListener;
import com.reactnativenavigation.viewcontrollers.ViewController;

import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ModalPresenterTest extends BaseTest {
    private static final String MODAL_ID_1 = "modalId1";
    private static final String MODAL_ID_2 = "modalId2";

    private ViewController modal1;
    private ViewController modal2;
    private ModalPresenter uut;
    private FrameLayout contentLayout;

    @Override
    public void beforeEach() {
        Activity activity = newActivity();
        uut = new ModalPresenter();
        contentLayout = new FrameLayout(activity);
        activity.setContentView(contentLayout);
        uut.setContentLayout(contentLayout);
        modal1 = spy(new SimpleViewController(activity, MODAL_ID_1, new Options()));
        modal2 = spy(new SimpleViewController(activity, MODAL_ID_2, new Options()));
    }

    @Test
    public void showModal() {
        CommandListener listener = spy(new CommandListenerAdapter() {
            @Override
            public void onSuccess(String childId) {
                assertThat(modal1.getView().getParent()).isEqualTo(contentLayout);
                verify(modal1, times(1)).onViewAppeared();
            }
        });
        uut.showModal(modal1, null, listener);
        verify(listener, times(1)).onSuccess(MODAL_ID_1);
    }

    @Test
    public void showModal_previousModalIsRemovedFromHierarchy() {
        uut.showModal(modal1, null, new CommandListenerAdapter() {
            @Override
            public void onSuccess(String childId) {
                uut.showModal(modal2, modal1, new CommandListenerAdapter() {
                    @Override
                    public void onSuccess(String childId) {
                        assertThat(modal1.getView().getParent()).isNull();
                        verify(modal1, times(1)).onViewDisappear();
                    }
                });
            }
        });
    }

    @Test
    public void dismissModal() {
        uut.showModal(modal1, null, new CommandListenerAdapter());
        uut.dismissModal(modal1, null, new CommandListenerAdapter());
        verify(modal1, times(1)).onViewDisappear();
        verify(modal1, times(1)).destroy();
    }

    @Test
    public void dismissModal_previousModalIsAddedBackToHierarchy() {
        uut.showModal(modal1, null, new CommandListenerAdapter());
        uut.showModal(modal2, modal1, new CommandListenerAdapter());
        assertThat(modal1.getView().getParent()).isNull();
        uut.dismissModal(modal2, modal1, new CommandListenerAdapter());
        verify(modal1, times(2)).onViewAppeared();
    }
}
