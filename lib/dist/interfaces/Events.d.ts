export interface CommandCompletedEvent {
    commandName: string;
    commandId: string;
    completionTime: number;
    params: any;
}
export interface BottomTabSelectedEvent {
    selectedTabIndex: number;
    unselectedTabIndex: number;
}
