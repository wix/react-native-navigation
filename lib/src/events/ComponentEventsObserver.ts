import { EventsRegistry } from './EventsRegistry';
import { Store } from '../components/Store';
import { LifecycleEvent, LifecycleEventType } from '../adapters/NativeEventsReceiver';

const BUTTON_PRESSED_EVENT_NAME = 'buttonPressed';
const ON_SEARCH_BAR_UPDATED = 'searchBarUpdated';
const ON_SEARCH_BAR_CANCEL_PRESSED = 'searchBarCancelPressed';

export class ComponentEventsObserver {
  constructor(private eventsRegistry: EventsRegistry, private store: Store) {
    this.componentLifecycle = this.componentLifecycle.bind(this);
    this.onNativeEvent = this.onNativeEvent.bind(this);
  }

  public registerForAllComponents(): void {
    this.eventsRegistry.registerComponentLifecycleListener(this.componentLifecycle);
    this.eventsRegistry.registerNativeEventListener(this.onNativeEvent);
  }

  private componentLifecycle(event: LifecycleEvent) {
    switch (event.type) {
      case LifecycleEventType.ComponentDidMount:
        break;
      case LifecycleEventType.ComponentDidAppear:
        const componentRef = this.store.getRefForId(event.componentId);
        if (componentRef && componentRef.componentDidAppear) {
          componentRef.componentDidAppear();
        }
        break;
      case LifecycleEventType.ComponentDidDisappear:
        break;
      case LifecycleEventType.ComponentWillUnmount:
        break;
    }
  }

  private onNativeEvent(name: string, params: any) {
    let componentRef;
    switch (name) {
      case BUTTON_PRESSED_EVENT_NAME:
        componentRef = this.store.getRefForId(params.componentId);
        if (componentRef && componentRef.onNavigationButtonPressed) {
          componentRef.onNavigationButtonPressed(params.buttonId);
        }
        break;
      case ON_SEARCH_BAR_UPDATED:
        componentRef = this.store.getRefForId(params.componentId);
        if (componentRef && componentRef.onSearchBarUpdated) {
          componentRef.onSearchBarUpdated(params.text, params.isFocused);
        }
        break;
      case ON_SEARCH_BAR_CANCEL_PRESSED:
        componentRef = this.store.getRefForId(params.componentId);
        if (componentRef && componentRef.onSearchBarCancelPressed) {
          componentRef.onSearchBarCancelPressed();
        }
        break;
    }
  }
}
