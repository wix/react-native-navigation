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
    private boolean titleBarHideOnScroll;
    private boolean drawBelowTopBar;

    CollapsingTopBarParamsParser(Bundle params, boolean titleBarHideOnScroll, boolean drawBelowTopBar) {
        this.params = params;
        this.titleBarHideOnScroll = titleBarHideOnScroll;
        this.drawBelowTopBar = drawBelowTopBar;
    }

    public CollapsingTopBarParams parse() {
        if (!hasBackgroundImage() && !titleBarHideOnScroll) {
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
        if (titleBarHideOnScroll && drawBelowTopBar) {
            return new CollapseTitleBarBehaviour();
        }
        return new TitleBarHideOnScrollBehaviour();
    }

    private boolean hasBackgroundImage() {
        return params.containsKey("collapsingToolBarImage");
    }
}
