package com.reactnativenavigation.playground;

import androidx.fragment.app.FragmentActivity;

import com.reactnativenavigation.viewcontrollers.externalcomponent.ExternalComponent;
import com.reactnativenavigation.viewcontrollers.externalcomponent.ExternalComponentCreator;

import org.json.JSONObject;

public class FragmentCreator implements ExternalComponentCreator {
    @Override
    public ExternalComponent create(FragmentActivity activity, JSONObject props) {
        return new FragmentComponent(activity, props);
    }
}
