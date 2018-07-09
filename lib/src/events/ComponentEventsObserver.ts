import * as _ from 'lodash';
import { EventSubscription } from '../interfaces/EventSubscription';
import { ComponentDidAppearEvent } from '../interfaces/Events';

export class ComponentEventsObserver {
  private listeners = {};

  bindComponent(component: React.Component<any>): EventSubscription {
    const key = component.props.componentId;
    if (!_.isString(key)) {
      throw new Error(`bindComponent expects a component with a componentId in props`);
    }

    _.set(this.listeners, key, component);

    return { remove: () => _.unset(this.listeners, key) };
  }

  notifyComponentDidAppear(event: ComponentDidAppearEvent) {
    this.triggerOnComponent(this.findListener(event.componentId), 'componentDidAppear');
  }

  private findListener(componentId: string) {
    return this.listeners[componentId];
  }

  private triggerOnComponent(component: React.Component<any>, method: string) {
    if (_.isObject(screen) && _.isFunction(component[method])) {
      component[method]();
    }
  }
}
