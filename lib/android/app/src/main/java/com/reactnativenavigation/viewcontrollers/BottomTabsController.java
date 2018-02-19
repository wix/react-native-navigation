package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.reactnativenavigation.parse.BottomTabOptions;
import com.reactnativenavigation.parse.BottomTabsOptions;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.Text;
import com.reactnativenavigation.presentation.BottomTabOptionsPresenter;
import com.reactnativenavigation.presentation.NavigationOptionsListener;
import com.reactnativenavigation.utils.ImageLoader;
import com.reactnativenavigation.utils.UiUtils;
import com.reactnativenavigation.views.BottomTabs;
import com.reactnativenavigation.views.ReactComponent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.RelativeLayout.ABOVE;
import static android.widget.RelativeLayout.ALIGN_PARENT_BOTTOM;
import static com.reactnativenavigation.parse.DEFAULT_VALUES.NO_INT_VALUE;

public class BottomTabsController extends ParentController implements AHBottomNavigation.OnTabSelectedListener, NavigationOptionsListener {
	private BottomTabs bottomTabs;
	private List<ViewController> tabs = new ArrayList<>();
    private ImageLoader imageLoader;

    public BottomTabsController(final Activity activity, ImageLoader imageLoader, final String id, Options initialOptions) {
		super(activity, id, initialOptions);
        this.imageLoader = imageLoader;
    }

	@NonNull
	@Override
	protected ViewGroup createView() {
		RelativeLayout root = new RelativeLayout(getActivity());
		bottomTabs = new BottomTabs(getActivity(), options.bottomTabsOptions);
        bottomTabs.setOnTabSelectedListener(this);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
		lp.addRule(ALIGN_PARENT_BOTTOM);
		root.addView(bottomTabs, lp);
		return root;
	}

    @Override
    public void applyOptions(Options options, ReactComponent childComponent) {
        super.applyOptions(options, childComponent);
        int tabIndex = findTabContainingComponent(childComponent);
        if (tabIndex >= 0) new BottomTabOptionsPresenter(bottomTabs).present(options, tabIndex);
        applyOnParentController(parentController ->
                ((ParentController) parentController).applyOptions(this.options.copy().clearBottomTabsOptions().clearBottomTabOptions(), childComponent)
        );
    }

    @Override
	public boolean handleBack() {
		return !tabs.isEmpty() && tabs.get(bottomTabs.getCurrentItem()).handleBack();
	}

    @Override
    public boolean onTabSelected(int index, boolean wasSelected) {
        if (wasSelected) return false;
        selectTabAtIndex(index);
        return true;
	}
	
	public void setTabs(final List<ViewController> tabs) {
		if (tabs.size() > 5) {
			throw new RuntimeException("Too many tabs!");
		}
		this.tabs = tabs;
		getView();
		for (int i = 0; i < tabs.size(); i++) {
		    tabs.get(i).setParentController(this);
			createTab(i, tabs.get(i).options.bottomTabOptions, tabs.get(i).options.bottomTabsOptions);
		}
		selectTabAtIndex(0);
	}

	private void createTab(int index, final BottomTabOptions tabOptions, final BottomTabsOptions bottomTabsOptions) {
	    if (!tabOptions.icon.hasValue()) {
            throw new RuntimeException("BottomTab must have an icon");
        }
        imageLoader.loadIcon(getActivity(), tabOptions.icon.get(), new ImageLoader.ImageLoadingListener() {
            @Override
            public void onComplete(@NonNull Drawable drawable) {
                setIconColor(drawable, bottomTabsOptions);
                AHBottomNavigationItem item = new AHBottomNavigationItem(tabOptions.title.get(""), drawable);
                bottomTabs.addItem(item);
                bottomTabs.post(() -> bottomTabs.setTabTag(index, tabOptions.testId));
            }

            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
            }
        });

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        params.addRule(ABOVE, bottomTabs.getId());
	}

    private void setIconColor(Drawable drawable, BottomTabsOptions options) {
        UiUtils.tintDrawable(drawable, Color.RED);
    }

    int getSelectedIndex() {
		return bottomTabs.getCurrentItem();
	}

	@NonNull
	@Override
	public Collection<ViewController> getChildControllers() {
		return tabs;
	}

	@Override
	public void mergeOptions(Options options) {
        this.options = this.options.mergeWith(options);
        if (options.bottomTabsOptions.currentTabIndex != NO_INT_VALUE) {
            selectTabAtIndex(options.bottomTabsOptions.currentTabIndex);
        }
        if (options.bottomTabsOptions.currentTabId.hasValue()) {
            Text id = options.bottomTabsOptions.currentTabId;
            for (ViewController controller : tabs) {
                if (controller.getId().equals(id.get())) {
                    selectTabAtIndex(tabs.indexOf(controller));
                }
                if (controller instanceof StackController) {
                    if (hasControlWithId((StackController) controller, id.get())) {
                        selectTabAtIndex(tabs.indexOf(controller));
                    }
                }
            }
        }
    }

    void selectTabAtIndex(final int newIndex) {
        getView().removeView(getCurrentView());
        bottomTabs.setCurrentItem(newIndex, false);
        getView().addView(getCurrentView());
    }

    @NonNull
    private ViewGroup getCurrentView() {
        return tabs.get(bottomTabs.getCurrentItem()).getView();
    }

    private boolean hasControlWithId(StackController controller, String id) {
		for (ViewController child : controller.getChildControllers()) {
			if (id.equals(child.getId())) {
				return true;
			}
			if (child instanceof StackController) {
				return hasControlWithId((StackController) child, id);
			}
		}
		return false;
	}

	@IntRange(from = -1)
    private int findTabContainingComponent(ReactComponent component) {
        for (int i = 0; i < tabs.size(); i++) {
            if (tabs.get(i).containsComponent(component)) {
                return i;
            }
        }
        return -1;
    }
}
