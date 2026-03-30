import {
  NavigationButtonPressedEvent,
  ModalDismissedEvent,
  ModalAttemptedToDismissEvent,
  SearchBarUpdatedEvent,
  SearchBarCancelPressedEvent,
  PreviewCompletedEvent,
  ScreenPoppedEvent,
  ComponentWillAppearEvent,
  ComponentDidAppearEvent,
  ComponentDidDisappearEvent,
} from './ComponentEvents';

export interface NavigationComponentListener {
  componentWillAppear?: (_event: ComponentWillAppearEvent) => void;
  componentDidAppear?: (_event: ComponentDidAppearEvent) => void;
  componentDidDisappear?: (_event: ComponentDidDisappearEvent) => void;
  navigationButtonPressed?: (_event: NavigationButtonPressedEvent) => void;
  modalDismissed?: (_event: ModalDismissedEvent) => void;
  modalAttemptedToDismiss?: (_event: ModalAttemptedToDismissEvent) => void;
  searchBarUpdated?: (_event: SearchBarUpdatedEvent) => void;
  searchBarCancelPressed?: (_event: SearchBarCancelPressedEvent) => void;
  previewCompleted?: (_event: PreviewCompletedEvent) => void;
  screenPopped?: (_event: ScreenPoppedEvent) => void;
}
