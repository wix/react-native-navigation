package com.reactnativenavigation.playground;

import android.support.annotation.*;

import com.facebook.react.*;
import com.reactnativenavigation.*;

import java.util.*;

public class MainApplication extends NavigationApplication {

    @Override
    public boolean isDebug() {
        return BuildConfig.DEBUG;
    }

    @Nullable
    @Override
    public List<ReactPackage> createAdditionalReactPackages() {
        return null;
    }
}
