package com.reactnativenavigation.params.parsers;

import android.graphics.Color;
import android.os.Bundle;

import com.reactnativenavigation.params.CollapsingTopBarParams;
import com.reactnativenavigation.params.StyleParams;
import com.reactnativenavigation.views.collapsingToolbar.behaviours.CollapseBehaviour;
import com.reactnativenavigation.views.collapsingToolbar.behaviours.CollapseTitleBarBehaviour;
import com.reactnativenavigation.views.collapsingToolbar.behaviours.CollapseTopBarBehaviour;
import com.reactnativenavigation.views.collapsingToolbar.behaviours.TitleBarHideOnScrollBehaviour;

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
        if (hasBackgroundImage()) {
            return new CollapseTopBarBehaviour();
        }
        if (shouldHideTitleBarOnScroll() && params.getBoolean("drawBelowTopBar", false)) {
            return new CollapseTitleBarBehaviour();
        }
        return new TitleBarHideOnScrollBehaviour();
    }

    private boolean hasBackgroundImage() {
        return params.containsKey("collapsingToolBarImage");
    }

    private boolean shouldHideTitleBarOnScroll() {
        return params.getBoolean("titleBarHideOnScroll", false);
    }
}
