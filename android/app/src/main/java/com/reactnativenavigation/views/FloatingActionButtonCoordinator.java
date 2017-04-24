package com.reactnativenavigation.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.reactnativenavigation.NavigationApplication;
import com.reactnativenavigation.R;
import com.reactnativenavigation.params.FabActionParams;
import com.reactnativenavigation.params.FabParams;
import com.reactnativenavigation.utils.ViewUtils;

import java.util.ArrayList;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

class FloatingActionButtonCoordinator {
    private static final String TAG = "FloatingActionButtonCoo";
    private static final int INITIAL_EXPENDED_FAB_ROTATION = -90;
    private CoordinatorLayout parent;
    private FabParams params;
    private FloatingActionButtonWrapper collapsedFab;
    private FloatingActionButtonWrapper expendedFab;
    private final int crossFadeAnimationDuration;
    private final int actionSize;
    private final int fabSize;
    private final int margin = (int) ViewUtils.convertDpToPixel(16);
    private final int labelRightSpacing = (int) ViewUtils.convertDpToPixel(20);
    private FloatingActionButtonAnimator fabAnimator;
    private final ArrayList<FloatingActionButtonWrapper> actions;
    private final ArrayList<FloatingActionButtonLabel> labels;

    FloatingActionButtonCoordinator(CoordinatorLayout parent) {
        this.parent = parent;
        actions = new ArrayList<>();
        labels = new ArrayList<>();
        crossFadeAnimationDuration = parent.getResources().getInteger(android.R.integer.config_shortAnimTime);
        actionSize = (int) ViewUtils.convertDpToPixel(40);
        fabSize = (int) ViewUtils.convertDpToPixel(56);
    }

    public void add(final FabParams params) {
        Log.i(TAG, "add() called with: params = [" + params + "]");
        if (hasFab()) {
            remove(new Runnable() {
                @Override
                public void run() {
                    add(params);
                }
            });
            return;
        }

        this.params = params;
        if (!params.isValid()) {
            return;
        }
        createCollapsedFab();
        createExpendedFab();
        setStyle();
        fabAnimator = new FloatingActionButtonAnimator(collapsedFab, expendedFab, crossFadeAnimationDuration);
        fabAnimator.show();
    }

