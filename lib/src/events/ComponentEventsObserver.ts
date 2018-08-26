import * as _ from 'lodash';
import { EventSubscription } from '../interfaces/EventSubscription';
import {
  ComponentDidAppearEvent,
  ComponentDidDisappearEvent,
  NavigationButtonPressedEvent,
  SearchBarUpdatedEvent,
  SearchBarCancelPressedEvent,
  ComponentEvent,
  ModalDismissedEvent
} from '../interfaces/ComponentEvents';
import { NativeEventsReceiver } from '../adapters/NativeEventsReceiver';

export class ComponentEventsObserver {
  private readonly listeners = {};
  private alreadyRegistered = false;

  constructor(private readonly nativeEventsReceiver: NativeEventsReceiver) {
    this.notifyComponentDidAppear = this.notifyComponentDidAppear.bind(this);
    this.notifyComponentDidDisappear = this.notifyComponentDidDisappear.bind(this);
    this.notifyNavigationButtonPressed = this.notifyNavigationButtonPressed.bind(this);
    this.notifyModalDismissed = this.notifyModalDismissed.bind(this);
    this.notifySearchBarUpdated = this.notifySearchBarUpdated.bind(this);
    this.notifySearchBarCancelPressed = this.notifySearchBarCancelPressed.bind(this);
  }

  public registerOnceForAllComponentEvents() {
    if (this.alreadyRegistered) { return; }
    this.alreadyRegistered = true;
    this.nativeEventsReceiver.registerComponentDidAppearListener(this.notifyComponentDidAppear);
    this.nativeEventsReceiver.registerComponentDidDisappearListener(this.notifyComponentDidDisappear);
    this.nativeEventsReceiver.registerNavigationButtonPressedListener(this.notifyNavigationButtonPressed);
    this.nativeEventsReceiver.registerModalDismissedListener(this.notifyModalDismissed);
    this.nativeEventsReceiver.registerSearchBarUpdatedListener(this.notifySearchBarUpdated);
    this.nativeEventsReceiver.registerSearchBarCancelPressedListener(this.notifySearchBarCancelPressed);
  }

  public bindComponent(component: React.Component<any>): EventSubscription {
    const componentId = component.props.componentId;
    if (!_.isString(componentId)) {
      throw new Error(`bindComponent expects a component with a componentId in props`);
    }
    if (_.isNil(this.listeners[componentId])) {
      this.listeners[componentId] = {};
    }
    const key = _.uniqueId();
    this.listeners[componentId][key] = component;

    return { remove: () => _.unset(this.listeners[componentId], key) };
  }

  public unmounted(componentId: string) {
    _.unset(this.listeners, componentId);
  }

  notifyComponentDidAppear(event: ComponentDidAppearEvent) {
    this.triggerOnAllListenersByComponentId(event, 'componentDidAppear');
  }

  notifyComponentDidDisappear(event: ComponentDidDisappearEvent) {
    this.triggerOnAllListenersByComponentId(event, 'componentDidDisappear');
  }

  notifyNavigationButtonPressed(event: NavigationButtonPressedEvent) {
    this.triggerOnAllListenersByComponentId(event, 'navigationButtonPressed');
  }

  notifyModalDismissed(event: ModalDismissedEvent) {
    this.triggerOnAllListenersByComponentId(event, 'modalDismissed');
  }

  notifySearchBarUpdated(event: SearchBarUpdatedEvent) {
    this.triggerOnAllListenersByComponentId(event, 'searchBarUpdated');
  }

  notifySearchBarCancelPressed(event: SearchBarCancelPressedEvent) {
    this.triggerOnAllListenersByComponentId(event, 'searchBarCancelPressed');
  }

  private triggerOnAllListenersByComponentId(event: ComponentEvent, method: string) {
    _.forEach(this.listeners[event.componentId], (component) => {
      if (_.isObject(component) && _.isFunction(component[method])) {
        component[method](event);
      }
    });
  }
}
