import { NativeModules, NativeEventEmitter } from 'react-native';
import { EventSubscription } from '../interfaces/EventSubscription';

export const enum LifecycleEventType {
  ComponentDidMount = 'ComponentDidMount',
  ComponentDidAppear = 'ComponentDidAppear',
  ComponentDidDisappear = 'ComponentDidDisappear',
  ComponentWillUnmount = 'ComponentWillUnmount'
}

export interface LifecycleEvent {
  type: LifecycleEventType;
  componentId: string;
  componentName: string;
}

export interface CommandCompletedEvent {
  commandId: string;
  completionTime: number;
  params: any;
}

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
