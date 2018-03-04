package com.reactnativenavigation.parse;

import com.reactnativenavigation.parse.params.Orientation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrientationOptions {
    Orientation[] orientations = new Orientation[0];

    public static OrientationOptions parse(JSONObject json) {
        OrientationOptions options = new OrientationOptions();
        if (json == null) return options;

        JSONArray orientations = json.optJSONArray("orientation");
        if (orientations == null) {
            String orientation = json.optString("orientation", Orientation.Default.name);
            options.orientations = new Orientation[]{Orientation.fromString(orientation)};
        } else {
            List<Orientation> parsed = new ArrayList<>();
            for (int i = 0; i < orientations.length(); i++) {
                Orientation o = Orientation.fromString(orientations.optString(i, "default"));
                if (o != null) {
                    parsed.add(o);
                }
            }
            options.orientations = parsed.toArray(new Orientation[0]);
        }

        return options;
    }

    public int getValue() {
        if (!hasValue()) return Orientation.Default.orientationCode;

        int result = 0;
        for (Orientation orientation : orientations) {
            result |= orientation.orientationCode;
        }
        return result;
    }

    public void mergeWith(OrientationOptions other) {
        if (other.hasValue()) orientations = other.orientations;
    }

    private boolean hasValue() {
        return orientations.length > 0;
    }

    public void mergeWithDefault(OrientationOptions defaultOptions) {
        if (!hasValue()) orientations = defaultOptions.orientations;
    }

    @Override
    public String toString() {
        return hasValue() ? Arrays.toString(orientations) : Orientation.Default.toString();
    }
}
