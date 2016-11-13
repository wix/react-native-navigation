package com.reactnativenavigation.controllers;

import android.content.Intent;

import static android.content.Intent.ACTION_VIEW;

public class IntentDataHandler {
    private static Intent intent;

    static void saveIntentData(Intent intent) {
        IntentDataHandler.intent = intent;
    }

    static boolean hasIntentData() {
        return intent != null;
    }

    static void setIntentData(Intent intent) {
        if (IntentDataHandler.intent != null) {
            intent.setData(IntentDataHandler.intent.getData());
            intent.putExtras(IntentDataHandler.intent);
            intent.setAction(ACTION_VIEW);
            clear();
        }
    }

    private static void clear() {
        intent = null;
    }
}
