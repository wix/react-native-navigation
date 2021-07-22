import {
  ComponentDidDisappearEvent,
  ComponentWillAppearEvent,
  ModalDismissedEvent,
} from '../../interfaces/ComponentEvents';
import { ComponentDidAppearEvent, NavigationButtonPressedEvent } from '../../index';

export const events = {
  navigationButtonPressed: [(_event: NavigationButtonPressedEvent) => {}],
  componentWillAppear: [(_event: ComponentWillAppearEvent) => {}],
  componentDidAppear: [(_event: ComponentDidAppearEvent) => {}],
  componentDidDisappear: [(_event: ComponentDidDisappearEvent) => {}],
  modalDismissed: [(_event: ModalDismissedEvent) => {}],
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
};
