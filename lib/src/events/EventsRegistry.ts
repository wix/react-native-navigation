import { NativeEventsReceiver } from '../adapters/NativeEventsReceiver';
import { CommandsObserver } from './CommandsObserver';
import { EventSubscription } from '../interfaces/EventSubscription';
import { ComponentEventsObserver } from './ComponentEventsObserver';
import {
  ComponentDidAppearEvent,
  ComponentDidDisappearEvent,
  NavigationButtonPressedEvent,
  SearchBarUpdatedEvent,
  SearchBarCancelPressedEvent,
  ModalDismissedEvent
} from '../interfaces/ComponentEvents';
import { CommandCompletedEvent, BottomTabSelectedEvent } from '../interfaces/Events';

export class EventsRegistry {
  constructor(private nativeEventsReceiver: NativeEventsReceiver, private commandsObserver: CommandsObserver, private componentEventsObserver: ComponentEventsObserver) { }

  public registerAppLaunchedListener(callback: () => void): EventSubscription {
    return this.nativeEventsReceiver.registerAppLaunchedListener(callback);
  }

  public registerComponentDidAppearListener(callback: (event: ComponentDidAppearEvent) => void): EventSubscription {
    return this.nativeEventsReceiver.registerComponentDidAppearListener(callback);
  }

  public registerComponentDidDisappearListener(callback: (event: ComponentDidDisappearEvent) => void): EventSubscription {
    return this.nativeEventsReceiver.registerComponentDidDisappearListener(callback);
  }

  public registerCommandCompletedListener(callback: (event: CommandCompletedEvent) => void): EventSubscription {
    return this.nativeEventsReceiver.registerCommandCompletedListener(callback);
  }

  public registerBottomTabSelectedListener(callback: (event: BottomTabSelectedEvent) => void): EventSubscription {
    return this.nativeEventsReceiver.registerBottomTabSelectedListener(callback);
  }

  public registerNavigationButtonPressedListener(callback: (event: NavigationButtonPressedEvent) => void): EventSubscription {
    return this.nativeEventsReceiver.registerNavigationButtonPressedListener(callback);
  }

  public registerModalDismissedListener(callback: (event: ModalDismissedEvent) => void): EventSubscription {
    return this.nativeEventsReceiver.registerModalDismissedListener(callback);
  }

  public registerSearchBarUpdatedListener(callback: (event: SearchBarUpdatedEvent) => void): EventSubscription {
    return this.nativeEventsReceiver.registerSearchBarUpdatedListener(callback);
  }

  public registerSearchBarCancelPressedListener(callback: (event: SearchBarCancelPressedEvent) => void): EventSubscription {
    return this.nativeEventsReceiver.registerSearchBarCancelPressedListener(callback);
  }

  public registerCommandListener(callback: (name: string, params: any) => void): EventSubscription {
    return this.commandsObserver.register(callback);
  }

  public bindComponent(component: React.Component<any>): EventSubscription {
    return this.componentEventsObserver.bindComponent(component);
  }
}
