package com.reactnativenavigation.options;

import androidx.annotation.NonNull;

import com.reactnativenavigation.options.params.Bool;
import com.reactnativenavigation.options.params.NullBool;
import com.reactnativenavigation.options.params.NullText;
import com.reactnativenavigation.options.params.Text;
import com.reactnativenavigation.options.parsers.BoolParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ModalOptions {

    public static ModalOptions parse(final JSONObject json) {
        ModalOptions options = new ModalOptions();
        if (json == null) return options;

        options.presentationStyle = ModalPresentationStyle.fromString(json.optString("modalPresentationStyle"));
        options.blurOnUnmount = BoolParser.parse(json, "blurOnUnmount");

        JSONObject modal = json.optJSONObject("modal");
        if (modal != null) {
            options.swipeToDismiss = BoolParser.parse(modal, "swipeToDismiss");
            options.prefersGrabberVisible = BoolParser.parse(modal, "prefersGrabberVisible");
            options.selectedDetent = parseText(modal, "selectedDetent");
            options.largestUndimmedDetent = parseText(modal, "largestUndimmedDetent");
            options.detents = ModalSheetDetentParser.parse(modal.optJSONArray("detents"));
        }

        return options;
    }

    private static Text parseText(JSONObject json, String key) {
        if (!json.has(key)) {
            return new NullText();
        }
        return new Text(json.optString(key));
    }

    public ModalPresentationStyle presentationStyle = ModalPresentationStyle.Unspecified;
    public @NonNull Bool blurOnUnmount = new NullBool();
    public @NonNull Bool swipeToDismiss = new NullBool();
    public @NonNull Bool prefersGrabberVisible = new NullBool();
    public @NonNull Text selectedDetent = new NullText();
    public @NonNull Text largestUndimmedDetent = new NullText();
    public @NonNull List<ModalSheetDetent> detents = new ArrayList<>();

    public void mergeWith(final ModalOptions other) {
        if (other.presentationStyleHasValue()) presentationStyle = other.presentationStyle;
        if (other.blurOnUnmount.hasValue()) blurOnUnmount = other.blurOnUnmount;
        if (other.swipeToDismiss.hasValue()) swipeToDismiss = other.swipeToDismiss;
        if (other.prefersGrabberVisible.hasValue()) prefersGrabberVisible = other.prefersGrabberVisible;
        if (other.selectedDetent.hasValue()) selectedDetent = other.selectedDetent;
        if (other.largestUndimmedDetent.hasValue()) largestUndimmedDetent = other.largestUndimmedDetent;
        if (!other.detents.isEmpty()) detents = other.detents;
    }

    private boolean presentationStyleHasValue() {
        return presentationStyle != ModalPresentationStyle.Unspecified;
    }

    public void mergeWithDefault(final ModalOptions defaultOptions) {
        if (!presentationStyleHasValue()) presentationStyle = defaultOptions.presentationStyle;
        if (!blurOnUnmount.hasValue()) blurOnUnmount = defaultOptions.blurOnUnmount;
        if (!swipeToDismiss.hasValue()) swipeToDismiss = defaultOptions.swipeToDismiss;
        if (!prefersGrabberVisible.hasValue()) prefersGrabberVisible = defaultOptions.prefersGrabberVisible;
        if (!selectedDetent.hasValue()) selectedDetent = defaultOptions.selectedDetent;
        if (!largestUndimmedDetent.hasValue()) largestUndimmedDetent = defaultOptions.largestUndimmedDetent;
        if (detents.isEmpty()) detents = defaultOptions.detents;
    }

    public boolean hasSheetPresentationOptions() {
        return !detents.isEmpty()
                || selectedDetent.hasValue()
                || prefersGrabberVisible.hasValue();
    }

    public boolean isPageSheetPresentation() {
        return presentationStyle == ModalPresentationStyle.PageSheet;
    }
}
