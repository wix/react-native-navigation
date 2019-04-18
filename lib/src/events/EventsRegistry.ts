import * as _ from 'lodash';
import { NativeEventsReceiver } from '../adapters/NativeEventsReceiver';
import { CommandsObserver } from './CommandsObserver';
import { EventSubscription } from '../interfaces/EventSubscription';
import { ComponentEventsObserver } from './ComponentEventsObserver';
import {
  ComponentEvent,
  ComponentDidAppearEvent,
  ComponentDidDisappearEvent,
  NavigationButtonPressedEvent,
  SearchBarUpdatedEvent,
  SearchBarCancelPressedEvent,
  PreviewCompletedEvent,
  ModalDismissedEvent
} from '../interfaces/ComponentEvents';
import { CommandCompletedEvent, BottomTabSelectedEvent } from '../interfaces/Events';

type NavEvent = ComponentEvent | CommandCompletedEvent | BottomTabSelectedEvent;

export class EventsRegistry {
  private listeners: Record<string, Record<string, (event?: NavEvent) => void>> = {};
  private alreadyRegistered = false;

  constructor(private nativeEventsReceiver: NativeEventsReceiver, private commandsObserver: CommandsObserver, private componentEventsObserver: ComponentEventsObserver) { }

  public registerOnceForAllEvents() {
    if (this.alreadyRegistered) { return; }
    this.alreadyRegistered = true;
    this.nativeEventsReceiver.registerAppLaunchedListener(() => this.notifyEvent('appLaunched'));
    this.nativeEventsReceiver.registerComponentDidAppearListener(event => this.notifyEvent('componentDidAppear', event));
    this.nativeEventsReceiver.registerComponentDidDisappearListener(event => this.notifyEvent('componentDidDisappear', event));
    this.nativeEventsReceiver.registerCommandCompletedListener(event => this.notifyEvent('commandCompleted', event));
    this.nativeEventsReceiver.registerBottomTabSelectedListener(event => this.notifyEvent('bottomTabSelected', event));
    this.nativeEventsReceiver.registerNavigationButtonPressedListener(event => this.notifyEvent('navigationButtonPressed', event));
    this.nativeEventsReceiver.registerModalDismissedListener(event => this.notifyEvent('modalDismissed', event));
    this.nativeEventsReceiver.registerSearchBarUpdatedListener(event => this.notifyEvent('searchBarUpdated', event));
    this.nativeEventsReceiver.registerSearchBarCancelPressedListener(event => this.notifyEvent('searchBarCancelPressed', event));
    this.nativeEventsReceiver.registerPreviewCompletedListener(event => this.notifyEvent('previewCompleted', event));
  }

  public registerAppLaunchedListener(callback: () => void): EventSubscription {
    return this.registerEvent('appLaunched', callback);
  }

  public registerComponentDidAppearListener(callback: (event: ComponentDidAppearEvent) => void): EventSubscription {
    return this.registerEvent('componentDidAppear', callback);
  }

  public registerComponentDidDisappearListener(callback: (event: ComponentDidDisappearEvent) => void): EventSubscription {
    return this.registerEvent('componentDidDisappear', callback);
  }

  public registerCommandCompletedListener(callback: (event: CommandCompletedEvent) => void): EventSubscription {
    return this.registerEvent('commandCompleted', callback);
  }

  public registerBottomTabSelectedListener(callback: (event: BottomTabSelectedEvent) => void): EventSubscription {
    return this.registerEvent('bottomTabSelected', callback);
  }

  public registerNavigationButtonPressedListener(callback: (event: NavigationButtonPressedEvent) => void): EventSubscription {
    return this.registerEvent('navigationButtonPressed', callback);
  }

  public registerModalDismissedListener(callback: (event: ModalDismissedEvent) => void): EventSubscription {
    return this.registerEvent('modalDismissed', callback);
  }

  public registerSearchBarUpdatedListener(callback: (event: SearchBarUpdatedEvent) => void): EventSubscription {
    return this.registerEvent('searchBarUpdated', callback);
  }

  public registerSearchBarCancelPressedListener(callback: (event: SearchBarCancelPressedEvent) => void): EventSubscription {
    return this.registerEvent('searchBarCancelPressed', callback);
  }

  public registerPreviewCompletedListener(callback: (event: PreviewCompletedEvent) => void): EventSubscription {
    return this.registerEvent('previewCompleted', callback);
  }

  public registerCommandListener(callback: (name: string, params: any) => void): EventSubscription {
    return this.commandsObserver.register(callback);
  }

  public bindComponent(component: React.Component<any>, componentId?: string): EventSubscription {
    return this.componentEventsObserver.bindComponent(component, componentId);
  }

  public notifyEvent(method: string, event?: NavEvent) {
    if (this.listeners[method]) {
      _.forEach(this.listeners[method], (callback) => {
        callback(event);
      });
    }
  }

  private registerEvent(method: string, callback: (event?: any) => void): EventSubscription {
    if (_.isNil(this.listeners[method])) {
      this.listeners[method] = {};
    }
    const key = _.uniqueId();
    this.listeners[method][key] = callback;

    return { remove: () => _.unset(this.listeners[method], key) };
  }
}
