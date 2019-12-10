package com.reactnativenavigation.parse;

import androidx.annotation.NonNull;

import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.parse.params.NullBool;
import com.reactnativenavigation.parse.parsers.BoolParser;

import org.json.JSONObject;

public class ModalOptions {

    public static ModalOptions parse(final JSONObject json) {
        ModalOptions options = new ModalOptions();
        if (json == null) return options;

        options.presentationStyle = ModalPresentationStyle.fromString(json.optString("modalPresentationStyle"));
        options.blurOnUnmount = BoolParser.parse(json, "blurOnUnmount");

        return options;
    }

    public ModalPresentationStyle presentationStyle = ModalPresentationStyle.Unspecified;
    public @NonNull Bool blurOnUnmount = new NullBool();

    public void mergeWith(final ModalOptions other) {
        if (other.presentationStyleHasValue()) presentationStyle = other.presentationStyle;
        if (other.blurOnUnmount.hasValue()) blurOnUnmount = other.blurOnUnmount;
    }

    private boolean presentationStyleHasValue() {
        return presentationStyle != ModalPresentationStyle.Unspecified;
    }

    public void mergeWithDefault(final ModalOptions defaultOptions) {
        if (!presentationStyleHasValue()) presentationStyle = defaultOptions.presentationStyle;
        if (!blurOnUnmount.hasValue()) blurOnUnmount = defaultOptions.blurOnUnmount;
    }

}
