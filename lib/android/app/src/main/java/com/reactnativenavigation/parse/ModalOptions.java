package com.reactnativenavigation.parse;

import org.json.JSONObject;

public class ModalOptions {

    public static ModalOptions parse(JSONObject json) {
        ModalOptions options = new ModalOptions();
        if (json == null) return options;

        options.presentationStyle = ModalPresentationStyle.fromString(json.optString("modalPresentationStyle"));

        return options;
    }

    public ModalPresentationStyle presentationStyle = ModalPresentationStyle.Unspecified;

    public void mergeWith(ModalOptions other) {
        if (other.hasValue()) presentationStyle = other.presentationStyle;
    }

    private boolean hasValue() {
        return presentationStyle != ModalPresentationStyle.Unspecified;
    }

    public void mergeWithDefault(ModalOptions defaultOptions) {
        if (!hasValue()) presentationStyle = defaultOptions.presentationStyle;
    }
}
