package com.reactnativenavigation.viewcontrollers.bottomtabs;

import android.animation.Animator;
import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.reactnativenavigation.options.BottomTabOptions;
import com.reactnativenavigation.options.Options;
import com.reactnativenavigation.react.CommandListener;
import com.reactnativenavigation.react.events.EventEmitter;
import com.reactnativenavigation.utils.ImageLoader;
import com.reactnativenavigation.viewcontrollers.bottomtabs.attacher.BottomTabsAttacher;
import com.reactnativenavigation.viewcontrollers.child.ChildControllersRegistry;
import com.reactnativenavigation.viewcontrollers.parent.ParentController;
import com.reactnativenavigation.viewcontrollers.viewcontroller.Presenter;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController;
import com.reactnativenavigation.views.bottomtabs.BottomTabs;
import com.reactnativenavigation.views.bottomtabs.BottomTabsContainer;
import com.reactnativenavigation.views.bottomtabs.BottomTabsLayout;

import java.util.Collection;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.reactnativenavigation.utils.CollectionUtils.*;
import static com.reactnativenavigation.utils.ObjectUtils.perform;

public class BottomTabsController extends ParentController<BottomTabsLayout> implements AHBottomNavigation.OnTabSelectedListener, TabSelector {

    private BottomTabsContainer bottomTabsContainer;
    private BottomTabs bottomTabs;
    private final List<ViewController<?>> tabs;
    private final EventEmitter eventEmitter;
    private final ImageLoader imageLoader;
    private final BottomTabsAttacher tabsAttacher;
    private final BottomTabsPresenter presenter;
    private final BottomTabPresenter tabPresenter;

    public BottomTabsAnimator getAnimator() {
        return presenter.getAnimator();
    }


    public BottomTabsController(Activity activity, List<ViewController<?>> tabs, ChildControllersRegistry childRegistry, EventEmitter eventEmitter, ImageLoader imageLoader, String id, Options initialOptions, Presenter presenter, BottomTabsAttacher tabsAttacher, BottomTabsPresenter bottomTabsPresenter, BottomTabPresenter bottomTabPresenter) {
        super(activity, childRegistry, id, presenter, initialOptions);
        this.tabs = tabs;
        this.eventEmitter = eventEmitter;
        this.imageLoader = imageLoader;
        this.tabsAttacher = tabsAttacher;
        this.presenter = bottomTabsPresenter;
        this.tabPresenter = bottomTabPresenter;
        forEach(tabs, tab -> tab.setParentController(this));
    }

    @Override
    public void setDefaultOptions(Options defaultOptions) {
        super.setDefaultOptions(defaultOptions);
        presenter.setDefaultOptions(defaultOptions);
        tabPresenter.setDefaultOptions(defaultOptions);
    }

    @NonNull
    @Override
    public BottomTabsLayout createView() {
        BottomTabsLayout root = new BottomTabsLayout(getActivity());
        this.bottomTabsContainer = createBottomTabsContainer();
        this.bottomTabs = bottomTabsContainer.getBottomTabs();
        Options resolveCurrentOptions = resolveCurrentOptions();
        tabsAttacher.init(root, resolveCurrentOptions);
        presenter.bindView(bottomTabsContainer, this);
        tabPresenter.bindView(bottomTabs);
        bottomTabs.setOnTabSelectedListener(this);
        root.addBottomTabsContainer(bottomTabsContainer);
        bottomTabs.addItems(createTabs());
        setInitialTab(resolveCurrentOptions);
        tabsAttacher.attach();
        return root;
    }

    private void setInitialTab(Options resolveCurrentOptions) {
        int initialTabIndex = 0;
        if (resolveCurrentOptions.bottomTabsOptions.currentTabId.hasValue())
            initialTabIndex = presenter.findTabIndexByTabId(resolveCurrentOptions.bottomTabsOptions.currentTabId.get());
        else if (resolveCurrentOptions.bottomTabsOptions.currentTabIndex.hasValue()) {
            initialTabIndex = resolveCurrentOptions.bottomTabsOptions.currentTabIndex.get();
        }
        bottomTabs.setCurrentItem(initialTabIndex, false);
    }

    @NonNull
    protected BottomTabsContainer createBottomTabsContainer() {
        return new BottomTabsContainer(getActivity(), createBottomTabs());
    }

    @NonNull
    protected BottomTabs createBottomTabs() {
        return new BottomTabs(getActivity());
    }

