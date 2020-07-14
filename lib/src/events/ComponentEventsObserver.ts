import isString from 'lodash/isString'
import isNil from 'lodash/isNil'
import uniqueId from 'lodash/uniqueId'
import unset from 'lodash/unset'
import forEach from 'lodash/forEach'
import { EventSubscription } from '../interfaces/EventSubscription';
import { NavigationComponentListener } from '../interfaces/NavigationComponentListener';
import {
  ComponentDidAppearEvent,
  ComponentDidDisappearEvent,
  NavigationButtonPressedEvent,
  SearchBarUpdatedEvent,
  SearchBarCancelPressedEvent,
  ComponentEvent,
  PreviewCompletedEvent,
  ModalDismissedEvent,
  ScreenPoppedEvent,
  ModalAttemptedToDismissEvent
} from '../interfaces/ComponentEvents';
import { NativeEventsReceiver } from '../adapters/NativeEventsReceiver';
import { Store } from '../components/Store';

type ReactComponentWithIndexing = NavigationComponentListener & Record<string, any>;

export class ComponentEventsObserver {
  private listeners: Record<string, Record<string, ReactComponentWithIndexing>> = {};
  private alreadyRegistered = false;

  constructor(
    private readonly nativeEventsReceiver: NativeEventsReceiver,
    private readonly store: Store
  ) {
    this.notifyComponentDidAppear = this.notifyComponentDidAppear.bind(this);
    this.notifyComponentDidDisappear = this.notifyComponentDidDisappear.bind(this);
    this.notifyNavigationButtonPressed = this.notifyNavigationButtonPressed.bind(this);
    this.notifyModalDismissed = this.notifyModalDismissed.bind(this);
    this.notifyModalAttemptedToDismiss = this.notifyModalAttemptedToDismiss.bind(this);
    this.notifySearchBarUpdated = this.notifySearchBarUpdated.bind(this);
    this.notifySearchBarCancelPressed = this.notifySearchBarCancelPressed.bind(this);
    this.notifyPreviewCompleted = this.notifyPreviewCompleted.bind(this);
    this.notifyScreenPopped = this.notifyScreenPopped.bind(this);
  }

  public registerOnceForAllComponentEvents() {
    if (this.alreadyRegistered) { return; }
    this.alreadyRegistered = true;
    this.nativeEventsReceiver.registerComponentDidAppearListener(this.notifyComponentDidAppear);
    this.nativeEventsReceiver.registerComponentDidDisappearListener(this.notifyComponentDidDisappear);
    this.nativeEventsReceiver.registerNavigationButtonPressedListener(this.notifyNavigationButtonPressed);
    this.nativeEventsReceiver.registerModalDismissedListener(this.notifyModalDismissed);
    this.nativeEventsReceiver.registerModalAttemptedToDismissListener(this.notifyModalAttemptedToDismiss);
    this.nativeEventsReceiver.registerSearchBarUpdatedListener(this.notifySearchBarUpdated);
    this.nativeEventsReceiver.registerSearchBarCancelPressedListener(this.notifySearchBarCancelPressed);
    this.nativeEventsReceiver.registerPreviewCompletedListener(this.notifyPreviewCompleted);
    this.nativeEventsReceiver.registerScreenPoppedListener(this.notifyPreviewCompleted);
  }

  public bindComponent(component: React.Component<any>, componentId?: string): EventSubscription {
    const computedComponentId = componentId || component.props.componentId;

    if (!isString(computedComponentId)) {
      throw new Error(`bindComponent expects a component with a componentId in props or a componentId as the second argument`);
    }
    
    return this.registerComponentListener(component as NavigationComponentListener, computedComponentId);
  }

  public registerComponentListener(listener: NavigationComponentListener, componentId: string): EventSubscription {
    if (!isString(componentId)) {
      throw new Error(`registerComponentListener expects a componentId as the second argument`);
    }
    if (isNil(this.listeners[componentId])) {
      this.listeners[componentId] = {};
    }
    const key = uniqueId();
    this.listeners[componentId][key] = listener;

    return { remove: () => unset(this.listeners[componentId], key) };
  }

  public unmounted(componentId: string) {
    unset(this.listeners, componentId);
  }

  notifyComponentDidAppear(event: ComponentDidAppearEvent) {
    event.passProps = this.store.getPropsForId(event.componentId);
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

  notifyModalAttemptedToDismiss(event: ModalAttemptedToDismissEvent) {
    this.triggerOnAllListenersByComponentId(event, 'modalAttemptedToDismiss');
  }

  notifySearchBarUpdated(event: SearchBarUpdatedEvent) {
    this.triggerOnAllListenersByComponentId(event, 'searchBarUpdated');
  }

  notifySearchBarCancelPressed(event: SearchBarCancelPressedEvent) {
    this.triggerOnAllListenersByComponentId(event, 'searchBarCancelPressed');
  }

  notifyPreviewCompleted(event: PreviewCompletedEvent) {
    this.triggerOnAllListenersByComponentId(event, 'previewCompleted');
  }

  notifyScreenPopped(event: ScreenPoppedEvent) {
    this.triggerOnAllListenersByComponentId(event, 'screenPopped');
  }

  private triggerOnAllListenersByComponentId(event: ComponentEvent, method: string) {
    forEach(this.listeners[event.componentId], (component) => {
      if (component && component[method]) {
        component[method](event);
      }
    });
  }
}
