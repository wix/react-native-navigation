package com.reactnativenavigation.options;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ModalSheetDetentParser {

    public static List<ModalSheetDetent> parse(JSONArray json) {
        if (json == null) {
            return new ArrayList<>();
        }
        List<ModalSheetDetent> detents = new ArrayList<>();
        for (int i = 0; i < json.length(); i++) {
            ModalSheetDetent detent = parseDetent(json.opt(i));
            if (detent != null) {
                detents.add(detent);
            }
        }
        return detents;
    }

    private static ModalSheetDetent parseDetent(Object item) {
        if (item instanceof String) {
            return ModalSheetDetent.system(((String) item).toLowerCase());
        }
        if (!(item instanceof JSONObject)) {
            return null;
        }
        JSONObject dict = (JSONObject) item;
        String id = dict.optString("id", null);
        if (id == null || id.isEmpty()) {
            return null;
        }
        double height = dict.optDouble("height", 0);
        if (height <= 0) {
            return null;
        }
        return ModalSheetDetent.custom(id, (float) height);
    }
}
