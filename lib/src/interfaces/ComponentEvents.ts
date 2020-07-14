export type ComponentType = 'Component' | 'TopBarTitle' | 'TopBarBackground' | 'TopBarButton';

export interface ComponentEvent {
  componentId: string;
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
