import { EventsRegistry } from './EventsRegistry';
import { Store } from '../components/Store';

const BUTTON_PRESSED_EVENT_NAME = 'buttonPressed';
const ON_SEARCH_BAR_UPDATED = 'searchBarUpdated';
const ON_SEARCH_BAR_CANCEL_PRESSED = 'searchBarCancelPressed';

export class ComponentEventsObserver {
  constructor(private eventsRegistry: EventsRegistry, private store: Store) {
    this.componentDidAppear = this.componentDidAppear.bind(this);
    this.componentDidDisappear = this.componentDidDisappear.bind(this);
    this.onNativeEvent = this.onNativeEvent.bind(this);
  }

  public registerForAllComponents(): void {
    this.eventsRegistry.registerComponentDidAppearListener(this.componentDidAppear);
    this.eventsRegistry.registerComponentDidDisappearListener(this.componentDidDisappear);
    this.eventsRegistry.registerNativeEventListener(this.onNativeEvent);
  }

  private componentDidAppear(componentId: string) {
    const componentRef = this.store.getRefForId(componentId);
    if (componentRef && componentRef.componentDidAppear) {
      componentRef.componentDidAppear();
    }
  }

  private componentDidDisappear(componentId: string) {
    const componentRef = this.store.getRefForId(componentId);
    if (componentRef && componentRef.componentDidDisappear) {
      componentRef.componentDidDisappear();
    }
  }

  private onNativeEvent(name: string, params: any) {
    if (name === BUTTON_PRESSED_EVENT_NAME) {
      const componentRef = this.store.getRefForId(params.componentId);
      if (componentRef && componentRef.onNavigationButtonPressed) {
        componentRef.onNavigationButtonPressed(params.buttonId);
      }
    }
    if (name === ON_SEARCH_BAR_UPDATED) {
      const componentRef = this.store.getRefForId(params.componentId);
      if (componentRef && componentRef.onSearchBarUpdated) {
        componentRef.onSearchBarUpdated(params.text, params.isFocused);
      }
    }
    if (name === ON_SEARCH_BAR_CANCEL_PRESSED) {
      const componentRef = this.store.getRefForId(params.componentId);
      if (componentRef && componentRef.onSearchBarCancelPressed) {
        componentRef.onSearchBarCancelPressed();
      }
    }
  }
}
