import { EmitterSubscription } from 'react-native';

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
  PreviewCompletedEvent,
  ModalDismissedEvent
} from '../interfaces/ComponentEvents';
import { CommandCompletedEvent, BottomTabSelectedEvent } from '../interfaces/Events';

export class EventsRegistry {
  constructor(private nativeEventsReceiver: NativeEventsReceiver, private commandsObserver: CommandsObserver, private componentEventsObserver: ComponentEventsObserver) { }

  public registerAppLaunchedListener(callback: () => void): EmitterSubscription {
    return this.nativeEventsReceiver.registerAppLaunchedListener(callback);
  }

  public registerComponentDidAppearListener(callback: (event: ComponentDidAppearEvent) => void): EmitterSubscription {
    return this.nativeEventsReceiver.registerComponentDidAppearListener(callback);
  }

  public registerComponentDidDisappearListener(callback: (event: ComponentDidDisappearEvent) => void): EmitterSubscription {
    return this.nativeEventsReceiver.registerComponentDidDisappearListener(callback);
  }

  public registerCommandCompletedListener(callback: (event: CommandCompletedEvent) => void): EmitterSubscription {
    return this.nativeEventsReceiver.registerCommandCompletedListener(callback);
  }

  public registerBottomTabSelectedListener(callback: (event: BottomTabSelectedEvent) => void): EmitterSubscription {
    return this.nativeEventsReceiver.registerBottomTabSelectedListener(callback);
  }

  public registerNavigationButtonPressedListener(callback: (event: NavigationButtonPressedEvent) => void): EmitterSubscription {
    return this.nativeEventsReceiver.registerNavigationButtonPressedListener(callback);
  }

  public registerModalDismissedListener(callback: (event: ModalDismissedEvent) => void): EmitterSubscription {
    return this.nativeEventsReceiver.registerModalDismissedListener(callback);
  }

  public registerSearchBarUpdatedListener(callback: (event: SearchBarUpdatedEvent) => void): EmitterSubscription {
    return this.nativeEventsReceiver.registerSearchBarUpdatedListener(callback);
  }

  public registerSearchBarCancelPressedListener(callback: (event: SearchBarCancelPressedEvent) => void): EmitterSubscription {
    return this.nativeEventsReceiver.registerSearchBarCancelPressedListener(callback);
  }

  public registerPreviewCompletedListener(callback: (event: PreviewCompletedEvent) => void): EmitterSubscription {
    return this.nativeEventsReceiver.registerPreviewCompletedListener(callback);
  }

  public registerCommandListener(callback: (name: string, params: any) => void): EventSubscription {
    return this.commandsObserver.register(callback);
  }

  public bindComponent(component: React.Component<any>, componentId?: string): EventSubscription {
    return this.componentEventsObserver.bindComponent(component, componentId);
  }
}
