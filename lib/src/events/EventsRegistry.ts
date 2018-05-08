import { NativeEventsReceiver } from '../adapters/NativeEventsReceiver';
import { CommandsObserver } from './CommandsObserver';
import { EventSubscription } from '../interfaces/EventSubscription';

export class EventsRegistry {
  constructor(private nativeEventsReceiver: NativeEventsReceiver, private commandsObserver: CommandsObserver) { }

  public registerAppLaunchedListener(callback: () => void): EventSubscription {
    return this.nativeEventsReceiver.registerAppLaunchedListener(callback);
  }

  public registerComponentDidAppearListener(callback: (componentId: string, componentName: string) => void): EventSubscription {
    return this.nativeEventsReceiver.registerComponentDidAppearListener(({ componentId, componentName }) => callback(componentId, componentName));
  }

  public registerComponentDidDisappearListener(callback: (componentId: string, componentName: string) => void): EventSubscription {
    return this.nativeEventsReceiver.registerComponentDidDisappearListener(({ componentId, componentName }) => callback(componentId, componentName));
  }

  public registerCommandListener(callback: (name: string, params: any) => void): EventSubscription {
    return this.commandsObserver.register(callback);
  }

  public registerCommandCompletedListener(callback: (commandId: string, completionTime: number, params: any) => void): EventSubscription {
    return this.nativeEventsReceiver.registerCommandCompletedListener(({ commandId, completionTime, params }) => callback(commandId, completionTime, params));
  }

  public registerNativeEventListener(callback: (name: string, params: any) => void): EventSubscription {
    return this.nativeEventsReceiver.registerNativeEventListener(({ name, params }) => callback(name, params));
  }
}
