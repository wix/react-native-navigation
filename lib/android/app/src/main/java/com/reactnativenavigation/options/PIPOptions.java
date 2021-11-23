package com.reactnativenavigation.options;

import org.json.JSONArray;
import org.json.JSONObject;

public class PIPOptions {

    public boolean enabledOnBackPress = false;

    public boolean enabledOnHomePress = false;

    public String actionControlGroup;

    public AspectRatio aspectRatio = new AspectRatio();

    public CustomPIPDimension customPIP = new CustomPIPDimension();

    public PIPActionButton[] actionButtons;

    public PIPOptions() {

    }

    public static PIPOptions parse(JSONObject json) {
        PIPOptions pipOptions = null;
        if (json != null) {
            pipOptions = new PIPOptions();
            pipOptions.actionControlGroup = json.optString("actionControlGroup");
            pipOptions.enabledOnBackPress = json.optBoolean("enabledOnBackPress");
            pipOptions.enabledOnHomePress = json.optBoolean("enabledOnHomePress");
            pipOptions.aspectRatio = AspectRatio.parse(json.optJSONObject("aspectRatio"));
            pipOptions.customPIP = CustomPIPDimension.parse(json.optJSONObject("customPIP"));
            JSONArray array = json.optJSONArray("actionButtons");
            if (array != null) {
                pipOptions.actionButtons = new PIPActionButton[array.length()];
                for (int index = 0; index < array.length(); index++) {
                    JSONObject object = array.optJSONObject(index);
                    pipOptions.actionButtons[index] = PIPActionButton.parse(object);
                }
            }
        }
        return pipOptions;
    }

    public PIPOptions copy() {
        PIPOptions pipOptions = new PIPOptions();
        return pipOptions.mergeWith(this);
    }

    public PIPOptions mergeWith(final PIPOptions other) {
        if (other != null) {
            this.enabledOnBackPress = other.enabledOnBackPress;
            this.enabledOnHomePress = other.enabledOnHomePress;
            this.actionControlGroup = other.actionControlGroup;
            this.aspectRatio.mergeWith(other.aspectRatio);
            this.actionButtons = other.actionButtons;
            this.customPIP.mergeWith(other.customPIP);
        }
        return this;
    }
}