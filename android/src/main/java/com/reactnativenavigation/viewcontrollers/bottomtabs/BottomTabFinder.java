package com.reactnativenavigation.viewcontrollers.bottomtabs;

import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController;

import java.util.List;

import androidx.annotation.IntRange;

public class BottomTabFinder {
    private final List<ViewController<?>> tabs;

    public BottomTabFinder(List<ViewController<?>> tabs) {
        this.tabs = tabs;
    }

    @IntRange(from = -1)
    public int findByControllerId(String id) {
        for (int i = 0; i < tabs.size(); i++) {
            if (tabs.get(i).findController(id) != null) {
                return i;
            }
        }
        return -1;
    }
}
