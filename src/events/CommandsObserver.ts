import { EventSubscription } from '../interfaces/EventSubscription';
import { UniqueIdProvider } from '../adapters/UniqueIdProvider';

export type CommandsListener = (name: string, params: Record<string, any>) => void;

export class CommandsObserver {
  private listeners: Record<string, CommandsListener> = {};
  constructor(private uniqueIdProvider: UniqueIdProvider) {}

  public register(listener: CommandsListener): EventSubscription {
    const id = this.uniqueIdProvider.generate();
    this.listeners[id] = listener;
    return {
      remove: () => delete this.listeners[id],
    };
  }

  public notify(commandName: string, params: Record<string, any>): void {
    Object.values(this.listeners).forEach((listener) => listener(commandName, params));
  }
}
