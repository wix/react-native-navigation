package com.reactnativenavigation.parse;

import android.support.annotation.CheckResult;

import com.reactnativenavigation.parse.params.Orientation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrientationOptions {
    List<Orientation> orientations = new ArrayList<>();

    public static OrientationOptions parse(JSONObject json) {
        OrientationOptions options = new OrientationOptions();
        if (json == null) return options;

        JSONArray orientations = json.optJSONArray("orientation");
        if (orientations == null) {
            String orientation = json.optString("orientation", Orientation.Default.name);
            options.orientations.add(Orientation.fromString(orientation));
        } else {
            List<Orientation> parsed = new ArrayList<>();
            for (int i = 0; i < orientations.length(); i++) {
                Orientation o = Orientation.fromString(orientations.optString(i, "default"));
                if (o != null) {
                    parsed.add(o);
                }
            }
            options.orientations = parsed;
        }

        return options;
    }

    public int getValue() {
        if (!hasValue()) return Orientation.Default.orientationCode;

        switch (orientations.get(0)) {
            case Landscape:
                return orientations.contains(Orientation.Portrait) ? Orientation.PortraitLandscape.orientationCode : Orientation.Landscape.orientationCode;
            case Portrait:
                return orientations.contains(Orientation.Landscape) ? Orientation.PortraitLandscape.orientationCode : Orientation.Portrait.orientationCode;
            default:
            case Default:
                return Orientation.Default.orientationCode;
        }
    }

    public boolean hasValue() {
        return !orientations.isEmpty();
    }

    @CheckResult
    public OrientationOptions copy() {
        OrientationOptions result = new OrientationOptions();
        result.orientations = new ArrayList<>(orientations);
        return result;
    }

    public OrientationOptions mergeWithDefault(OrientationOptions defaultOptions) {
        if (!hasValue()) orientations = defaultOptions.orientations;
        return this;
    }

    @Override
    public String toString() {
        return hasValue() ? Arrays.toString(orientations.toArray(new Orientation[0])) : Orientation.Default.toString();
    }
}
