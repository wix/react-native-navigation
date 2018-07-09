import { NativeModules, NativeEventEmitter } from 'react-native';
import { EventSubscription } from '../interfaces/EventSubscription';
import { LifecycleEvent } from '../interfaces/LifecycleEvent';
import { CommandCompletedEvent } from '../interfaces/CommandCompletedEvent';

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

  public registerComponentLifecycleListener(callback: (event: LifecycleEvent) => void): EventSubscription {
    return this.emitter.addListener('RNN.ComponentLifecycle', callback);
  }

  public registerCommandCompletedListener(callback: (data: CommandCompletedEvent) => void): EventSubscription {
    return this.emitter.addListener('RNN.CommandCompleted', callback);
  }

  public registerNativeEventListener(callback: (data) => void): EventSubscription {
    return this.emitter.addListener('RNN.NativeEvent', callback);
  }
}
