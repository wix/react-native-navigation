package com.reactnativenavigation.viewcontrollers.modal;

import android.app.Activity;
import android.view.ViewGroup;

import com.reactnativenavigation.anim.ModalAnimator;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.react.events.EventEmitter;
import com.reactnativenavigation.utils.CommandListener;
import com.reactnativenavigation.utils.CommandListenerAdapter;
import com.reactnativenavigation.viewcontrollers.ViewController;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import static com.reactnativenavigation.utils.ObjectUtils.perform;

public class ModalStack {
    private List<ViewController> modals = new ArrayList<>();
    private final ModalPresenter presenter;
    private EventEmitter eventEmitter;

    public void setEventEmitter(EventEmitter eventEmitter) {
        this.eventEmitter = eventEmitter;
    }

    public ModalStack(Activity activity) {
        this.presenter = new ModalPresenter(new ModalAnimator(activity));
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    ModalStack(ModalPresenter presenter) {
        this.presenter = presenter;
    }

    public void setModalsLayout(CoordinatorLayout modalsLayout) {
        presenter.setModalsLayout(modalsLayout);
    }

    public void setRootLayout(ViewGroup rootLayout) {
        presenter.setRootLayout(rootLayout);
    }

    public void setDefaultOptions(Options defaultOptions) {
        presenter.setDefaultOptions(defaultOptions);
    }

    public void showModal(ViewController viewController, ViewController root, CommandListener listener) {
        ViewController toRemove = isEmpty() ? root : peek();
        modals.add(viewController);
        presenter.showModal(viewController, toRemove, listener);
    }

    public boolean dismissModal(String componentId, @Nullable ViewController root, CommandListener listener) {
        ViewController toDismiss = findModalByComponentId(componentId);
        if (toDismiss != null) {
            boolean isDismissingTopModal = isTop(toDismiss);
            modals.remove(toDismiss);
            @Nullable ViewController toAdd = isEmpty() ? root : isDismissingTopModal ? get(size() - 1) : null;
            if (isDismissingTopModal) {
                if (toAdd == null) {
                    listener.onError("Could not dismiss modal");
                    return false;
                }
            }
            presenter.dismissModal(toDismiss, toAdd, root, new CommandListenerAdapter(listener) {
                @Override
                public void onSuccess(String childId) {
                    eventEmitter.emitModalDismissed(componentId, toDismiss.getCurrentComponentName(), 1);
                    super.onSuccess(componentId);
                }
            });
            return true;
        } else {
            listener.onError("Nothing to dismiss");
            return false;
        }
    }

    public void dismissAllModals(@Nullable ViewController root, Options mergeOptions, CommandListener listener) {
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

    public boolean handleBack(CommandListener listener, ViewController root) {
        if (isEmpty()) return false;
        if (peek().handleBack(listener)) {
            return true;
        }
        return dismissModal(peek().getId(), root, listener);
    }

    ViewController peek() {
        if (modals.isEmpty()) throw new EmptyStackException();
        return modals.get(modals.size() - 1);
    }

    public ViewController get(int index) {
        return modals.get(index);
    }

    public boolean isEmpty() {
        return modals.isEmpty();
    }

    public int size() {
        return modals.size();
    }

    private boolean isTop(ViewController modal) {
        return !isEmpty() && peek().equals(modal);
    }

    @Nullable
    private ViewController findModalByComponentId(String componentId) {
        for (ViewController modal : modals) {
            if (modal.findController(componentId) != null) {
                return modal;
            }
        }
        return null;
    }


    @Nullable
    public ViewController findControllerById(String componentId) {
        for (ViewController modal : modals) {
            ViewController controllerById = modal.findController(componentId);
            if (controllerById != null) {
                return controllerById;
            }
        }
        return null;
    }

    public void destroy() {
        for (ViewController modal : modals) {
            modal.destroy();
        }
        modals.clear();
    }
}
