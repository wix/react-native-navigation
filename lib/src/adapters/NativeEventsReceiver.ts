import { NativeModules, NativeEventEmitter } from 'react-native';
import { EventSubscription } from '../interfaces/EventSubscription';
import {
  ComponentDidAppearEvent,
  ComponentDidDisappearEvent,
  NavigationButtonPressedEvent,
  SearchBarUpdatedEvent,
  SearchBarCancelPressedEvent,
  ModalDismissedEvent
} from '../interfaces/ComponentEvents';
import { CommandCompletedEvent, BottomTabSelectedEvent } from '../interfaces/Events';

export class NativeEventsReceiver {
  private emitter;
  constructor() {
    try {
      this.emitter = new NativeEventEmitter(NativeModules.RNNEventEmitter);
    } catch (e) {
      this.emitter = {
        addListener: () => {
          return {
            remove: () => undefined
          };
        }
      };
    }
  }

  public registerAppLaunchedListener(callback: () => void): EventSubscription {
    return this.emitter.addListener('RNN.AppLaunched', callback);
  }

  public registerComponentDidAppearListener(callback: (event: ComponentDidAppearEvent) => void): EventSubscription {
    return this.emitter.addListener('RNN.ComponentDidAppear', callback);
  }

  public registerComponentDidDisappearListener(callback: (event: ComponentDidDisappearEvent) => void): EventSubscription {
    return this.emitter.addListener('RNN.ComponentDidDisappear', callback);
  }

  public registerNavigationButtonPressedListener(callback: (event: NavigationButtonPressedEvent) => void): EventSubscription {
    return this.emitter.addListener('RNN.NavigationButtonPressed', callback);
  }

  public registerModalDismissedListener(callback: (event: ModalDismissedEvent) => void): EventSubscription {
    return this.emitter.addListener('RNN.ModalDismissed', callback);
  }

  public registerSearchBarUpdatedListener(callback: (event: SearchBarUpdatedEvent) => void): EventSubscription {
    return this.emitter.addListener('RNN.SearchBarUpdated', callback);
  }

  public registerSearchBarCancelPressedListener(callback: (event: SearchBarCancelPressedEvent) => void): EventSubscription {
    return this.emitter.addListener('RNN.SearchBarCancelPressed', callback);
  }

  public registerCommandCompletedListener(callback: (data: CommandCompletedEvent) => void): EventSubscription {
    return this.emitter.addListener('RNN.CommandCompleted', callback);
  }

  public registerBottomTabSelectedListener(callback: (data: BottomTabSelectedEvent) => void): EventSubscription {
    return this.emitter.addListener('RNN.BottomTabSelected', callback);
  }
}
