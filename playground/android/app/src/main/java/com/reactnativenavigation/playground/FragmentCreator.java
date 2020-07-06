package com.reactnativenavigation.playground;

import androidx.fragment.app.FragmentActivity;

import com.facebook.react.ReactInstanceManager;
import com.reactnativenavigation.controllers.externalcomponent.ExternalComponent;
import com.reactnativenavigation.controllers.externalcomponent.ExternalComponentCreator;

import org.json.JSONObject;

public class FragmentCreator implements ExternalComponentCreator {
    @Override
    public ExternalComponent create(FragmentActivity activity, ReactInstanceManager reactInstanceManager, JSONObject props) {
        return new FragmentComponent(activity, props);
    }
}
