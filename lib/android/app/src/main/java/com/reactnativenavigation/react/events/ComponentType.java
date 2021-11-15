package com.reactnativenavigation.react.events;

public enum ComponentType {
    Component("Component"),
    Button("TopBarButton"),
    Title("TopBarTitle"),
    Background("TopBarBackground");

    private String name;

    public String getName() {
        return name;
    }

    ComponentType(String name) {
        this.name = name;
    }
    }
