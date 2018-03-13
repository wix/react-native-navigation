package com.reactnativenavigation.playground;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;

import com.reactnativenavigation.viewcontrollers.externalcomponent.ExternalComponent;

import org.json.JSONObject;

public class FragmentComponent implements ExternalComponent {
    private final FrameLayout content;

    FragmentComponent(FragmentActivity activity, JSONObject props) {
        content = new FrameLayout(activity) {
            @Override
            protected void onAttachedToWindow() {
                super.onAttachedToWindow();
                addFragmentAfterContainerIsAttached(activity, props);
            }
        };
        content.setId(R.id.fragment_screen_content);
    }

    private void addFragmentAfterContainerIsAttached(FragmentActivity activity, JSONObject props) {
        FragmentManager fm = activity.getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.fragment_screen_content, createFragment(props));
        transaction.commitAllowingStateLoss();
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
