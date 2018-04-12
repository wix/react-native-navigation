import { CommandsObserver } from './CommandsObserver';

describe('CommandsObserver', () => {
  let uut: CommandsObserver;
  let cb1;
  let cb2;

  beforeEach(() => {
    cb1 = jest.fn();
    cb2 = jest.fn();
    uut = new CommandsObserver();
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
    uut.register(cb1);
    const result = uut.register(cb2);
    expect(result).toBeDefined();

    uut.notify('commandName', {});
    expect(cb1).toHaveBeenCalledTimes(1);
    expect(cb2).toHaveBeenCalledTimes(1);

    result.remove();

    uut.notify('commandName', {});
    expect(cb1).toHaveBeenCalledTimes(2);
    expect(cb2).toHaveBeenCalledTimes(1);
  });
});
