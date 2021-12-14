import {
  ComponentDidDisappearEvent,
  ComponentWillAppearEvent,
  ModalDismissedEvent,
} from '../../src/interfaces/ComponentEvents';
import { ComponentDidAppearEvent, NavigationButtonPressedEvent } from '../../src/index';
import { BottomTabPressedEvent } from '../../src/interfaces/Events';

export const events = {
  navigationButtonPressed: [(_event: NavigationButtonPressedEvent) => {}],
  componentWillAppear: [(_event: ComponentWillAppearEvent) => {}],
  componentDidAppear: [(_event: ComponentDidAppearEvent) => {}],
  componentDidDisappear: [(_event: ComponentDidDisappearEvent) => {}],
  modalDismissed: [(_event: ModalDismissedEvent) => {}],
  bottomTabPressed: [(_event: BottomTabPressedEvent) => {}],
  invokeComponentWillAppear: (event: ComponentWillAppearEvent) => {
    events.componentWillAppear &&
      events.componentWillAppear.forEach((listener) => {
        listener(event);
      });
  },
  invokeComponentDidAppear: (event: ComponentDidAppearEvent) => {
    events.componentDidAppear &&
      events.componentDidAppear.forEach((listener) => {
        listener(event);
      });
  },
  invokeComponentDidDisappear: (event: ComponentDidDisappearEvent) => {
    events.componentDidDisappear &&
      events.componentDidDisappear.forEach((listener) => {
        listener(event);
      });
  },
  invokeModalDismissed: (event: ModalDismissedEvent) => {
    events.modalDismissed &&
      events.modalDismissed.forEach((listener) => {
        listener(event);
      });
  },
  invokeNavigationButtonPressed: (event: NavigationButtonPressedEvent) => {
    events.navigationButtonPressed &&
      events.navigationButtonPressed.forEach((listener) => {
        listener(event);
      });
  },
  invokeBottomTabPressed: (event: BottomTabPressedEvent) => {
    events.bottomTabPressed &&
      events.bottomTabPressed?.forEach((listener) => {
        listener(event);
      });
  },
};
