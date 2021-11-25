import _ from 'lodash';
import { EmitterSubscription } from 'react-native';
import {
  ComponentWillAppearEvent,
  ComponentDidAppearEvent,
  ComponentDidDisappearEvent,
  NavigationButtonPressedEvent,
  SearchBarUpdatedEvent,
  SearchBarCancelPressedEvent,
  PreviewCompletedEvent,
  ModalDismissedEvent,
  ScreenPoppedEvent,
  ModalAttemptedToDismissEvent,
} from '../../src/interfaces/ComponentEvents';
import {
  CommandCompletedEvent,
  BottomTabSelectedEvent,
  BottomTabLongPressedEvent,
  BottomTabPressedEvent,
} from '../../src/interfaces/Events';
import { events } from './../Stores/EventsStore';

export class NativeEventsReceiver {
  public registerAppLaunchedListener(callback: () => void): EmitterSubscription {
    callback();
    return {
      remove: () => {},
    } as EmitterSubscription;
  }

  public registerComponentWillAppearListener(
    callback: (event: ComponentWillAppearEvent) => void
  ): EmitterSubscription {
    events.componentWillAppear.push(callback);
    return {
      remove: () => {
        _.remove(events.componentWillAppear, (value) => value === callback);
      },
    } as EmitterSubscription;
  }

  public registerComponentDidAppearListener(
    callback: (event: ComponentDidAppearEvent) => void
  ): EmitterSubscription {
    events.componentDidAppear.push(callback);
    return {
      remove: () => {
        _.remove(events.componentDidAppear, (value) => value === callback);
      },
    } as EmitterSubscription;
  }

  public registerComponentDidDisappearListener(
    callback: (event: ComponentDidDisappearEvent) => void
  ): EmitterSubscription {
    events.componentDidDisappear.push(callback);
    return {
      remove: () => {
        _.remove(events.componentDidDisappear, (value) => value === callback);
      },
    } as EmitterSubscription;
  }

  public registerNavigationButtonPressedListener(
    callback: (event: NavigationButtonPressedEvent) => void
  ): EmitterSubscription {
    events.navigationButtonPressed.push(callback);
    return {
      remove: () => {
        _.remove(events.navigationButtonPressed, (value) => value === callback);
      },
    } as EmitterSubscription;
  }

  public registerBottomTabPressedListener(
    callback: (data: BottomTabPressedEvent) => void
  ): EmitterSubscription {
    events.bottomTabPressed.push(callback);
    return {
      remove: () => {
        _.remove(events.bottomTabPressed, (value) => value === callback);
      },
    } as EmitterSubscription;
  }

  public registerModalDismissedListener(
    callback: (event: ModalDismissedEvent) => void
  ): EmitterSubscription {
    events.modalDismissed.push(callback);
    return {
      remove: () => {
        _.remove(events.modalDismissed, (value) => value === callback);
      },
    } as EmitterSubscription;
  }

  public registerModalAttemptedToDismissListener(
    _callback: (event: ModalAttemptedToDismissEvent) => void
  ): EmitterSubscription {
    return {
      remove: () => {},
    } as EmitterSubscription;
  }

  public registerSearchBarUpdatedListener(
    _callback: (event: SearchBarUpdatedEvent) => void
  ): EmitterSubscription {
    return {
      remove: () => {},
    } as EmitterSubscription;
  }

  public registerSearchBarCancelPressedListener(
    _callback: (event: SearchBarCancelPressedEvent) => void
  ): EmitterSubscription {
    return {
      remove: () => {},
    } as EmitterSubscription;
  }

  public registerPreviewCompletedListener(
    _callback: (event: PreviewCompletedEvent) => void
  ): EmitterSubscription {
    return {
      remove: () => {},
    } as EmitterSubscription;
  }

  public registerCommandCompletedListener(
    _callback: (data: CommandCompletedEvent) => void
  ): EmitterSubscription {
    return {
      remove: () => {},
    } as EmitterSubscription;
  }

  public registerBottomTabSelectedListener(
    _callback: (data: BottomTabSelectedEvent) => void
  ): EmitterSubscription {
    return {
      remove: () => {},
    } as EmitterSubscription;
  }

  public registerBottomTabLongPressedListener(
    _callback: (data: BottomTabLongPressedEvent) => void
  ): EmitterSubscription {
    return {
      remove: () => {},
    } as EmitterSubscription;
  }

  public registerScreenPoppedListener(
    _callback: (event: ScreenPoppedEvent) => void
  ): EmitterSubscription {
    return {
      remove: () => {},
    } as EmitterSubscription;
  }
}
