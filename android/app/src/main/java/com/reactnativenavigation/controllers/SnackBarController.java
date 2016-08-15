package com.reactnativenavigation.controllers;

import android.app.Activity;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableMap;
import com.reactnativenavigation.core.RctManager;
import com.reactnativenavigation.core.objects.Screen;
import com.reactnativenavigation.utils.ContextProvider;

/**
 * Created by artald on 19/07/2016.
 */
public class SnackBarController {
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_DURATION = "duration";
    private static final String KEY_BACKGROUND_COLOR = "backgroundColor";
    private static final String KEY_ACTION = "action";
    private static final String KEY_ACTION_ID = "id";
    private static final String KEY_ACTION_TEXT = "text";
    private static final String KEY_ACTION_TEXT_COLOR = "textColor";

    private static final String VALUE_DURATION_SHORT = "short";
    private static final String VALUE_DURATION_LONG = "long";
    private static final String VALUE_DURATION_INDEFINITE = "indefinite";

    private static Snackbar mActiveSnackBar;

    public static void showSnackBar(ReadableMap params, final Screen screen) {
        if(params == null || !params.hasKey(KEY_MESSAGE) || mActiveSnackBar != null) {
            return;
        }

        Activity context = ContextProvider.getActivityContext();
        if(context == null) {
            return;
        }

        View rootView = context.findViewById(android.R.id.content);
        if(rootView == null) {
            return;
        }

        int duration = Snackbar.LENGTH_SHORT;
        if (params.hasKey(KEY_DURATION)) {
            final String durationString = params.getString(KEY_DURATION);
            switch (durationString) {
                case VALUE_DURATION_SHORT:
                    duration = Snackbar.LENGTH_SHORT;
                    break;
                case VALUE_DURATION_LONG:
                    duration = Snackbar.LENGTH_LONG;
                    break;
                case VALUE_DURATION_INDEFINITE:
                    duration = Snackbar.LENGTH_INDEFINITE;
                    break;
                default:
                    duration = Snackbar.LENGTH_SHORT;
            }
        }

        mActiveSnackBar =  Snackbar.make(rootView, params.getString(KEY_MESSAGE), duration);
        mActiveSnackBar.setCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                mActiveSnackBar = null;
            }
        });

        if (params.hasKey(KEY_BACKGROUND_COLOR)) {
            mActiveSnackBar.getView().setBackgroundColor(Color.parseColor(params.getString(KEY_BACKGROUND_COLOR)));
        }

        if (params.hasKey(KEY_ACTION)) {
            ReadableMap actionMap = params.getMap(KEY_ACTION);
            if (actionMap.hasKey(KEY_ACTION_ID) && actionMap.hasKey(KEY_ACTION_TEXT)) {

                if (actionMap.hasKey(KEY_ACTION_TEXT_COLOR)) {
                    mActiveSnackBar.setActionTextColor(Color.parseColor(actionMap.getString(KEY_ACTION_TEXT_COLOR)));
                }

                final String actionId = actionMap.getString(KEY_ACTION_ID);
                mActiveSnackBar.setAction(actionMap.getString(KEY_ACTION_TEXT), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RctManager.getInstance().sendEvent(actionId, screen, Arguments.createMap());
                    }
                });
            }
        }

        mActiveSnackBar.show();
    }
}
