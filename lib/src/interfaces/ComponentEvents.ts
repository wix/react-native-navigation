export interface ComponentEvent {
  componentId: string;
}

export interface ComponentDidAppearEvent extends ComponentEvent {
  componentName: string;
}

export interface ComponentDidDisappearEvent extends ComponentEvent {
  componentName: string;
}

export interface NavigationButtonPressedEvent extends ComponentEvent {
  buttonId: string;
}

export interface ModalDismissedEvent extends ComponentEvent {
  componentId: string;
}

export interface SearchBarUpdatedEvent extends ComponentEvent {
  text: string;
  isFocused: boolean;
}

export interface SearchBarCancelPressedEvent extends ComponentEvent {
  componentName?: string;
}
