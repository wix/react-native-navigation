package com.reactnativenavigation.options;

import com.reactnativenavigation.options.params.NullText;
import com.reactnativenavigation.options.params.Text;
import com.reactnativenavigation.options.parsers.TextParser;

import org.json.JSONException;
import org.json.JSONObject;

public class ExternalComponent {

    public Text name = new NullText();
    public JSONObject passProps = new JSONObject();

    public static ExternalComponent parse(JSONObject json) {
        ExternalComponent options = new ExternalComponent();
        if (json == null) {
            return options;
        }

        options.name = TextParser.parse(json, "name");
        if (!options.name.hasValue()) {
            throw new RuntimeException("ExternalComponent must have a name");
        }
        options.passProps = parsePassProps(json);
        return options;
    }

    private static JSONObject parsePassProps(JSONObject json) {
        if (json.has("passProps")) {
            try {
                return json.getJSONObject("passProps");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return new JSONObject();
    }

    public void mergeWith(ExternalComponent other) {
        if (other.name.hasValue()) {
            name = other.name;
        }
        if (other.passProps.length() > 0) {
            passProps = other.passProps;
        }
    }

    public void mergeWithDefault(ExternalComponent defaultOptions) {
        if (!name.hasValue()) {
            name = defaultOptions.name;
        }
        if (passProps.length() == 0) {
            passProps = defaultOptions.passProps;
        }
    }
}
