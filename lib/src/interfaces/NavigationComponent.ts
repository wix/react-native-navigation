import React from "react";
import { NavigationListener } from "./NavigationListener";
import {
  NavigationButtonPressedEvent,
  ModalDismissedEvent,
  ModalAttemptedToDismissEvent,
  SearchBarUpdatedEvent,
  SearchBarCancelPressedEvent,
  PreviewCompletedEvent,
  ScreenPoppedEvent,
} from "./ComponentEvents";
export interface NavigationComponentProps {
  componentId: string;
}

export class NavigationComponent<Props = {}, State = {}, Snapshot = any>
  extends React.Component<Props & NavigationComponentProps, State, Snapshot>
  implements NavigationListener {
  componentDidAppear?: () => void;
  componentDidDisappear?: () => void;
  navigationButtonPressed?: (_event: NavigationButtonPressedEvent) => void;
  modalDismissed?: (_event: ModalDismissedEvent) => void;
  modalAttemptedToDismiss?: (_event: ModalAttemptedToDismissEvent) => void;
  searchBarUpdated?: (_event: SearchBarUpdatedEvent) => void;
  searchBarCancelPressed?: (_event: SearchBarCancelPressedEvent) => void;
  previewCompleted?: (_event: PreviewCompletedEvent) => void;
  screenPopped?: (_event: ScreenPoppedEvent) => void;
}
