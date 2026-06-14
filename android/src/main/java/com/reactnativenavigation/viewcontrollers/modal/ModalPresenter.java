package com.reactnativenavigation.viewcontrollers.modal;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.reactnativenavigation.R;
import com.reactnativenavigation.options.AnimationOptions;
import com.reactnativenavigation.options.ModalPresentationStyle;
import com.reactnativenavigation.options.Options;
import com.reactnativenavigation.react.CommandListener;
import com.reactnativenavigation.utils.ScreenAnimationListener;
import com.reactnativenavigation.viewcontrollers.parent.ParentController;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController;

import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.reactnativenavigation.utils.CoordinatorLayoutUtils.matchParentLP;
import static com.reactnativenavigation.utils.CoordinatorLayoutUtils.matchParentWithBehaviour;

public class ModalPresenter {

    private ViewGroup rootLayout;
    private CoordinatorLayout modalsLayout;
    private final ModalAnimator modalAnimator;
    private Options defaultOptions = new Options();
    private final Map<String, FrameLayout> sheetContainersByModalId = new HashMap<>();

    ModalPresenter(ModalAnimator animator) {
        this.modalAnimator = animator;
    }

    void setRootLayout(ViewGroup rootLayout) {
        this.rootLayout = rootLayout;
    }

    void setModalsLayout(CoordinatorLayout modalsLayout) {
        this.modalsLayout = modalsLayout;
    }

    public void setDefaultOptions(Options defaultOptions) {
        this.defaultOptions = defaultOptions;
    }

    void showModal(ViewController<?> appearing, ViewController<?> disappearing, CommandListener listener) {
        if (modalsLayout == null) {
            listener.onError("Can not show modal before activity is created");
            return;
        }

        Options options = resolveSheetOptions(appearing);

        if (shouldUseBottomSheet(options)) {
            showBottomSheetModal(appearing, disappearing, options, listener);
            return;
        }

        AnimationOptions enterAnimationOptions = options.animations.showModal.getEnter();
        appearing.setWaitForRender(enterAnimationOptions.waitForRender);
        modalsLayout.setVisibility(View.VISIBLE);
        modalsLayout.addView(appearing.getView(), matchParentLP());

        if (enterAnimationOptions.enabled.isTrueOrUndefined()) {
            if (enterAnimationOptions.shouldWaitForRender().isTrue()) {
                appearing.addOnAppearedListener(() -> modalAnimator.show(appearing, disappearing, options.animations.showModal, createListener(appearing, disappearing, listener)));
            } else {
                modalAnimator.show(appearing, disappearing, options.animations.showModal, createListener(appearing, disappearing, listener));
            }
        } else {
            if (enterAnimationOptions.waitForRender.isTrue()) {
                appearing.addOnAppearedListener(() -> onShowModalEnd(appearing, disappearing, listener));
            } else {
                onShowModalEnd(appearing, disappearing, listener);
            }
        }
    }

    private void showBottomSheetModal(ViewController<?> appearing, ViewController<?> disappearing, Options options, CommandListener listener) {
        modalsLayout.setVisibility(View.VISIBLE);

        FrameLayout container = ModalBottomSheetPresenter.createContainer(modalsLayout.getContext());
        View content = appearing.getView();
        if (content.getParent() instanceof ViewGroup) {
            ((ViewGroup) content.getParent()).removeView(content);
        }
        container.addView(content, new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));

        BottomSheetBehavior<FrameLayout> behavior = new BottomSheetBehavior<>();
        modalsLayout.addView(container, matchParentWithBehaviour(behavior));
        ModalBottomSheetPresenter.configure(container, options.modal);
        sheetContainersByModalId.put(appearing.getId(), container);
        appearing.getView().setTag(R.id.modal_bottom_sheet_container, container);

