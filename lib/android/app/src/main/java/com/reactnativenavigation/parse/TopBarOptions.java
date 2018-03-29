package com.reactnativenavigation.parse;


import android.support.annotation.Nullable;

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

public class TopBarOptions implements DEFAULT_VALUES {

    public static TopBarOptions parse(TypefaceLoader typefaceManager, JSONObject json) {
        TopBarOptions options = new TopBarOptions();
        if (json == null) return options;

        options.title = TitleOptions.parse(typefaceManager, json.optJSONObject("title"));
        options.subtitle = SubtitleOptions.parse(typefaceManager, json.optJSONObject("subtitle"));
        options.background = TopBarBackgroundOptions.parse(json.optJSONObject("background"));
        options.visible = BoolParser.parse(json, "visible");
        options.animate = BoolParser.parse(json,"animate");
        options.hideOnScroll = BoolParser.parse(json,"hideOnScroll");
        options.drawBehind = BoolParser.parse(json,"drawBehind");
        options.rightButtons = Button.parseJsonArray(json.optJSONArray("rightButtons"));
        options.leftButtons = Button.parseJsonArray(json.optJSONArray("leftButtons"));
        options.testId = TextParser.parse(json, "testID");

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
    }
}
