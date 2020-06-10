package com.reactnativenavigation.parse;

import androidx.annotation.CheckResult;

import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.parse.parsers.TextParser;

import org.json.JSONObject;

public class PIPActionButton {
    public Number requestCode;
    public Text requestType;
    public Text actionTitle;
    public Text actionIcon;

    private PIPActionButton() {

    }

    public static PIPActionButton parse(JSONObject json) {
        PIPActionButton actionButton = new PIPActionButton();
        actionButton.actionIcon = TextParser.parse(json, "actionIcon");
        actionButton.actionTitle = TextParser.parse(json, "actionTitle");
        actionButton.requestCode = json.optInt("requestCode");
        actionButton.requestType = TextParser.parse(json, "requestType");
        return actionButton;
    }

    @CheckResult
    public PIPActionButton copy() {
        PIPActionButton actionButton = new PIPActionButton();
        actionButton.mergeWith(this);
        return actionButton;
    }

    @CheckResult
    public PIPActionButton mergeWith(PIPActionButton actionButton) {
        this.requestCode = actionButton.requestCode;
        this.actionTitle = actionButton.actionTitle;
        this.requestType = actionButton.requestType;
        this.actionIcon = actionButton.actionIcon;
        return this;
    }
}
