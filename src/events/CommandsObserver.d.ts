import { EventSubscription } from '../interfaces/EventSubscription';
import { UniqueIdProvider } from '../adapters/UniqueIdProvider';
export type CommandsListener = (name: string, params: Record<string, any>) => void;
export declare class CommandsObserver {
    private uniqueIdProvider;
    private listeners;
    constructor(uniqueIdProvider: UniqueIdProvider);
    register(listener: CommandsListener): EventSubscription;
    notify(commandName: string, params: Record<string, any>): void;
}
