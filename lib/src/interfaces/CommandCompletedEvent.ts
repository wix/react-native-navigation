export interface CommandCompletedEvent {
  commandId: string;
  completionTime: number;
  params: any;
}
