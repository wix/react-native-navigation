package com.reactnativenavigation.viewcontrollers.bottomtabs;

import android.support.annotation.IntRange;

import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.views.Component;

import java.util.List;

public class BottomTabFinder {
    private List<ViewController> tabs;

    @IntRange(from = -1)
    int findByComponent(Component component) {
        for (int i = 0; i < tabs.size(); i++) {
            if (tabs.get(i).containsComponent(component)) {
                return i;
            }
        }
        return -1;
    }

    @IntRange(from = -1)
    public int findByControllerId(String id) {
        for (int i = 0; i < tabs.size(); i++) {
            if (tabs.get(i).findControllerById(id) != null) {
                return i;
            }
        }
        return -1;
    }

    void setTabs(List<ViewController> tabs) {
        this.tabs = tabs;
    }
}
