import * as _ from 'lodash';
import { EventSubscription } from '../interfaces/EventSubscription';

export type CommandsListener = (name: string, params: {}) => void;

export class CommandsObserver {
  private readonly listeners = {};

  public register(listener: CommandsListener): EventSubscription {
    const id = _.uniqueId();
    _.set(this.listeners, id, listener);
    return {
      remove: () => _.unset(this.listeners, id)
    };
  }

  public notify(commandName: string, params: {}): void {
    _.forEach(this.listeners, (listener: CommandsListener) => listener(commandName, params));
  }
}
