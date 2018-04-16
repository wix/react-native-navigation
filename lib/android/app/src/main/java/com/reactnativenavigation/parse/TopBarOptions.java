package com.reactnativenavigation.parse;


import android.support.annotation.Nullable;
import android.util.Log;

import com.reactnativenavigation.BuildConfig;
import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.parse.params.Button;
import com.reactnativenavigation.parse.params.NullBool;
import com.reactnativenavigation.parse.params.NullText;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.parse.parsers.BoolParser;
import com.reactnativenavigation.parse.parsers.TextParser;
import com.reactnativenavigation.utils.TypefaceLoader;

import org.json.JSONObject;

import java.util.ArrayList;

public class TopBarOptions {

    public static TopBarOptions parse(TypefaceLoader typefaceLoader, JSONObject json) {
        TopBarOptions options = new TopBarOptions();
        if (json == null) return options;

        options.title = TitleOptions.parse(typefaceLoader, json.optJSONObject("title"));
        options.subtitle = SubtitleOptions.parse(typefaceLoader, json.optJSONObject("subtitle"));
        options.background = TopBarBackgroundOptions.parse(json.optJSONObject("background"));
        options.visible = BoolParser.parse(json, "visible");
        options.animate = BoolParser.parse(json,"animate");
        options.hideOnScroll = BoolParser.parse(json,"hideOnScroll");
        options.drawBehind = BoolParser.parse(json,"drawBehind");
        options.rightButtons = Button.parseJsonArray(json.optJSONArray("rightButtons"), typefaceLoader);
        options.leftButtons = Button.parseJsonArray(json.optJSONArray("leftButtons"), typefaceLoader);
        options.testId = TextParser.parse(json, "testID");

        options.validate();
        return options;
    }

    public TitleOptions title = new TitleOptions();
    public SubtitleOptions subtitle = new SubtitleOptions();
    public Text testId = new NullText();
    public TopBarBackgroundOptions background = new TopBarBackgroundOptions();
    public Bool visible = new NullBool();
    public Bool animate = new NullBool();
    public Bool hideOnScroll = new NullBool();
    public Bool drawBehind = new NullBool();
    @Nullable public ArrayList<Button> leftButtons;
    @Nullable public ArrayList<Button> rightButtons;

    void mergeWith(final TopBarOptions other) {
        title.mergeWith(other.title);
        subtitle.mergeWith(other.subtitle);
        background.mergeWith(other.background);
        if (other.testId.hasValue()) testId = other.testId;
        if (other.visible.hasValue()) visible = other.visible;
        if (other.animate.hasValue()) animate = other.animate;
        if (other.hideOnScroll.hasValue()) hideOnScroll = other.hideOnScroll;
        if (other.drawBehind.hasValue()) drawBehind = other.drawBehind;
        if (other.leftButtons != null) leftButtons = other.leftButtons;
        if (other.rightButtons != null) rightButtons = other.rightButtons;
        validate();
    }

    void mergeWithDefault(TopBarOptions defaultOptions) {
        title.mergeWithDefault(defaultOptions.title);
        subtitle.mergeWithDefault(defaultOptions.subtitle);
        background.mergeWithDefault(defaultOptions.background);
        if (!visible.hasValue()) visible = defaultOptions.visible;
        if (!animate.hasValue()) animate = defaultOptions.animate;
        if (!hideOnScroll.hasValue()) hideOnScroll = defaultOptions.hideOnScroll;
        if (!drawBehind.hasValue()) drawBehind = defaultOptions.drawBehind;
        if (leftButtons == null) leftButtons = defaultOptions.leftButtons;
        if (rightButtons == null) rightButtons = defaultOptions.rightButtons;
        if (!testId.hasValue()) testId = defaultOptions.testId;
        validate();
    }

    private void validate() {
        if (title.component.hasValue() && (title.text.hasValue() || subtitle.text.hasValue())) {
            if (BuildConfig.DEBUG) Log.w("RNN", "A screen can't use both text and component - clearing text.");
            title.text = new NullText();
            subtitle.text = new NullText();
        }
    }
}
