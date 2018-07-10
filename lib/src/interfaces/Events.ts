export interface CommandCompletedEvent {
  commandId: string;
  completionTime: number;
  params: any;
}

export interface BottomTabSelectedEvent {
  selectedTabIndex: number;
  unselectedTabIndex: number;
}
