import { NativeModules, NativeEventEmitter } from 'react-native';

export interface EventSubscription {
  remove();
}

export class NativeEventsReceiver {
  private emitter: NativeEventEmitter;
  constructor() {
    this.emitter = new NativeEventEmitter(NativeModules.RNNEventEmitter);
  }

  registerOnAppLaunched(callback: () => void): EventSubscription {
    return this.emitter.addListener('RNN.onAppLaunched', callback);
  }

  registerComponentDidAppear(callback: (params) => void): EventSubscription {
    return this.emitter.addListener('RNN.componentDidAppear', callback);
  }

  registerComponentDidDisappear(callback: (params) => void): EventSubscription {
    return this.emitter.addListener('RNN.componentDidDisappear', callback);
  }

  registerOnNavigationButtonPressed(callback: (params) => void): EventSubscription {
    return this.emitter.addListener('RNN.onNavigationButtonPressed', callback);
  }
}
