package com.reactnativenavigation.viewcontrollers.bottomtabs;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.reactnativenavigation.parse.BottomTabOptions;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.presentation.BottomTabsOptionsPresenter;
import com.reactnativenavigation.presentation.OptionsPresenter;
import com.reactnativenavigation.react.EventEmitter;
import com.reactnativenavigation.utils.CommandListener;
import com.reactnativenavigation.utils.ImageLoader;
import com.reactnativenavigation.utils.ImageLoadingListenerAdapter;
import com.reactnativenavigation.viewcontrollers.ChildControllersRegistry;
import com.reactnativenavigation.viewcontrollers.ParentController;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.views.BottomTabs;
import com.reactnativenavigation.views.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.RelativeLayout.ALIGN_PARENT_BOTTOM;

public class BottomTabsController extends ParentController implements AHBottomNavigation.OnTabSelectedListener, TabSelector {

	private BottomTabs bottomTabs;
	private List<ViewController> tabs;
    private EventEmitter eventEmitter;
    private ImageLoader imageLoader;
    private BottomTabsOptionsPresenter presenter;
    private final BottomTabFinder bottomTabFinder = new BottomTabFinder();

    public BottomTabsController(Activity activity, List<ViewController> tabs, ChildControllersRegistry childRegistry, EventEmitter eventEmitter, ImageLoader imageLoader, String id, Options initialOptions) {
		super(activity, childRegistry, id, new OptionsPresenter(activity), initialOptions);
        this.tabs = tabs;
        this.eventEmitter = eventEmitter;
        this.imageLoader = imageLoader;
        createTabs(tabs);
    }

	@NonNull
	@Override
	protected ViewGroup createView() {
		RelativeLayout root = new RelativeLayout(getActivity());
		bottomTabs = new BottomTabs(getActivity());
        presenter = new BottomTabsOptionsPresenter(bottomTabs, tabs, this, bottomTabFinder);
        bottomTabs.setOnTabSelectedListener(this);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
		lp.addRule(ALIGN_PARENT_BOTTOM);
		root.addView(bottomTabs, lp);
		return root;
	}

    @Override
    public void applyOptions(Options options) {
        super.applyOptions(options);
        presenter.present(options);
    }

    @Override
    public void applyChildOptions(Options options, Component child) {
        super.applyChildOptions(options, child);
        presenter.presentChildOptions(this.options, child);
        applyOnParentController(parentController ->
                ((ParentController) parentController).applyChildOptions(this.options.copy().clearBottomTabsOptions().clearBottomTabOptions(), child)
        );
    }

    @Override
    public void mergeChildOptions(Options options, Component child) {
        super.mergeChildOptions(options, child);
        presenter.presentChildOptions(options, child);
        applyOnParentController(parentController ->
                ((ParentController) parentController).mergeChildOptions(options.copy().clearBottomTabsOptions(), child)
        );
    }

    @Override
	public boolean handleBack(CommandListener listener) {
		return !tabs.isEmpty() && tabs.get(bottomTabs.getCurrentItem()).handleBack(listener);
	}

    @Override
    public void sendOnNavigationButtonPressed(String buttonId) {
        getCurrentTab().sendOnNavigationButtonPressed(buttonId);
    }

    private ViewController getCurrentTab() {
        return tabs.get(bottomTabs.getCurrentItem());
    }

    @Override
    public boolean onTabSelected(int index, boolean wasSelected) {
        if (wasSelected) return false;
        eventEmitter.emitBottomTabSelected(bottomTabs.getCurrentItem(), index);
        selectTab(index);
        return true;
	}

	private void createTabs(final List<ViewController> tabs) {
		if (tabs.size() > 5) {
			throw new RuntimeException("Too many tabs!");
		}
		this.tabs = tabs;
        bottomTabFinder.setTabs(tabs);
        getView();
        List<String> icons = new ArrayList<>();
        List<BottomTabOptions> bottomTabOptionsList = new ArrayList<>();
        for (int i = 0; i < tabs.size(); i++) {
            tabs.get(i).setParentController(this);
            BottomTabOptions tabOptions = tabs.get(i).options.bottomTabOptions;
            if (!tabOptions.icon.hasValue()) {
                throw new RuntimeException("BottomTab must have an icon");
            }
            bottomTabOptionsList.add(tabOptions);
            icons.add(tabOptions.icon.get());
        }

        imageLoader.loadIcons(getActivity(), icons, new ImageLoadingListenerAdapter() {

            @Override
            public void onComplete(@NonNull List<Drawable> drawables) {
                List<AHBottomNavigationItem> tabs = new ArrayList<>();
                for (int i = 0; i < drawables.size(); i++) {
                    tabs.add(new AHBottomNavigationItem(bottomTabOptionsList.get(i).title.get(""), drawables.get(i)));
                }
                bottomTabs.addItems(tabs);
                bottomTabs.post(() -> {
                    for (int i = 0; i < bottomTabOptionsList.size(); i++) {
                        bottomTabs.setTabTag(i, bottomTabOptionsList.get(i).testId);
                    }
                });
                selectTab(0);
            }

            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
            }
        });

	}

    public int getSelectedIndex() {
		return bottomTabs.getCurrentItem();
	}

	@NonNull
	@Override
	public Collection<ViewController> getChildControllers() {
		return tabs;
	}

    @Override
    public void selectTab(final int newIndex) {
        getView().removeView(getCurrentView());
        bottomTabs.setCurrentItem(newIndex, false);
        getView().addView(getCurrentView());
    }

    @NonNull
    private ViewGroup getCurrentView() {
        return tabs.get(bottomTabs.getCurrentItem()).getView();
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public BottomTabs getBottomTabs() {
        return bottomTabs;
    }
}
