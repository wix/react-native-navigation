import { EventSubscription, NativeEventsReceiver } from '../adapters/NativeEventsReceiver';

export class EventsRegistry {
  private nativeEventsReceiver: NativeEventsReceiver;

  constructor(nativeEventsReceiver: NativeEventsReceiver) {
    this.nativeEventsReceiver = nativeEventsReceiver;
  }

  public onAppLaunched(callback: () => void): EventSubscription {
    return this.nativeEventsReceiver.registerOnAppLaunched(callback);
  }

  public componentDidAppear(callback: (componentId: string, componentName: string) => void): EventSubscription {
    return this.nativeEventsReceiver.registerComponentDidAppear(({ componentId, componentName }) => callback(componentId, componentName));
  }

  public componentDidDisappear(callback: (componentId: string, componentName: string) => void): EventSubscription {
    return this.nativeEventsReceiver.registerComponentDidDisappear(({ componentId, componentName }) => callback(componentId, componentName));
  }

  public onNavigationButtonPressed(callback: (componentId: string, buttonId: string) => void): EventSubscription {
    return this.nativeEventsReceiver.registerOnNavigationButtonPressed(({ componentId, buttonId }) => callback(componentId, buttonId));
  }
}
