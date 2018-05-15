package com.reactnativenavigation.parse;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import com.reactnativenavigation.parse.params.Color;
import com.reactnativenavigation.parse.params.NullColor;
import com.reactnativenavigation.parse.params.NullNumber;
import com.reactnativenavigation.parse.params.NullText;
import com.reactnativenavigation.parse.parsers.ColorParser;
import com.reactnativenavigation.utils.TypefaceLoader;

import org.json.JSONObject;

public class Options {

    @NonNull
    public static Options parse(TypefaceLoader typefaceManager, JSONObject json) {
        return parse(typefaceManager, json, new Options());
    }

    @NonNull
    public static Options parse(TypefaceLoader typefaceManager, JSONObject json, @NonNull Options defaultOptions) {
        Options result = new Options();
        if (json == null) return result.withDefaultOptions(defaultOptions);

        result.orientationOptions = OrientationOptions.parse(json);
        result.topBar = TopBarOptions.parse(typefaceManager, json.optJSONObject("topBar"));
        result.topTabsOptions = TopTabsOptions.parse(json.optJSONObject("topTabs"));
        result.topTabOptions = TopTabOptions.parse(typefaceManager, json.optJSONObject("topTab"));
        result.bottomTabOptions = BottomTabOptions.parse(json.optJSONObject("bottomTab"));
        result.bottomTabsOptions = BottomTabsOptions.parse(json.optJSONObject("bottomTabs"));
        result.overlayOptions = OverlayOptions.parse(json.optJSONObject("overlay"));
        result.fabOptions = FabOptions.parse(json.optJSONObject("fab"));
        result.sideMenuRootOptions = SideMenuRootOptions.parse(json.optJSONObject("sideMenu"));
        result.animations = AnimationsOptions.parse(json.optJSONObject("animations"));
        result.screenBackgroundColor = ColorParser.parse(json, "screenBackgroundColor");
        result.modal = ModalOptions.parse(json);
        result.statusBar = StatusBarOptions.parse(json);

        return result.withDefaultOptions(defaultOptions);
    }

    @NonNull public OrientationOptions orientationOptions = new OrientationOptions();
    @NonNull public TopBarOptions topBar = new TopBarOptions();
    @NonNull public TopTabsOptions topTabsOptions = new TopTabsOptions();
    @NonNull public TopTabOptions topTabOptions = new TopTabOptions();
    @NonNull public BottomTabOptions bottomTabOptions = new BottomTabOptions();
    @NonNull public BottomTabsOptions bottomTabsOptions = new BottomTabsOptions();
    @NonNull public OverlayOptions overlayOptions = new OverlayOptions();
    @NonNull public FabOptions fabOptions = new FabOptions();
    @NonNull public AnimationsOptions animations = new AnimationsOptions();
    @NonNull public SideMenuRootOptions sideMenuRootOptions = new SideMenuRootOptions();
    @NonNull public Color screenBackgroundColor = new NullColor();
    @NonNull public ModalOptions modal = new ModalOptions();
    @NonNull public StatusBarOptions statusBar = new StatusBarOptions();

    void setTopTabIndex(int i) {
        topTabOptions.tabIndex = i;
    }

    @CheckResult
    public Options copy() {
        Options result = new Options();
        result.orientationOptions.mergeWith(orientationOptions);
        result.topBar.mergeWith(topBar);
        result.topTabsOptions.mergeWith(topTabsOptions);
        result.topTabOptions.mergeWith(topTabOptions);
        result.bottomTabOptions.mergeWith(bottomTabOptions);
        result.bottomTabsOptions.mergeWith(bottomTabsOptions);
        result.overlayOptions = overlayOptions;
        result.fabOptions.mergeWith(fabOptions);
        result.sideMenuRootOptions.mergeWith(sideMenuRootOptions);
        result.animations.mergeWith(animations);
        result.screenBackgroundColor = screenBackgroundColor;
        result.modal.mergeWith(modal);
        result.statusBar.mergeWith(statusBar);
        return result;
    }

    @CheckResult
	public Options mergeWith(final Options other) {
        Options result = copy();
        result.orientationOptions.mergeWith(other.orientationOptions);
        result.topBar.mergeWith(other.topBar);
        result.topTabsOptions.mergeWith(other.topTabsOptions);
        result.topTabOptions.mergeWith(other.topTabOptions);
        result.bottomTabOptions.mergeWith(other.bottomTabOptions);
        result.bottomTabsOptions.mergeWith(other.bottomTabsOptions);
        result.fabOptions.mergeWith(other.fabOptions);
        result.animations.mergeWith(other.animations);
        result.sideMenuRootOptions.mergeWith(other.sideMenuRootOptions);
        if (other.screenBackgroundColor.hasValue()) result.screenBackgroundColor = other.screenBackgroundColor;
        result.modal.mergeWith(other.modal);
        result.statusBar.mergeWith(other.statusBar);
        return result;
    }

    Options withDefaultOptions(final Options defaultOptions) {
        orientationOptions.mergeWithDefault(defaultOptions.orientationOptions);
        topBar.mergeWithDefault(defaultOptions.topBar);
        topTabOptions.mergeWithDefault(defaultOptions.topTabOptions);
        topTabsOptions.mergeWithDefault(defaultOptions.topTabsOptions);
        bottomTabOptions.mergeWithDefault(defaultOptions.bottomTabOptions);
        bottomTabsOptions.mergeWithDefault(defaultOptions.bottomTabsOptions);
        fabOptions.mergeWithDefault(defaultOptions.fabOptions);
        animations.mergeWithDefault(defaultOptions.animations);
        sideMenuRootOptions.mergeWithDefault(defaultOptions.sideMenuRootOptions);
        if (!screenBackgroundColor.hasValue()) screenBackgroundColor = defaultOptions.screenBackgroundColor;
        modal.mergeWithDefault(defaultOptions.modal);
        statusBar.mergeWithDefault(defaultOptions.statusBar);
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
        topTabsOptions = new TopTabsOptions();
        return this;
    }

    public Options clearBottomTabOptions() {
        bottomTabOptions = new BottomTabOptions();
        return this;
    }

    public Options clearSideMenuOptions() {
        sideMenuRootOptions = new SideMenuRootOptions();
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

    public void clearOneTimeOptions() {
        bottomTabsOptions.currentTabId = new NullText();
        bottomTabsOptions.currentTabIndex = new NullNumber();
    }
}