    void remove(@Nullable final Runnable onComplete) {
        if (!hasFab()) {
            if (onComplete != null) {
                onComplete.run();
            }
            return;
        }
        if (fabAnimator != null) {
            fabAnimator.removeFabFromScreen(expendedFab, new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    removeAllViews();
                    if (onComplete != null) {
                        onComplete.run();
                    }
                }
            });
            fabAnimator.removeFabFromScreen(collapsedFab, null);
            fabAnimator.removeActionsFromScreen(actions);
        }

    }

    private boolean hasFab() {
        return collapsedFab != null || expendedFab != null;
    }

    private void removeAllViews() {
        parent.removeView(collapsedFab);
        parent.removeView(expendedFab);
        collapsedFab = null;
        expendedFab = null;
        for (FloatingActionButtonWrapper action : actions) {
            ((CoordinatorLayout.LayoutParams) action.getLayoutParams()).setBehavior(null);
            parent.removeView(action);
        }
        for (FloatingActionButtonLabel label: labels) {
            parent.removeView(label);
        }
        actions.clear();
        labels.clear();
    }

    private void createCollapsedFab() {
        collapsedFab = createFab(params.collapsedIcon);
        parent.addView(collapsedFab, createFabLayoutParams());
        collapsedFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (params.hasExpendedState()) {
                    fabAnimator.hideCollapsed();
                    fabAnimator.showExpended();
                    showActions();
                }
                NavigationApplication.instance.getEventEmitter().sendNavigatorEvent(params.collapsedId, params.navigatorEventId);
            }
        });
    }

    private void createExpendedFab() {
        expendedFab = createFab(params.expendedIcon);
        parent.addView(expendedFab, createFabLayoutParams());
        expendedFab.setVisibility(View.GONE);
        expendedFab.setRotation(INITIAL_EXPENDED_FAB_ROTATION);
        expendedFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabAnimator.collapse();
                NavigationApplication.instance.getEventEmitter().sendNavigatorEvent(params.expendedId, params.navigatorEventId);
            }
        });
    }

    private FloatingActionButtonWrapper createFab(Drawable icon) {
        FloatingActionButtonWrapper fab = new FloatingActionButtonWrapper(parent.getContext());
        fab.setId(ViewUtils.generateViewId());
        fab.setImageDrawable(icon);
        return fab;
    }

    private CoordinatorLayout.LayoutParams createFabLayoutParams() {
        final CoordinatorLayout.LayoutParams lp = new CoordinatorLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        lp.gravity = Gravity.RIGHT | Gravity.BOTTOM;
        lp.bottomMargin = margin;
        lp.rightMargin = margin;
        lp.topMargin = margin;
        return lp;
    }

    private void setStyle() {
        collapsedFab.setBackgroundTintList(ColorStateList.valueOf(params.backgroundColor.getColor()));
        expendedFab.setBackgroundTintList(ColorStateList.valueOf(params.backgroundColor.getColor()));
    }

    private void showActions() {
        if (actions.size() > 0) {
            return;
        }

        for (int i = 0; i < params.actions.size(); i++) {
            FloatingActionButtonWrapper action = createAction(i);
            actions.add(action);

            FloatingActionButtonLabel label = action.getLabelView();
            labels.add(label);

            if(label != null) {
                parent.addView(label, createLabelLayoutParams(i));
            }

            parent.addView(action);
        }
    }

    private FloatingActionButtonWrapper createAction(int index) {
        final FabActionParams actionParams = params.actions.get(index);
        FloatingActionButtonWrapper action = createFab(actionParams.icon);

        final View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavigationApplication.instance.getEventEmitter().sendNavigatorEvent(actionParams.id, actionParams.navigatorEventId);
                fabAnimator.collapse();
            }
        };

        if(actionParams.title != null) {
            FloatingActionButtonLabel buttonLabel =  new FloatingActionButtonLabel(parent.getContext());
            buttonLabel.setOnClickListener(onClickListener);
            buttonLabel.setBackgroundColor(actionParams.titleBackgroundColor.getColor());
            buttonLabel.setTextColor(actionParams.titleColor.getColor());

            action.setTag(R.id.fab_label, buttonLabel);
            action.setTitle(actionParams.title);
        }

        action.setLayoutParams(createActionLayoutParams(index));
        action.setOnClickListener(onClickListener);

        if (actionParams.backgroundColor.hasColor()) {
            action.setBackgroundTintList(ColorStateList.valueOf(actionParams.backgroundColor.getColor()));
        }

        action.setSize(FloatingActionButtonWrapper.SIZE_MINI);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            action.setCompatElevation(0);
        }
        return action;
    }

    @NonNull
    private CoordinatorLayout.LayoutParams createActionLayoutParams(int actionIndex) {
        CoordinatorLayout.LayoutParams lp = new CoordinatorLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        lp.setAnchorId(expendedFab.getId());
        lp.anchorGravity = Gravity.CENTER_HORIZONTAL;
        lp.setBehavior(new ActionBehaviour(expendedFab, (actionIndex + 1) * (actionSize + margin)));

        return lp;
    }

    private  CoordinatorLayout.LayoutParams createLabelLayoutParams(int actionIndex) {
        CoordinatorLayout.LayoutParams lp = new CoordinatorLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        lp.gravity = Gravity.RIGHT | Gravity.BOTTOM;
        lp.rightMargin = margin + fabSize + labelRightSpacing;

        return lp;
    }

    private static class ActionBehaviour extends CoordinatorLayout.Behavior<FloatingActionButtonWrapper> {
        private final int MAX_VALUE = 90;
        private int dependencyId;
        private float yStep;

        ActionBehaviour(View anchor, float yStep) {
            this.yStep = yStep;
            this.dependencyId = anchor.getId();
        }

        @Override
        public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButtonWrapper child, View dependency) {
            return dependency.getId() == dependencyId;
        }

        @Override
        public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButtonWrapper child, View dependency) {
            final View dependentView = parent.findViewById(dependencyId);
            if (dependentView == null) {
                return false;
            }
            final float dependentValue = dependency.getRotation();
            float fraction = calculateTransitionFraction(dependentValue);
            child.setY(calculateY(dependentView, fraction));
            child.setAlpha(calculateAlpha(fraction));
            setVisibility(child);
            return true;
        }

        private void setVisibility(FloatingActionButtonWrapper child) {
            child.setVisibility(child.getAlpha() == 0 ? View.GONE : View.VISIBLE);
        }

        private float calculateAlpha(float fraction) {
            return 1 * fraction;
        }

        private float calculateY(View dependentView, float fraction) {
            return dependentView.getY() - yStep * fraction;
        }

        @FloatRange(from=0.0, to=1.0)
        private float calculateTransitionFraction(float dependentValue) {
            return 1 - Math.abs(dependentValue / MAX_VALUE);
        }
    }
}
