export interface ComponentDidAppear {
  componentId: string;
  componentName: string;
}

export interface ComponentDidDisappear {
  componentId: string;
  componentName: string;
}

export interface NavigationButtonPressed {
  componentId: string;
  buttonId: string;
}

export interface SearchBarUpdated {
  componentId: string;
  text: string;
  isFocused: boolean;
}

export interface SearchBarCancelPressed {
  componentId: string;
}
