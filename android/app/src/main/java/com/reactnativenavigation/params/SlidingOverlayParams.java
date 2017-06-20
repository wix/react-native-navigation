package com.reactnativenavigation.params;

public class SlidingOverlayParams {
    public enum Position {
        Top, Bottom;

        public static Position fromString(String string) {
            switch(string) {
                case "bottom":
                    return Position.Bottom;
                case "top":
                default:
                    return Position.Top;
            }
        }
    }

    public String screenInstanceId;
    public NavigationParams navigationParams;
    public Integer autoDismissTimerSec;
    public Position position;
}
