export interface ComponentDidAppearEvent {
  componentId: string;
  componentName: string;
}

export interface ComponentDidDisappearEvent {
  componentId: string;
  componentName: string;
}

export interface NavigationButtonPressedEvent {
  componentId: string;
  buttonId: string;
}

export interface SearchBarUpdatedEvent {
  componentId: string;
  text: string;
  isFocused: boolean;
}

export interface SearchBarCancelPressedEvent {
  componentId: string;
}

export interface CommandCompletedEvent {
  commandId: string;
  completionTime: number;
  params: any;
}

export interface BottomTabSelectedEvent {
  selectedTabIndex: number;
  unselectedTabIndex: number;
}
