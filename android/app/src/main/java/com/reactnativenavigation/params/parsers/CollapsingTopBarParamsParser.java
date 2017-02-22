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
    private final boolean hasReactView;
    private final boolean hasBackgroundImage;

    CollapsingTopBarParamsParser(Bundle params, boolean titleBarHideOnScroll, boolean drawBelowTopBar) {
        this.params = params;
        this.titleBarHideOnScroll = titleBarHideOnScroll;
        this.drawBelowTopBar = drawBelowTopBar;
        hasReactView = params.containsKey("collapsingToolBarComponent");
        hasBackgroundImage = params.containsKey("collapsingToolBarImage");
    }

    public CollapsingTopBarParams parse() {
        if (!validateParams()) {
            return null;
        }
        CollapsingTopBarParams result = new CollapsingTopBarParams();
        result.imageUri = params.getString("collapsingToolBarImage", null);
        result.reactViewId = params.getString("collapsingToolBarComponent", null);
        result.expendOnTopTabChange = params.getBoolean("expendCollapsingToolBarOnTopTabChange");
        result.scrimColor = getColor(params, "collapsingToolBarCollapsedColor", new StyleParams.Color(Color.WHITE));
        result.showTitleWhenCollapsed = hasReactView;
        result.collapseBehaviour = getCollapseBehaviour();
        return result;
    }

    private boolean validateParams() {
        return titleBarHideOnScroll || hasImageOrReactView();
    }

    private CollapseBehaviour getCollapseBehaviour() {
        if (hasImageOrReactView()) {
            return new CollapseTopBarBehaviour();
        }
        if (titleBarHideOnScroll && drawBelowTopBar) {
            return new CollapseTitleBarBehaviour();
        }
        return new TitleBarHideOnScrollBehaviour();
    }

    private boolean hasImageOrReactView() {
        return hasBackgroundImage || hasReactView;
    }
}
