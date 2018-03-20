package com.reactnativenavigation.parse;

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

    public void mergeWith(OrientationOptions other) {
        if (other.hasValue()) orientations = other.orientations;
    }

    private boolean hasValue() {
        return !orientations.isEmpty();
    }

    public void mergeWithDefault(OrientationOptions defaultOptions) {
        if (!hasValue()) orientations = defaultOptions.orientations;
    }

    @Override
    public String toString() {
        return hasValue() ? Arrays.toString(orientations.toArray(new Orientation[0])) : Orientation.Default.toString();
    }
}
