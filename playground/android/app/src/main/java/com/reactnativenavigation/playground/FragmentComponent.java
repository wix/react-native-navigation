package com.reactnativenavigation.playground;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.reactnativenavigation.viewcontrollers.externalcomponent.ExternalComponent;

import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

public class FragmentComponent implements ExternalComponent {
    private final FrameLayout content;

    FragmentComponent(FragmentActivity activity, JSONObject props) {
        content = new FrameLayout(activity);
        content.setId(R.id.fragment_screen_content);
        activity.getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_screen_content, createFragment(props))
                .commitAllowingStateLoss();
    }

    @NonNull
    private FragmentScreen createFragment(JSONObject props) {
        FragmentScreen fragment = new FragmentScreen();
        Bundle args = new Bundle();
        args.putString("text", props.optString("text", ""));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View asView() {
        return content;
    }
}