    @Override
    public void applyOptions(Options options) {
        super.applyOptions(options);
        bottomTabs.disableItemsCreation();
        presenter.applyOptions(options);
        tabPresenter.applyOptions();
        bottomTabs.enableItemsCreation();
        this.options.bottomTabsOptions.clearOneTimeOptions();
        this.initialOptions.bottomTabsOptions.clearOneTimeOptions();
    }

    @Override
    public void mergeOptions(Options options) {
        presenter.mergeOptions(options, this);
        tabPresenter.mergeOptions(options);
        super.mergeOptions(options);
        this.options.bottomTabsOptions.clearOneTimeOptions();
        this.initialOptions.bottomTabsOptions.clearOneTimeOptions();
    }

    @Override
    public void applyChildOptions(Options options, ViewController child) {
        super.applyChildOptions(options, child);
        presenter.applyChildOptions(resolveCurrentOptions(), child);
        performOnParentController(parent -> parent.applyChildOptions(
                this.options.copy()
                        .clearBottomTabsOptions()
                        .clearBottomTabOptions(),
                child
                )
        );
    }

    @Override
    public void mergeChildOptions(Options options, ViewController child) {
        super.mergeChildOptions(options, child);
        presenter.mergeChildOptions(options, child);
        tabPresenter.mergeChildOptions(options, child);
        performOnParentController(parent -> parent.mergeChildOptions(options.copy().clearBottomTabsOptions(), child));
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
    public ViewController<?> getCurrentChild() {
        return tabs.get(bottomTabs == null ? 0 : bottomTabs.getCurrentItem());
    }

    @Override
    public boolean onTabSelected(int index, boolean wasSelected) {
        BottomTabOptions options = tabs.get(index).resolveCurrentOptions().bottomTabOptions;

        eventEmitter.emitBottomTabPressed(index);

        if (options.selectTabOnPress.get(true)) {
            eventEmitter.emitBottomTabSelected(bottomTabs.getCurrentItem(), index);
            if (wasSelected) return false;
            selectTab(index);
        }
        return false;
    }

    private List<AHBottomNavigationItem> createTabs() {
        if (tabs.size() > 5) throw new RuntimeException("Too many tabs!");
        return map(tabs, tab -> {
            BottomTabOptions options = tab.resolveCurrentOptions().bottomTabOptions;
            return new AHBottomNavigationItem(
                    options.text.get(""),
                    imageLoader.loadIcon(getActivity(), options.icon.get(null)),
                    imageLoader.loadIcon(getActivity(), options.selectedIcon.get(null)),
                    options.testId.get("")
            );
        });
    }

    int getSelectedIndex() {
        return bottomTabs.getCurrentItem();
    }

    @Override
    public boolean onMeasureChild(CoordinatorLayout parent, ViewGroup child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        perform(findController(child), ViewController::applyBottomInset);
        return super.onMeasureChild(parent, child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);
    }

    @Override
    public int getBottomInset(ViewController child) {
        return presenter.getBottomInset(resolveChildOptions(child)) + perform(getParentController(), 0, p -> p.getBottomInset(this));
    }

    @Override
    public void applyBottomInset() {
        presenter.applyBottomInset(getBottomInset());
        super.applyBottomInset();
    }

    @NonNull

    @Override
    public Collection<ViewController<?>> getChildControllers() {
        return tabs;
    }

    @Override
    public void destroy() {
        tabsAttacher.destroy();
        super.destroy();
    }

    @Override
    public void selectTab(final int newIndex) {
        tabsAttacher.onTabSelected(tabs.get(newIndex));
        getCurrentView().setVisibility(View.INVISIBLE);
        bottomTabs.setCurrentItem(newIndex, false);
        getCurrentView().setVisibility(View.VISIBLE);
        getCurrentChild().onViewDidAppear();
    }

    @NonNull
    private ViewGroup getCurrentView() {
        return tabs.get(bottomTabs.getCurrentItem()).getView();
    }

    public Animator getPushAnimation(Options appearingOptions) {
        return presenter.getPushAnimation(appearingOptions);
    }

    public Animator getSetStackRootAnimation(Options appearingOptions) {
        return presenter.getSetStackRootAnimation(appearingOptions);
    }

    public Animator getPopAnimation(Options appearingOptions, Options disappearingOptions) {
        return presenter.getPopAnimation(appearingOptions, disappearingOptions);
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public BottomTabs getBottomTabs() {
        return bottomTabs;
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public BottomTabsContainer getBottomTabsContainer() {
        return bottomTabsContainer;
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public void setBottomTabsContainer(BottomTabsContainer bottomTabsContainer) {
        this.bottomTabsContainer = bottomTabsContainer;
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public void setBottomTabs(BottomTabs bottomTabs) {
        this.bottomTabs = bottomTabs;
    }
}
