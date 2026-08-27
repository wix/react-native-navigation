import { NativeEventEmitter, EmitterSubscription } from 'react-native';
import {
  ComponentWillAppearEvent,
  ComponentDidAppearEvent,
  ComponentDidDisappearEvent,
  NavigationButtonPressedEvent,
  SearchBarUpdatedEvent,
  SearchBarCancelPressedEvent,
  PreviewCompletedEvent,
  ModalDismissedEvent,
  ScreenPoppedEvent,
  ModalAttemptedToDismissEvent,
} from '../interfaces/ComponentEvents';
import {
  CommandCompletedEvent,
  BottomTabSelectedEvent,
  BottomTabLongPressedEvent,
  BottomTabPressedEvent,
} from '../interfaces/Events';
import RNNEventEmitter from './NativeRNNTurboEventEmitter';

export class NativeEventsReceiver {
  private emitter: NativeEventEmitter;
  constructor() {
    try {
      this.emitter = new NativeEventEmitter(RNNEventEmitter ?? undefined);
    } catch {
      this.emitter = ({
        addListener: () => {
          return {
            remove: () => undefined,
          };
        },
      } as any) as NativeEventEmitter;
    }
  }

  private addListener<T>(eventName: string, callback: (event: T) => void): EmitterSubscription {
    // NativeEventEmitter exposes untyped native payloads. Keep the assertion at
    // this boundary while the public registration methods retain their event types.
    return this.emitter.addListener(eventName, callback as (event: unknown) => void);
  }

  public registerAppLaunchedListener(callback: () => void): EmitterSubscription {
    return this.emitter.addListener('RNN.AppLaunched', callback);
  }

  public registerComponentWillAppearListener(
    callback: (event: ComponentWillAppearEvent) => void
  ): EmitterSubscription {
    return this.addListener('RNN.ComponentWillAppear', callback);
  }

  public registerComponentDidAppearListener(
    callback: (event: ComponentDidAppearEvent) => void
  ): EmitterSubscription {
    return this.addListener('RNN.ComponentDidAppear', callback);
  }

  public registerComponentDidDisappearListener(
    callback: (event: ComponentDidDisappearEvent) => void
  ): EmitterSubscription {
    return this.addListener('RNN.ComponentDidDisappear', callback);
  }

  public registerNavigationButtonPressedListener(
    callback: (event: NavigationButtonPressedEvent) => void
  ): EmitterSubscription {
    return this.addListener('RNN.NavigationButtonPressed', callback);
  }

  public registerBottomTabPressedListener(
    callback: (data: BottomTabPressedEvent) => void
  ): EmitterSubscription {
    return this.addListener('RNN.BottomTabPressed', callback);
  }

  public registerModalDismissedListener(
    callback: (event: ModalDismissedEvent) => void
  ): EmitterSubscription {
    return this.addListener('RNN.ModalDismissed', callback);
  }

  public registerModalAttemptedToDismissListener(
    callback: (event: ModalAttemptedToDismissEvent) => void
  ): EmitterSubscription {
    return this.addListener('RNN.ModalAttemptedToDismiss', callback);
  }

  public registerSearchBarUpdatedListener(
    callback: (event: SearchBarUpdatedEvent) => void
  ): EmitterSubscription {
    return this.addListener('RNN.SearchBarUpdated', callback);
  }

  public registerSearchBarCancelPressedListener(
    callback: (event: SearchBarCancelPressedEvent) => void
  ): EmitterSubscription {
    return this.addListener('RNN.SearchBarCancelPressed', callback);
  }

  public registerPreviewCompletedListener(
    callback: (event: PreviewCompletedEvent) => void
  ): EmitterSubscription {
    return this.addListener('RNN.PreviewCompleted', callback);
  }

  public registerCommandCompletedListener(
    callback: (data: CommandCompletedEvent) => void
  ): EmitterSubscription {
    return this.addListener('RNN.CommandCompleted', callback);
  }

  public registerBottomTabSelectedListener(
    callback: (data: BottomTabSelectedEvent) => void
  ): EmitterSubscription {
    return this.addListener('RNN.BottomTabSelected', callback);
  }

  public registerBottomTabLongPressedListener(
    callback: (data: BottomTabLongPressedEvent) => void
  ): EmitterSubscription {
    return this.addListener('RNN.BottomTabLongPressed', callback);
  }

  public registerScreenPoppedListener(
    callback: (event: ScreenPoppedEvent) => void
  ): EmitterSubscription {
    return this.addListener('RNN.ScreenPopped', callback);
  }
}
