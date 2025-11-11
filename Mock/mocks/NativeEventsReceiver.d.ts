import { EmitterSubscription } from 'react-native';
import { ComponentWillAppearEvent, ComponentDidAppearEvent, ComponentDidDisappearEvent, NavigationButtonPressedEvent, SearchBarUpdatedEvent, SearchBarCancelPressedEvent, PreviewCompletedEvent, ModalDismissedEvent, ScreenPoppedEvent, ModalAttemptedToDismissEvent } from '../../src/interfaces/ComponentEvents';
import { CommandCompletedEvent, BottomTabSelectedEvent, BottomTabLongPressedEvent, BottomTabPressedEvent } from '../../src/interfaces/Events';
export declare class NativeEventsReceiver {
    registerAppLaunchedListener(callback: () => void): EmitterSubscription;
    registerComponentWillAppearListener(callback: (event: ComponentWillAppearEvent) => void): EmitterSubscription;
    registerComponentDidAppearListener(callback: (event: ComponentDidAppearEvent) => void): EmitterSubscription;
    registerComponentDidDisappearListener(callback: (event: ComponentDidDisappearEvent) => void): EmitterSubscription;
    registerNavigationButtonPressedListener(callback: (event: NavigationButtonPressedEvent) => void): EmitterSubscription;
    registerBottomTabPressedListener(callback: (data: BottomTabPressedEvent) => void): EmitterSubscription;
    registerModalDismissedListener(callback: (event: ModalDismissedEvent) => void): EmitterSubscription;
    registerModalAttemptedToDismissListener(_callback: (event: ModalAttemptedToDismissEvent) => void): EmitterSubscription;
    registerSearchBarUpdatedListener(_callback: (event: SearchBarUpdatedEvent) => void): EmitterSubscription;
    registerSearchBarCancelPressedListener(_callback: (event: SearchBarCancelPressedEvent) => void): EmitterSubscription;
    registerPreviewCompletedListener(_callback: (event: PreviewCompletedEvent) => void): EmitterSubscription;
    registerCommandCompletedListener(callback: (data: CommandCompletedEvent) => void): EmitterSubscription;
    registerBottomTabSelectedListener(_callback: (data: BottomTabSelectedEvent) => void): EmitterSubscription;
    registerBottomTabLongPressedListener(_callback: (data: BottomTabLongPressedEvent) => void): EmitterSubscription;
    registerScreenPoppedListener(callback: (event: ScreenPoppedEvent) => void): EmitterSubscription;
}
