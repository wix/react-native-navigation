package com.reactnativenavigation.viewcontrollers.modal;

import android.content.Context;
import android.content.res.Configuration;
import android.view.ViewGroup;

import com.reactnativenavigation.options.ModalPresentationStyle;
import com.reactnativenavigation.options.Options;
import com.reactnativenavigation.react.CommandListener;
import com.reactnativenavigation.react.CommandListenerAdapter;
import com.reactnativenavigation.react.events.EventEmitter;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController;
import com.reactnativenavigation.viewcontrollers.viewcontroller.overlay.ModalOverlay;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import static com.reactnativenavigation.react.Constants.HARDWARE_BACK_BUTTON_ID;
import static com.reactnativenavigation.utils.ObjectUtils.perform;

public class ModalStack {
    private final List<ViewController<?>> modals = new ArrayList<>();
    private final ModalPresenter presenter;
    private final ModalOverlay overlay;
    private EventEmitter eventEmitter;

    public void setEventEmitter(EventEmitter eventEmitter) {
        this.eventEmitter = eventEmitter;
    }

    public ModalStack(Context context) {
        this.presenter = new ModalPresenter(new ModalAnimator(context));
        overlay = new ModalOverlay(context);
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    ModalStack(Context context, ModalPresenter presenter) {
        this.presenter = presenter;
        overlay = new ModalOverlay(context);
    }

    public void setModalsLayout(CoordinatorLayout modalsLayout) {
        presenter.setModalsLayout(modalsLayout);
        overlay.setModalsLayout(modalsLayout);
    }

    public void setRootLayout(ViewGroup rootLayout) {
        presenter.setRootLayout(rootLayout);
    }

    public void setDefaultOptions(Options defaultOptions) {
        presenter.setDefaultOptions(defaultOptions);
    }

    public void showModal(ViewController<?> viewController, ViewController<?> root, CommandListener listener) {
        ViewController<?> toRemove = isEmpty() ? root : peek();
        modals.add(viewController);
        viewController.setOverlay(overlay);
        presenter.showModal(viewController, toRemove, listener);
    }

    public boolean dismissModal(String componentId, @Nullable ViewController<?> root, CommandListener listener) {
        ViewController<?> toDismiss = findModalByComponentId(componentId);
        if (toDismiss != null) {
            boolean isDismissingTopModal = isTop(toDismiss);
            modals.remove(toDismiss);
            @Nullable ViewController<?> toAdd = isEmpty() ? root : isDismissingTopModal ? get(size() - 1) : null;
            if (isDismissingTopModal) {
                if (toAdd == null) {
                    listener.onError("Could not dismiss modal");
                    return false;
                }
            }
            presenter.dismissModal(toDismiss, toAdd, root, new CommandListenerAdapter(listener) {
                @Override
                public void onSuccess(String childId) {
                    eventEmitter.emitModalDismissed(toDismiss.getId(), toDismiss.getCurrentComponentName(), 1);
                    super.onSuccess(toDismiss.getId());
                }
            });
            return true;
        } else {
            listener.onError("Nothing to dismiss");
            return false;
        }
    }

    public void dismissAllModals(@Nullable ViewController<?> root, Options mergeOptions, CommandListener listener) {
        if (modals.isEmpty()) {
            listener.onSuccess(perform(root, "", ViewController::getId));
            return;
        }
        String topModalId = peek().getId();
        String topModalName = peek().getCurrentComponentName();
        int modalsDismissed = size();

        peek().mergeOptions(mergeOptions);

        while (!modals.isEmpty()) {
            if (modals.size() == 1) {
                dismissModal(modals.get(0).getId(), root, new CommandListenerAdapter(listener) {
                    @Override
                    public void onSuccess(String childId) {
                        eventEmitter.emitModalDismissed(topModalId, topModalName, modalsDismissed);
                        super.onSuccess(childId);
                    }
                });
            } else {
                modals.get(0).destroy();
                modals.remove(0);
            }
        }
    }

    public boolean handleBack(CommandListener listener, ViewController<?> root) {
        if (isEmpty()) return false;
        if (peek().handleBack(listener)) {
            return true;
        }

        if (presenter.shouldDismissModal(peek())) return dismissModal(peek().getId(), root, listener);
        else {
            peek().sendOnNavigationButtonPressed(HARDWARE_BACK_BUTTON_ID);
            return true;
        }
    }

    ViewController<?> peek() {
        if (modals.isEmpty()) throw new EmptyStackException();
        return modals.get(modals.size() - 1);
    }

    public ViewController<?> get(int index) {
        return modals.get(index);
    }

    public boolean isEmpty() {
        return modals.isEmpty();
    }

    public int size() {
        return modals.size();
    }

    private boolean isTop(ViewController<?> modal) {
        return !isEmpty() && peek().equals(modal);
    }

    @Nullable
    private ViewController<?> findModalByComponentId(String componentId) {
        for (ViewController<?> modal : modals) {
            if (modal.findController(componentId) != null) {
                return modal;
            }
        }
        return null;
    }


    @Nullable
    public ViewController<?> findControllerById(String componentId) {
        for (ViewController<?> modal : modals) {
            ViewController<?> controllerById = modal.findController(componentId);
            if (controllerById != null) {
                return controllerById;
            }
        }
        return null;
    }

    public void destroy() {
        for (ViewController<?> modal : modals) {
            modal.destroy();
        }
        modals.clear();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        for (ViewController<?> controller : modals) {
            controller.onConfigurationChanged(newConfig);
        }
    }

    public void onHostPause() {
        if (!isEmpty()) {
            peek().onViewDisappear();
        }
    }

    public void onHostResume() {
        if (!isEmpty()) {
            peek().onViewDidAppear();
        }
    }

    public boolean peekDisplayedOverCurrentContext() {
        return !isEmpty() && presenter.resolveOptions(peek()).modal.presentationStyle == ModalPresentationStyle.OverCurrentContext;
    }
}
