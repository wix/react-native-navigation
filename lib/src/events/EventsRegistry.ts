import { NativeEventsReceiver, LifecycleEvent } from '../adapters/NativeEventsReceiver';
import { CommandsObserver } from './CommandsObserver';
import { EventSubscription } from '../interfaces/EventSubscription';

export class EventsRegistry {
  constructor(private nativeEventsReceiver: NativeEventsReceiver, private commandsObserver: CommandsObserver) { }

  public registerAppLaunchedListener(callback: () => void): EventSubscription {
    return this.nativeEventsReceiver.registerAppLaunchedListener(callback);
  }

  public registerComponentLifecycleListener(callback: (event: LifecycleEvent) => void): EventSubscription {
    return this.nativeEventsReceiver.registerComponentLifecycleListener((event) => callback(event));
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
