import { NativeEventsReceiver } from '../adapters/NativeEventsReceiver';
import { CommandsObserver } from './CommandsObserver';

export interface EventSubscription {
  remove();
}

export class EventsRegistry {
  constructor(private nativeEventsReceiver: NativeEventsReceiver, private commandsObserver: CommandsObserver) { }

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

  public onNavigationCommand(callback: (name: string, params: any) => void): EventSubscription {
    return this.commandsObserver.register(callback);
  }
}
