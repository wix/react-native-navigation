package com.reactnativenavigation.parse;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.parse.params.Color;
import com.reactnativenavigation.parse.params.NullBool;
import com.reactnativenavigation.parse.params.NullColor;
import com.reactnativenavigation.parse.parsers.BoolParser;
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
        result.topBarOptions = TopBarOptions.parse(typefaceManager, json.optJSONObject("topBar"));
        result.topTabsOptions = TopTabsOptions.parse(json.optJSONObject("topTabs"));
        result.topTabOptions = TopTabOptions.parse(typefaceManager, json.optJSONObject("topTab"));
        result.bottomTabOptions = BottomTabOptions.parse(json.optJSONObject("bottomTab"));
        result.bottomTabsOptions = BottomTabsOptions.parse(json.optJSONObject("bottomTabs"));
        result.overlayOptions = OverlayOptions.parse(json.optJSONObject("overlay"));
        result.fabOptions = FabOptions.parse(json.optJSONObject("fab"));
        result.sideMenuRootOptions = SideMenuRootOptions.parse(json.optJSONObject("sideMenu"));
        result.animationsOptions = AnimationsOptions.parse(json.optJSONObject("animations"));
        result.animated = BoolParser.parse(json, "animated");
        result.screenBackgroundColor = ColorParser.parse(json, "screenBackgroundColor");

        return result.withDefaultOptions(defaultOptions);
    }

    @NonNull public OrientationOptions orientationOptions = new OrientationOptions();
    @NonNull public TopBarOptions topBarOptions = new TopBarOptions();
    @NonNull public TopTabsOptions topTabsOptions = new TopTabsOptions();
    @NonNull public TopTabOptions topTabOptions = new TopTabOptions();
    @NonNull public BottomTabOptions bottomTabOptions = new BottomTabOptions();
    @NonNull public BottomTabsOptions bottomTabsOptions = new BottomTabsOptions();
    @NonNull public OverlayOptions overlayOptions = new OverlayOptions();
    @NonNull public FabOptions fabOptions = new FabOptions();
    @NonNull public AnimationsOptions animationsOptions = new AnimationsOptions();
    @NonNull public SideMenuRootOptions sideMenuRootOptions = new SideMenuRootOptions();
    @NonNull public Bool animated = new NullBool();
    @NonNull public Color screenBackgroundColor = new NullColor();

    void setTopTabIndex(int i) {
        topTabOptions.tabIndex = i;
    }

    @CheckResult
    public Options copy() {
        Options result = new Options();
        result.orientationOptions.mergeWith(orientationOptions);
        result.topBarOptions.mergeWith(topBarOptions);
        result.topTabsOptions.mergeWith(topTabsOptions);
        result.topTabOptions.mergeWith(topTabOptions);
        result.bottomTabOptions.mergeWith(bottomTabOptions);
        result.bottomTabsOptions.mergeWith(bottomTabsOptions);
        result.overlayOptions = overlayOptions;
        result.fabOptions.mergeWith(fabOptions);
        result.sideMenuRootOptions.mergeWith(sideMenuRootOptions);
        result.animationsOptions.mergeWith(animationsOptions);
        result.animated = animated;
        result.screenBackgroundColor = screenBackgroundColor;
        return result;
    }

    @CheckResult
	public Options mergeWith(final Options other) {
        Options result = copy();
        result.orientationOptions.mergeWith(other.orientationOptions);
        result.topBarOptions.mergeWith(other.topBarOptions);
        result.topTabsOptions.mergeWith(other.topTabsOptions);
        result.topTabOptions.mergeWith(other.topTabOptions);
        result.bottomTabOptions.mergeWith(other.bottomTabOptions);
        result.bottomTabsOptions.mergeWith(other.bottomTabsOptions);
        result.fabOptions.mergeWith(other.fabOptions);
        result.animationsOptions.mergeWith(other.animationsOptions);
        result.sideMenuRootOptions.mergeWith(other.sideMenuRootOptions);
        if (other.animated.hasValue()) result.animated = other.animated;
        if (other.screenBackgroundColor.hasValue()) {
            result.screenBackgroundColor = other.screenBackgroundColor;
        }
        return result;
    }

    Options withDefaultOptions(final Options defaultOptions) {
        orientationOptions.mergeWithDefault(defaultOptions.orientationOptions);
        topBarOptions.mergeWithDefault(defaultOptions.topBarOptions);
        topTabOptions.mergeWithDefault(defaultOptions.topTabOptions);
        topTabsOptions.mergeWithDefault(defaultOptions.topTabsOptions);
        bottomTabOptions.mergeWithDefault(defaultOptions.bottomTabOptions);
        bottomTabsOptions.mergeWithDefault(defaultOptions.bottomTabsOptions);
        fabOptions.mergeWithDefault(defaultOptions.fabOptions);
        animationsOptions.mergeWithDefault(defaultOptions.animationsOptions);
        sideMenuRootOptions.mergeWithDefault(defaultOptions.sideMenuRootOptions);
        if (!animated.hasValue()) animated = defaultOptions.animated;
        if (!screenBackgroundColor.hasValue()) {
            screenBackgroundColor = defaultOptions.screenBackgroundColor;
        }
        return this;
    }

    public Options clearTopBarOptions() {
        topBarOptions = new TopBarOptions();
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
        animationsOptions = new AnimationsOptions();
        return this;
    }

    public Options clearFabOptions() {
        fabOptions = new FabOptions();
        return this;
    }
}
