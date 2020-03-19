package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.TestUtils;
import com.reactnativenavigation.views.titlebar.TitleBar;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;

import androidx.appcompat.widget.ActionMenuView;

import static com.reactnativenavigation.utils.Assertions.assertNotNull;
import static com.reactnativenavigation.utils.ViewUtils.findChildByClass;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TitleBarTest extends BaseTest {

    private TitleBar uut;
    private Activity activity;

    @Override
    public void beforeEach() {
        activity = newActivity();
        uut = spy(new TitleBar(activity));
    }

    @Test
    public void setLeftButton_titleIsAligned() {
        uut.setTitle("Title");
        TextView title = new TextView(activity);
        uut.addView(title);
        when(uut.findTitleTextView()).thenReturn(title);

        uut.setLeftButtons(Collections.singletonList(Mockito.mock(TitleBarButtonController.class)));
        dispatchPreDraw(title);
        verify(uut).alignTextView(any(), eq(title));
    }

    @Test
    public void setComponent_addsComponentToTitleBar() {
        View component = new View(activity);
        uut.setComponent(component);
        verify(uut).addView(component);
    }

    @Test
    public void setComponent_doesNothingIfComponentIsAlreadyAdded() {
        View component = new View(activity);
        uut.setComponent(component);

        uut.setComponent(component);
        verify(uut).addView(component);
    }

    @Test
    public void addView_overflowIsEnabledForButtonsContainer() {
        ActionMenuView buttonsContainer = mock(ActionMenuView.class);
        uut.addView(buttonsContainer);
        verify(buttonsContainer).setClipChildren(false);
    }

    @Test
    public void clear() {
        View title = new View(activity);
        uut.setComponent(title);
        verify(uut).addView(title);

        uut.clear();
        assertThat(uut.getTitle()).isNullOrEmpty();
        assertThat(uut.getMenu().size()).isZero();
        assertThat(uut.getNavigationIcon()).isNull();
        assertThat(title.getParent()).isNull();
    }

    @Test
    public void setLayoutDirection_directionIsExplicitlyAppliedToButtonsContainer() {
        ActionMenuView buttonsContainer = findChildByClass(uut, ActionMenuView.class);
        assertNotNull(buttonsContainer);
        ActionMenuView spy = TestUtils.spyOn(buttonsContainer);
        uut.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        verify(spy).setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
    }
}
