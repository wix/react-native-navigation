/// <reference types="react" />
import { EventSubscription } from '../interfaces/EventSubscription';
import { ComponentDidAppearEvent, ComponentDidDisappearEvent, NavigationButtonPressedEvent, SearchBarUpdatedEvent, SearchBarCancelPressedEvent, PreviewCompletedEvent, ModalDismissedEvent, ScreenPoppedEvent, ModalAttemptedToDismissEvent } from '../interfaces/ComponentEvents';
import { NativeEventsReceiver } from '../adapters/NativeEventsReceiver';
import { Store } from '../components/Store';
export declare class ComponentEventsObserver {
    private readonly nativeEventsReceiver;
    private readonly store;
    private listeners;
    private alreadyRegistered;
    constructor(nativeEventsReceiver: NativeEventsReceiver, store: Store);
    registerOnceForAllComponentEvents(): void;
    bindComponent(component: React.Component<any>, componentId?: string): EventSubscription;
    unmounted(componentId: string): void;
    notifyComponentDidAppear(event: ComponentDidAppearEvent): void;
    notifyComponentDidDisappear(event: ComponentDidDisappearEvent): void;
    notifyNavigationButtonPressed(event: NavigationButtonPressedEvent): void;
    notifyModalDismissed(event: ModalDismissedEvent): void;
    notifyModalAttemptedToDismiss(event: ModalAttemptedToDismissEvent): void;
    notifySearchBarUpdated(event: SearchBarUpdatedEvent): void;
    notifySearchBarCancelPressed(event: SearchBarCancelPressedEvent): void;
    notifyPreviewCompleted(event: PreviewCompletedEvent): void;
    notifyScreenPopped(event: ScreenPoppedEvent): void;
    private triggerOnAllListenersByComponentId;
}
