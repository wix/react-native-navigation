import type { Component } from 'react';
import { EventSubscription } from '../interfaces/EventSubscription';
import { NavigationComponentListener } from '../interfaces/NavigationComponentListener';
import { ComponentWillAppearEvent, ComponentDidAppearEvent, ComponentDidDisappearEvent, NavigationButtonPressedEvent, SearchBarUpdatedEvent, SearchBarCancelPressedEvent, PreviewCompletedEvent, ScreenPoppedEvent } from '../interfaces/ComponentEvents';
import { NativeEventsReceiver } from '../adapters/NativeEventsReceiver';
import { Store } from '../components/Store';
export declare class ComponentEventsObserver {
    private readonly nativeEventsReceiver;
    private readonly store;
    private listeners;
    private alreadyRegistered;
    constructor(nativeEventsReceiver: NativeEventsReceiver, store: Store);
    registerOnceForAllComponentEvents(): void;
    bindComponent(component: Component<{
        componentId?: string;
    }>, componentId?: string): EventSubscription;
    registerComponentListener(listener: NavigationComponentListener, componentId: string): EventSubscription;
    unmounted(componentId: string): void;
    notifyComponentWillAppear(event: ComponentWillAppearEvent): void;
    notifyComponentDidAppear(event: ComponentDidAppearEvent): void;
    notifyComponentDidDisappear(event: ComponentDidDisappearEvent): void;
    notifyNavigationButtonPressed(event: NavigationButtonPressedEvent): void;
    notifySearchBarUpdated(event: SearchBarUpdatedEvent): void;
    notifySearchBarCancelPressed(event: SearchBarCancelPressedEvent): void;
    notifyPreviewCompleted(event: PreviewCompletedEvent): void;
    notifyScreenPopped(event: ScreenPoppedEvent): void;
    private triggerOnAllListenersByComponentId;
}
