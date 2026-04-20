import { CommandMiddleware } from './CommandMiddleware';
import { CommandMiddlewareStore } from './CommandMiddlewareStore';
import { CommandName } from '../interfaces/CommandName';
import { NavigationCommand } from '../interfaces/NavigationMiddleware';

describe('Command middleware', () => {
  let uut: CommandMiddleware;
  let store: CommandMiddlewareStore;
  beforeEach(() => {
    store = new CommandMiddlewareStore();
    uut = new CommandMiddleware(store);
  });

  it('returns the command unchanged when store is empty', () => {
    const command: NavigationCommand = {
      commandName: CommandName.Push,
      componentId: 'c1',
      layout: { component: { name: 'Screen' } },
    };
    expect(uut.run(command)).toEqual(command);
  });

  it('allows a middleware to observe without modifying', () => {
    const seen: NavigationCommand[] = [];
    store.addMiddleware((cmd) => {
      seen.push(cmd);
      return cmd;
    });
    const command: NavigationCommand = {
      commandName: CommandName.ShowModal,
      layout: { component: { name: 'Modal' } },
    };
    const result = uut.run(command);
    expect(result).toEqual(command);
    expect(seen).toEqual([command]);
  });

  it('allows a middleware to rewrite the layout (auth guard pattern)', () => {
    store.addMiddleware((cmd) => {
      if (cmd.commandName === CommandName.Push) {
        return { ...cmd, layout: { component: { name: 'LoginScreen' } } };
      }
      return cmd;
    });
    const result = uut.run({
      commandName: CommandName.Push,
      componentId: 'c1',
      layout: { component: { name: 'SecretScreen' } },
    });
    expect(result?.layout).toEqual({ component: { name: 'LoginScreen' } });
  });

  it('cancels the command when a middleware returns null', () => {
    store.addMiddleware(() => null);
    store.addMiddleware(() => {
      throw new Error('should not be reached after cancel');
    });
    const result = uut.run({ commandName: CommandName.Pop, componentId: 'c1' });
    expect(result).toBeNull();
  });

  it('chains middlewares in registration order', () => {
    store.addMiddleware((cmd) => ({ ...cmd, componentId: `${cmd.componentId}-a` }));
    store.addMiddleware((cmd) => ({ ...cmd, componentId: `${cmd.componentId}-b` }));
    const result = uut.run({ commandName: CommandName.Pop, componentId: 'c' });
    expect(result?.componentId).toEqual('c-a-b');
  });

  it('unsubscribes a middleware via ProcessorSubscription.remove', () => {
    const sub = store.addMiddleware(() => null);
    sub.remove();
    const command: NavigationCommand = { commandName: CommandName.Pop, componentId: 'c' };
    expect(uut.run(command)).toEqual(command);
  });
});
