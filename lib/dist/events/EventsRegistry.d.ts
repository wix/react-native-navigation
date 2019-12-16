/// <reference types="react" />
import { EmitterSubscription } from 'react-native';
import { NativeEventsReceiver } from '../adapters/NativeEventsReceiver';
import { CommandsObserver } from './CommandsObserver';
import { EventSubscription } from '../interfaces/EventSubscription';
import { ComponentEventsObserver } from './ComponentEventsObserver';
import { ComponentDidAppearEvent, ComponentDidDisappearEvent, NavigationButtonPressedEvent, SearchBarUpdatedEvent, SearchBarCancelPressedEvent, PreviewCompletedEvent, ModalDismissedEvent } from '../interfaces/ComponentEvents';
import { CommandCompletedEvent, BottomTabSelectedEvent } from '../interfaces/Events';
export declare class EventsRegistry {
    private nativeEventsReceiver;
    private commandsObserver;
    private componentEventsObserver;
    constructor(nativeEventsReceiver: NativeEventsReceiver, commandsObserver: CommandsObserver, componentEventsObserver: ComponentEventsObserver);
    registerAppLaunchedListener(callback: () => void): EmitterSubscription;
    registerComponentDidAppearListener(callback: (event: ComponentDidAppearEvent) => void): EmitterSubscription;
    registerComponentDidDisappearListener(callback: (event: ComponentDidDisappearEvent) => void): EmitterSubscription;
    registerCommandCompletedListener(callback: (event: CommandCompletedEvent) => void): EmitterSubscription;
    registerBottomTabSelectedListener(callback: (event: BottomTabSelectedEvent) => void): EmitterSubscription;
    registerNavigationButtonPressedListener(callback: (event: NavigationButtonPressedEvent) => void): EmitterSubscription;
    registerModalDismissedListener(callback: (event: ModalDismissedEvent) => void): EmitterSubscription;
    registerSearchBarUpdatedListener(callback: (event: SearchBarUpdatedEvent) => void): EmitterSubscription;
    registerSearchBarCancelPressedListener(callback: (event: SearchBarCancelPressedEvent) => void): EmitterSubscription;
    registerPreviewCompletedListener(callback: (event: PreviewCompletedEvent) => void): EmitterSubscription;
    registerCommandListener(callback: (name: string, params: any) => void): EventSubscription;
    bindComponent(component: React.Component<any>, componentId?: string): EventSubscription;
}
