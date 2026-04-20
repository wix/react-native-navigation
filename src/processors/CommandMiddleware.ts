import { CommandMiddlewareStore } from './CommandMiddlewareStore';
import { NavigationCommand } from '../interfaces/NavigationMiddleware';

export class CommandMiddleware {
  constructor(private commandMiddlewareStore: CommandMiddlewareStore) {}

  /**
   * Runs all registered middleware in order.
   * Returns the (possibly transformed) command, or `null` if a middleware cancelled it.
   */
  public run(command: NavigationCommand): NavigationCommand | null {
    const middlewares = this.commandMiddlewareStore.getMiddlewares();
    let current: NavigationCommand | null = command;
    for (const middleware of middlewares) {
      if (current === null) return null;
      current = middleware(current);
    }
    return current;
  }
}
