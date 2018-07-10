import * as _ from 'lodash';
import { EventSubscription } from '../interfaces/EventSubscription';
import {
  ComponentDidAppearEvent,
  ComponentDidDisappearEvent,
  NavigationButtonPressedEvent,
  SearchBarUpdatedEvent,
  SearchBarCancelPressedEvent,
  ComponentEvent
} from '../interfaces/ComponentEvents';
import { NativeEventsReceiver } from '../adapters/NativeEventsReceiver';

export class ComponentEventsObserver {
  private readonly listeners = {};

  constructor(private readonly nativeEventsReceiver: NativeEventsReceiver) {
    this.notifyComponentDidAppear = this.notifyComponentDidAppear.bind(this);
    this.notifyComponentDidDisappear = this.notifyComponentDidDisappear.bind(this);
    this.notifyNavigationButtonPressed = this.notifyNavigationButtonPressed.bind(this);
    this.notifySearchBarUpdated = this.notifySearchBarUpdated.bind(this);
    this.notifySearchBarCancelPressed = this.notifySearchBarCancelPressed.bind(this);
  }

  public registerForAllComponentEvents() {
    this.nativeEventsReceiver.registerComponentDidAppearListener(this.notifyComponentDidAppear);
    this.nativeEventsReceiver.registerComponentDidDisappearListener(this.notifyComponentDidDisappear);
    this.nativeEventsReceiver.registerNavigationButtonPressedListener(this.notifyNavigationButtonPressed);
    this.nativeEventsReceiver.registerSearchBarUpdatedListener(this.notifySearchBarUpdated);
    this.nativeEventsReceiver.registerSearchBarCancelPressedListener(this.notifySearchBarCancelPressed);
  }

  public bindComponent(component: React.Component<any>): EventSubscription {
    const key = component.props.componentId;
    if (!_.isString(key)) {
      throw new Error(`bindComponent expects a component with a componentId in props`);
    }

    _.set(this.listeners, key, component);

    return { remove: () => _.unset(this.listeners, key) };
  }

  notifyComponentDidAppear(event: ComponentDidAppearEvent) {
    this.triggerOnComponent(this.listeners[event.componentId], 'componentDidAppear', event);
  }

  notifyComponentDidDisappear(event: ComponentDidDisappearEvent) {
    this.triggerOnComponent(this.listeners[event.componentId], 'componentDidDisappear', event);
  }

  notifyNavigationButtonPressed(event: NavigationButtonPressedEvent) {
    this.triggerOnComponent(this.listeners[event.componentId], 'navigationButtonPressed', event);
  }

  notifySearchBarUpdated(event: SearchBarUpdatedEvent) {
    this.triggerOnComponent(this.listeners[event.componentId], 'searchBarUpdated', event);
  }

  notifySearchBarCancelPressed(event: SearchBarCancelPressedEvent) {
    this.triggerOnComponent(this.listeners[event.componentId], 'searchBarCancelPressed', event);
  }

  private triggerOnComponent(component: React.Component<any>, method: string, event: ComponentEvent) {
    if (_.isObject(component) && _.isFunction(component[method])) {
      component[method](event);
    }
  }
}
