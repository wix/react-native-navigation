import { ProcessorSubscription } from '../interfaces/ProcessorSubscription';
import { NavigationMiddleware } from '../interfaces/NavigationMiddleware';

export class CommandMiddlewareStore {
  private middlewares: NavigationMiddleware[] = [];

  public addMiddleware(middleware: NavigationMiddleware): ProcessorSubscription {
    this.middlewares.push(middleware);

    return { remove: () => this.removeMiddleware(middleware) };
  }

  public getMiddlewares(): NavigationMiddleware[] {
    return this.middlewares;
  }

  private removeMiddleware(middleware: NavigationMiddleware) {
    const index = this.middlewares.indexOf(middleware);
    if (index >= 0) {
      this.middlewares.splice(index, 1);
    }
  }
}
