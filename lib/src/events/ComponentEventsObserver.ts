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

export class ComponentEventsObserver {
  private readonly listeners = {};

  public bindComponent(component: React.Component<any>): EventSubscription {
    const key = component.props.componentId;
    if (!_.isString(key)) {
      throw new Error(`bindComponent expects a component with a componentId in props`);
    }

    _.set(this.listeners, key, component);

    return { remove: () => _.unset(this.listeners, key) };
  }

  public notifyComponentDidAppear(event: ComponentDidAppearEvent) {
    this.triggerOnComponent(this.listeners[event.componentId], 'componentDidAppear', event);
  }

  public notifyComponentDidDisappear(event: ComponentDidDisappearEvent) {
    this.triggerOnComponent(this.listeners[event.componentId], 'componentDidDisappear', event);
  }

  public notifyNavigationButtonPressed(event: NavigationButtonPressedEvent) {
    this.triggerOnComponent(this.listeners[event.componentId], 'navigationButtonPressed', event);
  }

  public notifySearchBarUpdated(event: SearchBarUpdatedEvent) {
    this.triggerOnComponent(this.listeners[event.componentId], 'searchBarUpdated', event);
  }

  public notifySearchBarCancelPressed(event: SearchBarCancelPressedEvent) {
    this.triggerOnComponent(this.listeners[event.componentId], 'searchBarCancelPressed', event);
  }

  private triggerOnComponent(component: React.Component<any>, method: string, event: ComponentEvent) {
    if (_.isObject(component) && _.isFunction(component[method])) {
      component[method](event);
    }
  }
}
