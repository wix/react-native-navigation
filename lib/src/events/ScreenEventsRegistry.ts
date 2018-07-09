import { LifecycleEvent, LifecycleEventType } from '../adapters/NativeEventsReceiver';
import * as _ from 'lodash';
import { EventSubscription } from '../interfaces/EventSubscription';

export class ScreenEventsRegistry {
  private screens = {};

  onLifecycleEvent(event: LifecycleEvent) {
    const screen = this.screens[event.componentId];
    if (!screen) {
      return;
    }

    switch (event.type) {
      case LifecycleEventType.ComponentDidAppear:
      case LifecycleEventType.ComponentDidDisappear:
        this.triggerOnScreen(screen, event.type);
        break;
    }
  }

  bindScreen(screen: React.Component<any>): EventSubscription {
    const key = screen.props.componentId;
    if (!_.isString(key)) {
      throw new Error(`bindScreen expects a screen with a componentId`);
    }

    this.screens[key] = screen;

    return { remove: () => delete this.screens[key] };
  }

  private triggerOnScreen(screen, eventType) {
    const method = _.camelCase(eventType);
    if (_.isFunction(screen[method])) {
      screen[method]();
    }
  }
}
