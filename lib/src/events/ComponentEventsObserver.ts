import * as _ from 'lodash';
import { EventSubscription } from '../interfaces/EventSubscription';
import { ComponentDidAppearEvent, ComponentDidDisappearEvent } from '../interfaces/Events';

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
    this.triggerOnComponent(this.findListener(event.componentId), 'componentDidAppear');
  }

  public notifyComponentDidDisappear(event: ComponentDidDisappearEvent) {
    this.triggerOnComponent(this.findListener(event.componentId), 'componentDidDisappear');
  }

  private findListener(componentId: string) {
    return this.listeners[componentId];
  }

  private triggerOnComponent(component: React.Component<any>, method: string) {
    if (_.isObject(component) && _.isFunction(component[method])) {
      component[method]();
    }
  }
}
