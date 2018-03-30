package com.reactnativenavigation.viewcontrollers.externalcomponent;

import android.support.v4.app.FragmentActivity;

import com.facebook.react.ReactInstanceManager;

import org.json.JSONObject;

public interface ExternalComponentCreator {
    ExternalComponent create(FragmentActivity activity, ReactInstanceManager reactInstanceManager, JSONObject props);
}
