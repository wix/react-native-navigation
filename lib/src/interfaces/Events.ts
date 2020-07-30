export interface CommandCompletedEvent {
  commandName: string;
  commandId: string;
  completionTime: number;
}

export interface BottomTabSelectedEvent {
  selectedTabIndex: number;
  unselectedTabIndex: number;
}

export interface BottomTabLongPressedEvent {
  selectedTabIndex: number;
}

export interface BottomTabPressedEvent {
  tabIndex: number;
}
