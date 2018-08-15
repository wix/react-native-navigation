package com.reactnativenavigation.viewcontrollers.bottomtabs;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.reactnativenavigation.parse.BottomTabOptions;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.presentation.BottomTabOptionsPresenter;
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
    private BottomTabOptionsPresenter tabPresenter;

    public BottomTabsController(Activity activity, List<ViewController> tabs, ChildControllersRegistry childRegistry, EventEmitter eventEmitter, ImageLoader imageLoader, String id, Options initialOptions, OptionsPresenter presenter, BottomTabsOptionsPresenter bottomTabsPresenter, BottomTabOptionsPresenter bottomTabPresenter) {
		super(activity, childRegistry, id, presenter, initialOptions);
        this.tabs = tabs;
        this.eventEmitter = eventEmitter;
        this.imageLoader = imageLoader;
        this.presenter = bottomTabsPresenter;
        this.tabPresenter = bottomTabPresenter;
    }

    @Override
    public void setDefaultOptions(Options defaultOptions) {
        super.setDefaultOptions(defaultOptions);
        presenter.setDefaultOptions(defaultOptions);
        tabPresenter.setDefaultOptions(defaultOptions);
    }

    @NonNull
	@Override
	protected ViewGroup createView() {
		RelativeLayout root = new RelativeLayout(getActivity());
		bottomTabs = new BottomTabs(getActivity());
        presenter.bindView(bottomTabs, this);
        tabPresenter.bindView(bottomTabs);
        bottomTabs.setOnTabSelectedListener(this);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
		lp.addRule(ALIGN_PARENT_BOTTOM);
		root.addView(bottomTabs, lp);
		createTabs(root);
		return root;
	}

    @Override
    public void applyOptions(Options options) {
        super.applyOptions(options);
        presenter.present(options);
        tabPresenter.present();
    }

    @Override
    public void applyChildOptions(Options options, Component child) {
        super.applyChildOptions(options, child);
        presenter.applyChildOptions(this.options, child);
        performOnParentController(parentController ->
                ((ParentController) parentController).applyChildOptions(this.options.copy().clearBottomTabsOptions().clearBottomTabOptions(), child)
        );
    }

    @Override
    public void mergeChildOptions(Options options, Component child) {
        super.mergeChildOptions(options, child);
        presenter.mergeChildOptions(options, child);
        tabPresenter.mergeChildOptions(options, child);
        performOnParentController(parentController ->
                ((ParentController) parentController).mergeChildOptions(options.copy().clearBottomTabsOptions(), child)
        );
    }

    @Override
	public boolean handleBack(CommandListener listener) {
		return !tabs.isEmpty() && tabs.get(bottomTabs.getCurrentItem()).handleBack(listener);
	}

    @Override
    public void sendOnNavigationButtonPressed(String buttonId) {
        getCurrentChild().sendOnNavigationButtonPressed(buttonId);
    }

    @Override
    protected ViewController getCurrentChild() {
        return tabs.get(bottomTabs.getCurrentItem());
    }

    @Override
    public boolean onTabSelected(int index, boolean wasSelected) {
        if (wasSelected) return false;
        eventEmitter.emitBottomTabSelected(bottomTabs.getCurrentItem(), index);
        selectTab(index);
        return false;
	}

	private void createTabs(RelativeLayout root) {
		if (tabs.size() > 5) {
			throw new RuntimeException("Too many tabs!");
		}
        List<String> icons = new ArrayList<>();
        List<BottomTabOptions> bottomTabOptionsList = new ArrayList<>();
        for (int i = 0; i < tabs.size(); i++) {
            tabs.get(i).setParentController(this);
            BottomTabOptions tabOptions = tabs.get(i).resolveCurrentOptions().bottomTabOptions;
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
                    tabs.add(new AHBottomNavigationItem(bottomTabOptionsList.get(i).text.get(""), drawables.get(i)));
                }
                bottomTabs.addItems(tabs);
                bottomTabs.post(() -> {
                    for (int i = 0; i < bottomTabOptionsList.size(); i++) {
                        bottomTabs.setTabTestId(i, bottomTabOptionsList.get(i).testId);
                    }
                });
                attachTabs(root);
            }

            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
            }
        });
	}

    private void attachTabs(RelativeLayout root) {
        for (int i = (tabs.size() - 1); i >= 0; i--) {
            ViewGroup tab = tabs.get(i).getView();
            tab.setLayoutParams(new RelativeLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
            Options options = resolveCurrentOptions();
            presenter.applyLayoutParamsOptions(options, i);
            if (i != 0) tab.setVisibility(View.INVISIBLE);
            root.addView(tab);
        }
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
        getCurrentView().setVisibility(View.INVISIBLE);
        bottomTabs.setCurrentItem(newIndex, false);
        getCurrentView().setVisibility(View.VISIBLE);
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
