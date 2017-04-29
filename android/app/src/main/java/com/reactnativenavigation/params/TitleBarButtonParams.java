package com.reactnativenavigation.params;

public class TitleBarButtonParams extends BaseTitleBarButtonParams {
    public StyleParams.Font font;
    public String hint;

    @Override
    public void setStyleFromScreen(StyleParams styleParams) {
        super.setStyleFromScreen(styleParams);
        font = styleParams.titleBarTitleFont;
    }

    public boolean hasFont() {
        return font != null && font.hasFont();
    }
}
