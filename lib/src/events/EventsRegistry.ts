import { NativeEventsReceiver } from '../adapters/NativeEventsReceiver';
import { CommandsObserver } from './CommandsObserver';
import { EventSubscription } from '../interfaces/EventSubscription';
import { ScreenEventsRegistry } from './ScreenEventsRegistry';
import { LifecycleEvent } from '../interfaces/LifecycleEvent';
import { CommandCompletedEvent } from '../interfaces/CommandCompletedEvent';

export class EventsRegistry {
  constructor(private nativeEventsReceiver: NativeEventsReceiver, private commandsObserver: CommandsObserver, private screenEventsRegistry: ScreenEventsRegistry) { }

  public registerAppLaunchedListener(callback: () => void): EventSubscription {
    return this.nativeEventsReceiver.registerAppLaunchedListener(callback);
  }

  public registerComponentLifecycleListener(callback: (event: LifecycleEvent) => void): EventSubscription {
    return this.nativeEventsReceiver.registerComponentLifecycleListener((event) => callback(event));
  }

  public registerCommandListener(callback: (name: string, params: any) => void): EventSubscription {
    return this.commandsObserver.register(callback);
  }

  public registerCommandCompletedListener(callback: (event: CommandCompletedEvent) => void): EventSubscription {
    return this.nativeEventsReceiver.registerCommandCompletedListener((event) => callback(event));
  }

  public registerNativeEventListener(callback: (name: string, params: any) => void): EventSubscription {
    return this.nativeEventsReceiver.registerNativeEventListener(({ name, params }) => callback(name, params));
  }

  public bindScreen(screen: React.Component<any>): EventSubscription {
    return this.screenEventsRegistry.bindScreen(screen);
  }
}
