package com.reactnativenavigation.viewcontrollers.bottomtabs;

import androidx.annotation.IntRange;

import com.reactnativenavigation.viewcontrollers.ViewController;

import java.util.List;

public class BottomTabFinder {
    private List<ViewController> tabs;

    public BottomTabFinder(List<ViewController> tabs) {
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
