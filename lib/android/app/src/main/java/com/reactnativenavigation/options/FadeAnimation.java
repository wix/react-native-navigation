package com.reactnativenavigation.options;

import org.json.JSONException;
import org.json.JSONObject;

public class FadeAnimation extends NestedAnimationsOptions {
    public FadeAnimation(boolean reversed) {
        try {
            JSONObject alpha = new JSONObject();
            alpha.put("from", reversed ? 1 : 0);
            alpha.put("to", reversed ? 0 : 1);
            alpha.put("duration", 300);

            JSONObject content = new JSONObject();
            content.put("alpha", alpha);

            JSONObject animation = new JSONObject();
            animation.put("content", content);
            mergeWith(parse(animation));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public FadeAnimation() {
        this(false);
    }
}
