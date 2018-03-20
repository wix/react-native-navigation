package com.reactnativenavigation.viewcontrollers.externalcomponent;

import android.support.v4.app.FragmentActivity;

import org.json.JSONObject;

public interface ExternalComponentCreator {
    ExternalComponent create(FragmentActivity activity, JSONObject props);
}
