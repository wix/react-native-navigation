package com.reactnativenavigation.options;

public class ModalSheetDetent {

    public enum Type {
        SYSTEM,
        CUSTOM
    }

    public Type type;
    public String systemId;
    public String customId;
    public float height;

    public static ModalSheetDetent system(String id) {
        ModalSheetDetent detent = new ModalSheetDetent();
        detent.type = Type.SYSTEM;
        detent.systemId = id;
        return detent;
    }

    public static ModalSheetDetent custom(String id, float height) {
        ModalSheetDetent detent = new ModalSheetDetent();
        detent.type = Type.CUSTOM;
        detent.customId = id;
        detent.height = height;
        return detent;
    }
}
