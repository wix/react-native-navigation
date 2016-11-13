package com.reactnativenavigation.params.parsers;

import android.graphics.Color;
import android.os.Bundle;

import com.reactnativenavigation.params.CollapsingTopBarParams;
import com.reactnativenavigation.params.CollapsingTopBarParams.CollapseBehaviour;
import com.reactnativenavigation.params.StyleParams;

class CollapsingTopBarParamsParser extends Parser {
    private Bundle params;

    CollapsingTopBarParamsParser(Bundle params) {
        this.params = params;
    }

    public CollapsingTopBarParams parse() {
        if (!hasBackgroundImage() && !shouldHideTitleBarOnScroll()) {
            return null;
        }

        CollapsingTopBarParams result = new CollapsingTopBarParams();
        if (hasBackgroundImage()) {
            result.imageUri = params.getString("collapsingToolBarImage");
        }
        result.scrimColor = getColor(params, "collapsingToolBarCollapsedColor", new StyleParams.Color(Color.WHITE));
        result.collapseBehaviour = getCollapseBehaviour();
        return result;
    }

    private CollapseBehaviour getCollapseBehaviour() {
        return shouldHideTitleBarOnScroll() ? CollapseBehaviour.TitleBarHideOnScroll : CollapseBehaviour.CollapseTopBar;
    }

    private boolean hasBackgroundImage() {
        return params.containsKey("collapsingToolBarImage");
    }

    private boolean shouldHideTitleBarOnScroll() {
        return params.getBoolean("titleBarHideOnScroll", false);
    }
}
