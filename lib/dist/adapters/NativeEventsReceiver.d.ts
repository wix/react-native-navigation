import { EmitterSubscription } from 'react-native';
import { ComponentDidAppearEvent, ComponentDidDisappearEvent, NavigationButtonPressedEvent, SearchBarUpdatedEvent, SearchBarCancelPressedEvent, PreviewCompletedEvent, ModalDismissedEvent, ScreenPoppedEvent, ModalAttemptedToDismissEvent } from '../interfaces/ComponentEvents';
import { CommandCompletedEvent, BottomTabSelectedEvent, BottomTabLongPressedEvent, BottomTabPressedEvent } from '../interfaces/Events';
export declare class NativeEventsReceiver {
    private emitter;
    constructor();
    registerAppLaunchedListener(callback: () => void): EmitterSubscription;
    registerComponentDidAppearListener(callback: (event: ComponentDidAppearEvent) => void): EmitterSubscription;
    registerComponentDidDisappearListener(callback: (event: ComponentDidDisappearEvent) => void): EmitterSubscription;
    registerNavigationButtonPressedListener(callback: (event: NavigationButtonPressedEvent) => void): EmitterSubscription;
    registerBottomTabPressedListener(callback: (data: BottomTabPressedEvent) => void): EmitterSubscription;
    registerModalDismissedListener(callback: (event: ModalDismissedEvent) => void): EmitterSubscription;
    registerModalAttemptedToDismissListener(callback: (event: ModalAttemptedToDismissEvent) => void): EmitterSubscription;
    registerSearchBarUpdatedListener(callback: (event: SearchBarUpdatedEvent) => void): EmitterSubscription;
    registerSearchBarCancelPressedListener(callback: (event: SearchBarCancelPressedEvent) => void): EmitterSubscription;
    registerPreviewCompletedListener(callback: (event: PreviewCompletedEvent) => void): EmitterSubscription;
    registerCommandCompletedListener(callback: (data: CommandCompletedEvent) => void): EmitterSubscription;
    registerBottomTabSelectedListener(callback: (data: BottomTabSelectedEvent) => void): EmitterSubscription;
    registerBottomTabLongPressedListener(callback: (data: BottomTabLongPressedEvent) => void): EmitterSubscription;
    registerScreenPoppedListener(callback: (event: ScreenPoppedEvent) => void): EmitterSubscription;
}
