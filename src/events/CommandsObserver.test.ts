import { CommandsObserver, CommandsListener } from './CommandsObserver';
import { UniqueIdProvider } from '../adapters/UniqueIdProvider';

describe('CommandsObserver', () => {
  let uut: CommandsObserver;
  let cb1: CommandsListener;
  let cb2: CommandsListener;

  beforeEach(() => {
    cb1 = jest.fn();
    cb2 = jest.fn();
    uut = new CommandsObserver(new UniqueIdProvider());
  });

  it('register and notify listener', () => {
    const theCommandName = 'theCommandName';
    const theParams = { x: 1 };

    uut.register(cb1);
    uut.register(cb2);

    expect(cb1).toHaveBeenCalledTimes(0);
    expect(cb2).toHaveBeenCalledTimes(0);

    uut.notify(theCommandName, theParams);

    expect(cb1).toHaveBeenCalledTimes(1);
    expect(cb1).toHaveBeenCalledWith(theCommandName, theParams);
    expect(cb2).toHaveBeenCalledTimes(1);
    expect(cb2).toHaveBeenCalledWith(theCommandName, theParams);
  });

  it('remove listener', () => {
    expect(cb1).toHaveBeenCalledTimes(0);
    expect(cb2).toHaveBeenCalledTimes(0);

    uut.register(cb1);
    const cb2Subscription = uut.register(cb2);

    uut.notify('commandName', {});
    expect(cb1).toHaveBeenCalledTimes(1);
    expect(cb2).toHaveBeenCalledTimes(1);

    cb2Subscription.remove();

    uut.notify('commandName', {});
    expect(cb1).toHaveBeenCalledTimes(2);
    expect(cb2).toHaveBeenCalledTimes(1);
  });
});
