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
      this.emitter = new NativeEventEmitter(RNNEventEmitter);
    } catch (e) {
      this.emitter = ({
        addListener: () => {
          return {
            remove: () => undefined,
          };
        },
      } as any) as NativeEventEmitter;
    }
  }

  public registerAppLaunchedListener(callback: () => void): EmitterSubscription {
    return this.emitter.addListener('RNN.AppLaunched', callback);
  }

  public registerComponentWillAppearListener(
    callback: (event: ComponentWillAppearEvent) => void
  ): EmitterSubscription {
    return this.emitter.addListener('RNN.ComponentWillAppear', callback);
  }

  public registerComponentDidAppearListener(
    callback: (event: ComponentDidAppearEvent) => void
  ): EmitterSubscription {
    return this.emitter.addListener('RNN.ComponentDidAppear', callback);
  }

  public registerComponentDidDisappearListener(
    callback: (event: ComponentDidDisappearEvent) => void
  ): EmitterSubscription {
    return this.emitter.addListener('RNN.ComponentDidDisappear', callback);
  }

  public registerNavigationButtonPressedListener(
    callback: (event: NavigationButtonPressedEvent) => void
  ): EmitterSubscription {
    return this.emitter.addListener('RNN.NavigationButtonPressed', callback);
  }

  public registerBottomTabPressedListener(
    callback: (data: BottomTabPressedEvent) => void
  ): EmitterSubscription {
    return this.emitter.addListener('RNN.BottomTabPressed', callback);
  }

  public registerModalDismissedListener(
    callback: (event: ModalDismissedEvent) => void
  ): EmitterSubscription {
    return this.emitter.addListener('RNN.ModalDismissed', callback);
  }

  public registerModalAttemptedToDismissListener(
    callback: (event: ModalAttemptedToDismissEvent) => void
  ): EmitterSubscription {
    return this.emitter.addListener('RNN.ModalAttemptedToDismiss', callback);
  }

  public registerSearchBarUpdatedListener(
    callback: (event: SearchBarUpdatedEvent) => void
  ): EmitterSubscription {
    return this.emitter.addListener('RNN.SearchBarUpdated', callback);
  }

  public registerSearchBarCancelPressedListener(
    callback: (event: SearchBarCancelPressedEvent) => void
  ): EmitterSubscription {
    return this.emitter.addListener('RNN.SearchBarCancelPressed', callback);
  }

  public registerPreviewCompletedListener(
    callback: (event: PreviewCompletedEvent) => void
  ): EmitterSubscription {
    return this.emitter.addListener('RNN.PreviewCompleted', callback);
  }

  public registerCommandCompletedListener(
    callback: (data: CommandCompletedEvent) => void
  ): EmitterSubscription {
    return this.emitter.addListener('RNN.CommandCompleted', callback);
  }

  public registerBottomTabSelectedListener(
    callback: (data: BottomTabSelectedEvent) => void
  ): EmitterSubscription {
    return this.emitter.addListener('RNN.BottomTabSelected', callback);
  }

  public registerBottomTabLongPressedListener(
    callback: (data: BottomTabLongPressedEvent) => void
  ): EmitterSubscription {
    return this.emitter.addListener('RNN.BottomTabLongPressed', callback);
  }

  public registerScreenPoppedListener(
    callback: (event: ScreenPoppedEvent) => void
  ): EmitterSubscription {
    return this.emitter.addListener('RNN.ScreenPopped', callback);
  }
}
