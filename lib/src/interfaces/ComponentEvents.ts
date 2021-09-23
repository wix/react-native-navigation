export type ComponentType = 'Component' | 'TopBarTitle' | 'TopBarBackground' | 'TopBarButton';
export type PIPState =
  | 'NOT_STARTED'
  | 'MOUNT_START'
  | 'CUSTOM_MOUNTED'
  | 'CUSTOM_EXPANDED'
  | 'CUSTOM_COMPACT'
  | 'RESTORE_START'
  | 'UNMOUNT_START'
  | 'NATIVE_MOUNTED'
  | 'NATIVE_MOUNT_START';

export interface ComponentEvent {
  componentId: string;
}

export interface ComponentWillAppearEvent extends ComponentEvent {
  componentName: string;
  passProps?: object;
  componentType: ComponentType;
}

export interface ComponentDidAppearEvent extends ComponentEvent {
  componentName: string;
  passProps?: object;
  componentType: ComponentType;
}

export interface ComponentDidDisappearEvent extends ComponentEvent {
  componentName: string;
  componentType: ComponentType;
}

export interface NavigationButtonPressedEvent extends ComponentEvent {
  buttonId: string;
}

export interface ModalDismissedEvent extends ComponentEvent {
  componentName: string;
  modalsDismissed: number;
}

export interface ModalAttemptedToDismissEvent extends ComponentEvent {
  componentId: string;
}

export interface SearchBarUpdatedEvent extends ComponentEvent {
  text: string;
  isFocused: boolean;
}

export interface SearchBarCancelPressedEvent extends ComponentEvent {
  componentName?: string;
}

export interface PreviewCompletedEvent extends ComponentEvent {
  componentName?: string;
  previewComponentId?: string;
}

export interface ScreenPoppedEvent extends ComponentEvent {
  componentId: string;
}

export interface PIPStateChangedEvent extends ComponentEvent {
  prevState: PIPState;
  newState: PIPState;
}

export interface PIPButtonPressedEvent extends ComponentEvent {
  buttonId: string;
}
