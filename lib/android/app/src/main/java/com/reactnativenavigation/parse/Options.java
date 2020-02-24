package com.reactnativenavigation.parse;

import com.reactnativenavigation.parse.params.NullNumber;
import com.reactnativenavigation.parse.params.NullText;
import com.reactnativenavigation.utils.TypefaceLoader;

import org.json.JSONObject;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;

public class Options {
    public static final Options EMPTY = new Options();

    @NonNull
    public static Options parse(TypefaceLoader typefaceManager, JSONObject json) {
        Options result = new Options();
        if (json == null) return result;

        result.topBar = TopBarOptions.parse(typefaceManager, json.optJSONObject("topBar"));
        result.topTabs = TopTabsOptions.parse(json.optJSONObject("topTabs"));
        result.topTabOptions = TopTabOptions.parse(typefaceManager, json.optJSONObject("topTab"));
        result.bottomTabOptions = BottomTabOptions.parse(typefaceManager, json.optJSONObject("bottomTab"));
        result.bottomTabsOptions = BottomTabsOptions.parse(json.optJSONObject("bottomTabs"));
        result.overlayOptions = OverlayOptions.parse(json.optJSONObject("overlay"));
        result.fabOptions = FabOptions.parse(json.optJSONObject("fab"));
        result.sideMenuRootOptions = SideMenuRootOptions.parse(json.optJSONObject("sideMenu"));
        result.animations = AnimationsOptions.parse(json.optJSONObject("animations"));
        result.modal = ModalOptions.parse(json);
        result.navigationBar = NavigationBarOptions.parse(json.optJSONObject("navigationBar"));
        result.statusBar = StatusBarOptions.parse(json.optJSONObject("statusBar"));
        result.layout = LayoutOptions.parse(json.optJSONObject("layout"));

        return result;
    }

    @NonNull public TopBarOptions topBar = new TopBarOptions();
    @NonNull public TopTabsOptions topTabs = new TopTabsOptions();
    @NonNull public TopTabOptions topTabOptions = new TopTabOptions();
    @NonNull public BottomTabOptions bottomTabOptions = new BottomTabOptions();
    @NonNull public BottomTabsOptions bottomTabsOptions = new BottomTabsOptions();
    @NonNull public OverlayOptions overlayOptions = new OverlayOptions();
    @NonNull public FabOptions fabOptions = new FabOptions();
    @NonNull public AnimationsOptions animations = new AnimationsOptions();
    @NonNull public SideMenuRootOptions sideMenuRootOptions = new SideMenuRootOptions();
    @NonNull public ModalOptions modal = new ModalOptions();
    @NonNull public NavigationBarOptions navigationBar = new NavigationBarOptions();
    @NonNull public StatusBarOptions statusBar = new StatusBarOptions();
    @NonNull public LayoutOptions layout = new LayoutOptions();

    void setTopTabIndex(int i) {
        topTabOptions.tabIndex = i;
    }

    @CheckResult
    public Options copy() {
        Options result = new Options();
        result.topBar.mergeWith(topBar);
        result.topTabs.mergeWith(topTabs);
        result.topTabOptions.mergeWith(topTabOptions);
        result.bottomTabOptions.mergeWith(bottomTabOptions);
        result.bottomTabsOptions.mergeWith(bottomTabsOptions);
        result.overlayOptions = overlayOptions;
        result.fabOptions.mergeWith(fabOptions);
        result.sideMenuRootOptions.mergeWith(sideMenuRootOptions);
        result.animations.mergeWith(animations);
        result.modal.mergeWith(modal);
        result.navigationBar.mergeWith(navigationBar);
        result.statusBar.mergeWith(statusBar);
        result.layout.mergeWith(layout);
        return result;
    }

    @CheckResult
	public Options mergeWith(final Options other) {
        Options result = copy();
        result.topBar.mergeWith(other.topBar);
        result.topTabs.mergeWith(other.topTabs);
        result.topTabOptions.mergeWith(other.topTabOptions);
        result.bottomTabOptions.mergeWith(other.bottomTabOptions);
        result.bottomTabsOptions.mergeWith(other.bottomTabsOptions);
        result.fabOptions.mergeWith(other.fabOptions);
        result.animations.mergeWith(other.animations);
        result.sideMenuRootOptions.mergeWith(other.sideMenuRootOptions);
        result.modal.mergeWith(other.modal);
        result.navigationBar.mergeWith(other.navigationBar);
        result.statusBar.mergeWith(other.statusBar);
        result.layout.mergeWith(other.layout);
        return result;
    }

    public Options withDefaultOptions(final Options defaultOptions) {
        topBar.mergeWithDefault(defaultOptions.topBar);
        topTabOptions.mergeWithDefault(defaultOptions.topTabOptions);
        topTabs.mergeWithDefault(defaultOptions.topTabs);
        bottomTabOptions.mergeWithDefault(defaultOptions.bottomTabOptions);
        bottomTabsOptions.mergeWithDefault(defaultOptions.bottomTabsOptions);
        fabOptions.mergeWithDefault(defaultOptions.fabOptions);
        animations.mergeWithDefault(defaultOptions.animations);
        sideMenuRootOptions.mergeWithDefault(defaultOptions.sideMenuRootOptions);
        modal.mergeWithDefault(defaultOptions.modal);
        navigationBar.mergeWithDefault(defaultOptions.navigationBar);
        statusBar.mergeWithDefault(defaultOptions.statusBar);
        layout.mergeWithDefault(defaultOptions.layout);
        return this;
    }

    public Options clearTopBarOptions() {
        topBar = new TopBarOptions();
        return this;
    }

    public Options clearBottomTabsOptions() {
        bottomTabsOptions = new BottomTabsOptions();
        return this;
    }

    public Options clearTopTabOptions() {
        topTabOptions = new TopTabOptions();
        return this;
    }

    public Options clearTopTabsOptions() {
        topTabs = new TopTabsOptions();
        return this;
    }

    public Options clearBottomTabOptions() {
        bottomTabOptions = new BottomTabOptions();
        return this;
    }

    public Options clearAnimationOptions() {
        animations = new AnimationsOptions();
        return this;
    }

    public Options clearFabOptions() {
        fabOptions = new FabOptions();
        return this;
    }

    public Options clearOneTimeOptions() {
        bottomTabsOptions.currentTabId = new NullText();
        bottomTabsOptions.currentTabIndex = new NullNumber();
        return this;
    }
}
