package com.reactnativenavigation.views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.reactnativenavigation.R;
import com.reactnativenavigation.activities.BaseReactActivity;
import com.reactnativenavigation.core.objects.Button;
import com.reactnativenavigation.core.objects.Screen;
import com.reactnativenavigation.utils.ContextProvider;
import com.reactnativenavigation.utils.ImageUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by guyc on 09/04/16.
 */
public class RnnToolBar extends Toolbar {

    private static final int ANIMATE_DURATION = 180;

    private List<Screen> mScreens;
    private AsyncTask mSetupToolbarTask;
    private Drawable mBackground;
    private ArrayList<View> mMenuItems;

    public RnnToolBar(Context context) {
        super(context);
        init();
    }

    public RnnToolBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RnnToolBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mMenuItems = new ArrayList<>();
        mBackground = getBackground();
    }

    public void setScreens(List<Screen> screens) {
        mScreens = screens;
    }

    public void setStyle(Screen screen) {
        if (screen.toolBarColor != null) {
            setBackgroundColor(screen.toolBarColor);
        } else {
            resetBackground();
        }

        if (screen.navBarTextColor != null) {
            setTitleTextColor(screen.navBarTextColor);
        } else {
            resetTitleTextColor();
        }

        if (screen.toolBarHidden != null && screen.toolBarHidden) {
            hideToolbar();
        } else {
            showToolbar();
        }
    }

    private void resetBackground() {
        setBackground(mBackground);
    }

    private void resetTitleTextColor() {
        setTitleTextColor(ContextCompat.getColor(getContext(), android.R.color.primary_text_light));
    }

    public void handleOnCreateOptionsMenuAsync() {
        setupToolbarButtonsAsync(null, mScreens.get(0));
    }

    public void setupToolbarButtonsAsync(Screen newScreen) {
        this.setupToolbarButtonsAsync(null, newScreen);
    }


    public void setupToolbarButtonsAsync(Screen oldScreen, Screen newScreen) {
        if (mSetupToolbarTask == null) {
            mSetupToolbarTask = new SetupToolbarButtonsTask(this, oldScreen, newScreen).execute();
        }
    }

    public void showToolbar(boolean animated) {
        ActionBar actionBar = ((AppCompatActivity) getContext()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setShowHideAnimationEnabled(animated);
            // We hide the ToolBar's parent (AppBarLayout) since this animates the shadow added by AppBar as well
            ((View) getParent()).setVisibility(VISIBLE);
        }
    }

    public void hideToolbar(boolean animated) {
        ActionBar actionBar = ((AppCompatActivity) getContext()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setShowHideAnimationEnabled(animated);
            // We hide the ToolBar's parent (AppBarLayout) since this animates the shadow added by AppBar as well
            ((View) getParent()).setVisibility(GONE);
        }
    }

    private void showToolbar() {
        showToolbar(false);
    }

    private void hideToolbar() {
        hideToolbar(false);
    }

    @SuppressWarnings({"ConstantConditions"})
    public void showBackButton(Screen screen) {
        ActionBar actionBar = ContextProvider.getActivityContext().getSupportActionBar();
        Drawable backButton = setupBackButton(screen);
        actionBar.setHomeAsUpIndicator(backButton);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @SuppressLint("PrivateResource")
    @SuppressWarnings({"ConstantConditions"})
    private Drawable setupBackButton(Screen screen) {
        Resources resources = getResources();
        final Drawable backButton = ResourcesCompat.getDrawable(resources,
                R.drawable.abc_ic_ab_back_mtrl_am_alpha,
                ContextProvider.getActivityContext().getTheme());
        int tintColor = screen.navBarButtonColor != null ? screen.navBarButtonColor : Color.BLACK;
        ImageUtils.tint(backButton, tintColor);
        return backButton;
    }

    @SuppressWarnings({"ConstantConditions"})
    public void hideBackButton() {
        ContextProvider.getActivityContext().getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    /**
     * Update the ToolBar from screen. This function sets any properties that are defined
     * in the screen.
     * @param screen The currently displayed screen
     */
    @UiThread
    public void update(@NonNull Screen screen) {
        ((AppCompatActivity) getContext()).setSupportActionBar(this);
        setTitle(screen.title);
        setStyle(screen);
    }

    public void updateAndSetButtons(Screen screen) {
        update(screen);
        setupToolbarButtonsAsync(screen);
    }

    private static class SetupToolbarButtonsTask extends AsyncTask<Void, Void, Map<String, Drawable>> {
        private final List<Button> mOldButtons;
        private final List<Button> mNewButtons;
        private final WeakReference<RnnToolBar> mToolbarWR;
        @ColorInt private final Integer mTintColor;
        private final int mIconDimensions;

        public SetupToolbarButtonsTask(RnnToolBar toolBar, Screen oldScreen, Screen newScreen) {
            mToolbarWR = new WeakReference<>(toolBar);
            mOldButtons = oldScreen == null ? null : oldScreen.getButtons();
            mNewButtons = newScreen.getButtons();
            mTintColor = newScreen.navBarButtonColor;
            mIconDimensions = (int) (toolBar.getHeight() * 0.4f);
        }

        @Override
        protected Map<String, Drawable> doInBackground(Void... params) {
            Context context = ContextProvider.getActivityContext();
            if (context == null) {
                return null;
            }

            Map<String, Drawable> icons = new HashMap<>();
            for (Button button : mNewButtons) {
                if (button.hasIcon()) {
                    icons.put(button.id, button.getIcon(context, mIconDimensions));
                }
            }
            return icons;
        }

        @Override
        protected void onPostExecute(Map<String, Drawable> icons) {
            final Context context = ContextProvider.getActivityContext();
            if (context == null) {
                return;
            }

            Menu menu = ((BaseReactActivity) context).getMenu();
            if (menu == null) {
                RnnToolBar toolBar = mToolbarWR.get();
                if (toolBar != null) {
                    toolBar.mSetupToolbarTask = null;
                }
                mToolbarWR.clear();
                return;
            }

            // Remove prev screen buttons
            if(mOldButtons == null) {
                menu.clear();
            } else {
                for (Button btn : mOldButtons) {
                    menu.removeItem(btn.getItemId());
                }
            }

            // Add new screen buttons
            final List<String> textButtons = new ArrayList<>();
            final int size = mNewButtons.size();
            for (int i = 0; i < size; i++) {
                Button button = mNewButtons.get(i);
                MenuItem item = menu.add(Menu.NONE, button.getItemId(), size - i - 1, button.title);
                item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

                // Set button icon
                if (button.hasIcon()) {
                    Drawable icon = icons.get(button.id);
                    if (mTintColor != null) {
                        ImageUtils.tint(icon, mTintColor);
                    }
                    item.setIcon(icon);
                } else {
                    textButtons.add(button.title);
                }

                // Disable button if needed
                if (button.disabled) {
                    item.setEnabled(false);
                }
            }

            final RnnToolBar toolBar = mToolbarWR.get();
            if (toolBar != null) {
                // Tint overflow icon which appears when there's not enough space in Toolbar for icons
                if (mTintColor != null) {
                    ImageUtils.tint(toolBar.getOverflowIcon(), mTintColor);
                }

                // Tint text buttons
                if (textButtons.size() > 0 && mTintColor != null) {
                    final View decorView = ((Activity) context).getWindow().getDecorView();
                    decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            decorView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                            // Find TextViews
                            for (String text : textButtons) {
                                decorView.findViewsWithText(toolBar.mMenuItems, text, View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                            }

                            // Set text color
                            for (View button : toolBar.mMenuItems) {
                                ((TextView) button).setTextColor(mTintColor);
                            }

                            toolBar.mMenuItems.clear();
                        }
                    });
                }

                toolBar.mSetupToolbarTask = null;
                mToolbarWR.clear();
            }
        }
    }
}