        appearing.getView().setAlpha(1);
        onShowModalEnd(appearing, disappearing, listener);
    }

    public void applySheetOptions(ViewController<?> modalRoot) {
        Options resolved = resolveSheetOptions(modalRoot);
        if (!shouldUseBottomSheet(resolved)) {
            return;
        }
        FrameLayout container = sheetContainersByModalId.get(modalRoot.getId());
        if (container == null) {
            return;
        }
        container.post(() -> ModalBottomSheetPresenter.configure(container, resolved.modal));
    }

    private boolean shouldUseBottomSheet(Options options) {
        return options.modal.isPageSheetPresentation() || options.modal.hasSheetPresentationOptions();
    }

    private Options resolveSheetOptions(ViewController<?> modalRoot) {
        Options options = modalRoot.resolveCurrentOptions(defaultOptions);
        if (!(modalRoot instanceof ParentController)) {
            return options;
        }
        ParentController<?> parent = (ParentController<?>) modalRoot;
        ViewController<?> child = parent.getCurrentChild();
        if (child == null) {
            Collection<? extends ViewController<?>> children = parent.getChildControllers();
            if (!children.isEmpty()) {
                child = children.iterator().next();
            }
        }
        if (child == null) {
            return options;
        }
        Options childOptions = child.resolveCurrentOptions(defaultOptions);
        if (!childOptions.modal.isPageSheetPresentation() && !childOptions.modal.hasSheetPresentationOptions()) {
            return options;
        }
        Options merged = options.copy();
        if (childOptions.modal.presentationStyle != ModalPresentationStyle.Unspecified) {
            merged.modal.presentationStyle = childOptions.modal.presentationStyle;
        }
        merged.modal.mergeWith(childOptions.modal);
        return merged;
    }

    @NotNull
    private ScreenAnimationListener createListener(ViewController<?> toAdd, ViewController<?> toRemove, CommandListener listener) {
        return new ScreenAnimationListener() {
            @Override
            public void onStart() {
                toAdd.getView().setAlpha(1);
            }

            @Override
            public void onEnd() {
                onShowModalEnd(toAdd, toRemove, listener);
            }

            @Override
            public void onCancel() {
                listener.onSuccess(toAdd.getId());
            }
        };
    }

    private void onShowModalEnd(ViewController<?> toAdd, @Nullable ViewController<?> toRemove, CommandListener listener) {
        toAdd.onViewDidAppear();
        if (toRemove != null && shouldDetachUnderlyingOnShow(toAdd)) {
            toRemove.detachView();
        }
        listener.onSuccess(toAdd.getId());
    }

    private boolean shouldDetachUnderlyingOnShow(ViewController<?> appearing) {
        Options options = resolveSheetOptions(appearing);
        if (shouldUseBottomSheet(options)) {
            return false;
        }
        return options.modal.presentationStyle != ModalPresentationStyle.OverCurrentContext;
    }

    void dismissModal(ViewController<?> toDismiss, @Nullable ViewController<?> toAdd, ViewController<?> root, CommandListener listener) {
        if (modalsLayout == null) {
            listener.onError("Can not dismiss modal before activity is created");
            return;
        }
        if (toAdd != null) {
            toAdd.attachView(toAdd == root ? rootLayout : modalsLayout, 0);
            toAdd.onViewDidAppear();
        }
        Options options = toDismiss.resolveCurrentOptions(defaultOptions);
        if (options.animations.dismissModal.getExit().enabled.isTrueOrUndefined()) {
            modalAnimator.dismiss(toAdd, toDismiss, options.animations.dismissModal, new ScreenAnimationListener() {
                @Override
                public void onEnd() {
                    onDismissEnd(toDismiss, listener);
                }
            });
        } else {
            onDismissEnd(toDismiss, listener);
        }
    }

    boolean shouldDismissModal(ViewController<?> toDismiss) {
        return toDismiss.resolveCurrentOptions(defaultOptions).hardwareBack.dismissModalOnPress.get(true);
    }

    public Options resolveOptions(ViewController<?> modalController){
        return modalController.resolveCurrentOptions(defaultOptions);
    }

    private void onDismissEnd(ViewController<?> toDismiss, CommandListener listener) {
        listener.onSuccess(toDismiss.getId());
        FrameLayout sheetContainer = sheetContainersByModalId.remove(toDismiss.getId());
        if (sheetContainer != null) {
            modalsLayout.removeView(sheetContainer);
        }
        toDismiss.destroy();
        if (isEmpty()) modalsLayout.setVisibility(View.GONE);
    }

    private boolean isEmpty() {
        return modalsLayout.getChildCount() == 0;
    }
}
