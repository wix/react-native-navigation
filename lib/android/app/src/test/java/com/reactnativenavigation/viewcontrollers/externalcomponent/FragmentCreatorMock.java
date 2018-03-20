package com.reactnativenavigation.viewcontrollers.externalcomponent;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.widget.FrameLayout;

import com.reactnativenavigation.R;

import org.json.JSONObject;

public class FragmentCreatorMock implements ExternalComponentCreator {
    @Override
    public ExternalComponent create(FragmentActivity activity, JSONObject props) {
        return createContent(activity);
    }

    private ExternalComponent createContent(Activity activity) {
        FrameLayout content = new FrameLayout(activity) {
            @Override
            protected void onAttachedToWindow() {
                super.onAttachedToWindow();
                FragmentManager fm = activity.getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.add(R.id.fragment_screen_content, new SomeFragment());
                ft.commitAllowingStateLoss();
            }
        };
        content.setId(R.id.fragment_screen_content);
        return () -> content;
    }
}
