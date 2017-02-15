package com.reactnativenavigation.controllers;

import android.content.Intent;
import android.support.annotation.Nullable;

import com.reactnativenavigation.NavigationApplication;

import static android.content.Intent.ACTION_VIEW;

class IntentDataHandler {
    private static Intent intent;

    static void onStartApp(Intent intent) {
        setIntentData(intent);
    }

    static void onResume(Intent intent) {
        if (hasIntentData()) {
            setIntentData(intent);
        } else {
            saveIntentData(intent);
        }
    }

    static void saveIntentData(Intent intent) {
        IntentDataHandler.intent = intent;
    }

    static void onPostResume(Intent intent) {
        if (hasIntentData()) {
            fakeOnNewIntentForLinkingModule(intent);
            clear();
        }
    }

    static void onPause(@Nullable Intent intent) {
        if (intent != null) {
            intent.setData(null);
        }
        clear();
    }

    private static void fakeOnNewIntentForLinkingModule(Intent intent) {
        if (intent != null) {
            NavigationApplication.instance.getReactGateway().onNewIntent(intent);
        }
    }

    private static boolean hasIntentData() {
        return intent != null;
    }

    private static void setIntentData(@Nullable Intent intent) {
        if (intent != null && IntentDataHandler.intent != null) {
            intent.setData(IntentDataHandler.intent.getData());
            intent.putExtras(IntentDataHandler.intent);
            intent.setAction(ACTION_VIEW);
        }
    }

    private static void clear() {
        intent = null;
    }
}
