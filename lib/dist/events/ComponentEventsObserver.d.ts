/// <reference types="react" />
import { EventSubscription } from '../interfaces/EventSubscription';
import { ComponentDidAppearEvent, ComponentDidDisappearEvent, NavigationButtonPressedEvent, SearchBarUpdatedEvent, SearchBarCancelPressedEvent, PreviewCompletedEvent, ModalDismissedEvent } from '../interfaces/ComponentEvents';
import { NativeEventsReceiver } from '../adapters/NativeEventsReceiver';
export declare class ComponentEventsObserver {
    private readonly nativeEventsReceiver;
    private listeners;
    private alreadyRegistered;
    constructor(nativeEventsReceiver: NativeEventsReceiver);
    registerOnceForAllComponentEvents(): void;
    bindComponent(component: React.Component<any>, componentId?: string): EventSubscription;
    unmounted(componentId: string): void;
    notifyComponentDidAppear(event: ComponentDidAppearEvent): void;
    notifyComponentDidDisappear(event: ComponentDidDisappearEvent): void;
    notifyNavigationButtonPressed(event: NavigationButtonPressedEvent): void;
    notifyModalDismissed(event: ModalDismissedEvent): void;
    notifySearchBarUpdated(event: SearchBarUpdatedEvent): void;
    notifySearchBarCancelPressed(event: SearchBarCancelPressedEvent): void;
    notifyPreviewCompleted(event: PreviewCompletedEvent): void;
    private triggerOnAllListenersByComponentId;
}
