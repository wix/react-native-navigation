package com.reactnativenavigation.parse;

import org.json.JSONArray;
import org.json.JSONObject;

public class PIPOptions {
    public String actionControlGroup;

    public AspectRatio aspectRatio = new AspectRatio();

    public PIPActionButton[] actionButtons;

    public PIPOptions() {

    }

    public static PIPOptions parse(JSONObject json) {
        PIPOptions pipOptions = null;
        if (json != null) {
            pipOptions = new PIPOptions();
            pipOptions.actionControlGroup = json.optString("actionControlGroup");
            pipOptions.aspectRatio = AspectRatio.parse(json.optJSONObject("aspectRatio"));
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
            this.actionControlGroup = other.actionControlGroup;
            this.aspectRatio.mergeWith(other.aspectRatio);
            this.actionButtons = other.actionButtons;
        }
        return this;
    }
}