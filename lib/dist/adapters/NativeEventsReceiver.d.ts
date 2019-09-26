import { EmitterSubscription } from 'react-native';
import { ComponentDidAppearEvent, ComponentDidDisappearEvent, NavigationButtonPressedEvent, SearchBarUpdatedEvent, SearchBarCancelPressedEvent, PreviewCompletedEvent, ModalDismissedEvent } from '../interfaces/ComponentEvents';
import { CommandCompletedEvent, BottomTabSelectedEvent } from '../interfaces/Events';
export declare class NativeEventsReceiver {
    private emitter;
    constructor();
    registerAppLaunchedListener(callback: () => void): EmitterSubscription;
    registerComponentDidAppearListener(callback: (event: ComponentDidAppearEvent) => void): EmitterSubscription;
    registerComponentDidDisappearListener(callback: (event: ComponentDidDisappearEvent) => void): EmitterSubscription;
    registerNavigationButtonPressedListener(callback: (event: NavigationButtonPressedEvent) => void): EmitterSubscription;
    registerModalDismissedListener(callback: (event: ModalDismissedEvent) => void): EmitterSubscription;
    registerSearchBarUpdatedListener(callback: (event: SearchBarUpdatedEvent) => void): EmitterSubscription;
    registerSearchBarCancelPressedListener(callback: (event: SearchBarCancelPressedEvent) => void): EmitterSubscription;
    registerPreviewCompletedListener(callback: (event: PreviewCompletedEvent) => void): EmitterSubscription;
    registerCommandCompletedListener(callback: (data: CommandCompletedEvent) => void): EmitterSubscription;
    registerBottomTabSelectedListener(callback: (data: BottomTabSelectedEvent) => void): EmitterSubscription;
}
