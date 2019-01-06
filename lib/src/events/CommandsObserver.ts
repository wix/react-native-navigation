import { EventSubscription } from '../interfaces/EventSubscription';
import { UniqueIdProvider } from '../adapters/UniqueIdProvider';
import { Service, Inject } from 'typedi';

export type CommandsListener = (name: string, params: Record<string, any>) => void;

@Service()
export class CommandsObserver {
  @Inject()
  public uniqueIdProvider!: UniqueIdProvider;

  private listeners: Record<string, CommandsListener> = {};

  public register(listener: CommandsListener): EventSubscription {
    const id = this.uniqueIdProvider.generate();
    this.listeners[id] = listener;
    return {
      remove: () => delete this.listeners[id]
    };
  }

  public notify(commandName: string, params: Record<string, any>): void {
    Object.values(this.listeners).forEach((listener) => listener(commandName, params));
  }
}
