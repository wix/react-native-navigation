package com.reactnativenavigation.viewcontrollers.stack;

import android.app.Activity;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.TestUtils;
import com.reactnativenavigation.fakes.IconResolverFake;
import com.reactnativenavigation.options.ButtonOptions;
import com.reactnativenavigation.options.params.Text;
import com.reactnativenavigation.viewcontrollers.stack.topbar.button.ButtonPresenter;
import com.reactnativenavigation.viewcontrollers.stack.topbar.button.ButtonController;
import com.reactnativenavigation.views.stack.topbar.titlebar.TitleBar;
import com.reactnativenavigation.views.stack.topbar.titlebar.TitleBarButtonCreator;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import androidx.appcompat.widget.ActionMenuView;

import static com.reactnativenavigation.utils.Assertions.assertNotNull;
import static com.reactnativenavigation.utils.ViewUtils.findChildByClass;
import static java.util.Collections.singletonList;
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

        uut.setLeftButtons(singletonList(createButtonController(createIconButton())));
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

    @Test
    public void setSubtitleFontSize_usesDpInsteadofSp() {
        TextView mockSubtitleView = mock(TextView.class);

        when(uut.findSubtitleTextView()).thenReturn(mockSubtitleView);
        uut.setSubtitleFontSize(10);

        verify(mockSubtitleView).setTextSize(eq(TypedValue.COMPLEX_UNIT_DIP), eq(10f));
    }

    @Test
    public void setTitleFontSize_usesDpInsteadofSp() {
        TextView mockTitleView = mock(TextView.class);

        when(uut.findTitleTextView()).thenReturn(mockTitleView);
        uut.setTitleFontSize(10);

        verify(mockTitleView).setTextSize(eq(TypedValue.COMPLEX_UNIT_DIP), eq(10f));
    }

    @NotNull
    private ButtonController createButtonController(ButtonOptions b) {
        return new ButtonController(
                activity,
                new ButtonPresenter(activity, b, new IconResolverFake(activity)),
                b,
                mock(TitleBarButtonCreator.class),
                mock(ButtonController.OnClickListener.class)
        );
    }

    @NotNull
    private ButtonOptions createIconButton() {
        ButtonOptions b = new ButtonOptions();
        b.id = "id";
        b.icon = new Text("");
        return b;
    }
}
