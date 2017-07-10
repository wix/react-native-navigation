describe('Navigation', () => {
  let Navigation;

  beforeEach(() => {
    //TODO don't mock here, just create the actual object and assert on basic behavior
    jest.mock('./adapters/UniqueIdProvider');
    jest.mock('./adapters/NativeCommandsSender');
    jest.mock('./adapters/NativeEventsReceiver');

    jest.mock('./containers/ContainerRegistry');
    jest.mock('./commands/Commands');

    Navigation = require('./Navigation').default;
  });

  it('registerContainer delegates to ContainerRegistry', () => {
    expect(Navigation.containerRegistry.registerContainer).not.toHaveBeenCalled();
    const fn = jest.fn();
    Navigation.registerContainer('name', fn);
    expect(Navigation.containerRegistry.registerContainer).toHaveBeenCalledTimes(1);
    expect(Navigation.containerRegistry.registerContainer).toHaveBeenCalledWith('name', fn);
  });

  it('setRoot delegates to Commands', async () => {
    Navigation.commands.setRoot.mockReturnValue(Promise.resolve('result'));
    const params = {};
    const result = await Navigation.setRoot(params);
    expect(result).toEqual('result');
    expect(Navigation.commands.setRoot).toHaveBeenCalledTimes(1);
    expect(Navigation.commands.setRoot).toHaveBeenCalledWith(params);
  });

  it('setOptions delegates to Commands', async () => {
    const theContainerId = "7";
    const params = { title: "T" };
    Navigation.setOptions(theContainerId, params);
    expect(Navigation.commands.setOptions).toHaveBeenCalledTimes(1);
    expect(Navigation.commands.setOptions).toHaveBeenCalledWith(theContainerId, params);
  });

  it('showModal delegates to Commands', async () => {
    Navigation.commands.showModal.mockReturnValue(Promise.resolve('result'));
    const params = {};
    const result = await Navigation.showModal(params);
    expect(result).toEqual('result');
    expect(Navigation.commands.showModal).toHaveBeenCalledWith(params);
    expect(Navigation.commands.showModal).toHaveBeenCalledTimes(1);
  });

  it('dismissModal delegates to Commands', async () => {
    Navigation.commands.dismissModal.mockReturnValue(Promise.resolve('result'));
    const params = {};
    const result = await Navigation.dismissModal(params);
    expect(result).toEqual('result');
    expect(Navigation.commands.dismissModal).toHaveBeenCalledTimes(1);
    expect(Navigation.commands.dismissModal).toHaveBeenCalledWith(params);
  });

  it('dismissAllModals', async () => {
    Navigation.commands.dismissAllModals.mockReturnValue(Promise.resolve('result'));
    const result = await Navigation.dismissAllModals();
    expect(Navigation.commands.dismissAllModals).toHaveBeenCalledTimes(1);
    expect(result).toEqual('result');
  });

  it('events return public events', () => {
    const cb = jest.fn();
    Navigation.events().onAppLaunched(cb);
    expect(Navigation.nativeEventsReceiver.appLaunched).toHaveBeenCalledTimes(1);
    expect(Navigation.nativeEventsReceiver.appLaunched).toHaveBeenCalledWith(cb);
  });

  it('starts listening and handles internal events', () => {
    expect(Navigation.nativeEventsReceiver.containerStart).toHaveBeenCalledTimes(1);
  });

  it('push delegates to commands', async () => {
    Navigation.commands.push.mockReturnValue(Promise.resolve('result'));
    const params = {};
    const result = await Navigation.push('theContainerId', params);
    expect(result).toEqual('result');
    expect(Navigation.commands.push).toHaveBeenCalledWith('theContainerId', params);
    expect(Navigation.commands.push).toHaveBeenCalledTimes(1);
  });

  it('pop delegates to commands', async () => {
    Navigation.commands.pop.mockReturnValue(Promise.resolve('result'));
    const result = await Navigation.pop('theContainerId');
    expect(result).toEqual('result');
    expect(Navigation.commands.pop).toHaveBeenCalledWith('theContainerId');
    expect(Navigation.commands.pop).toHaveBeenCalledTimes(1);
  });

  it('popTo delegates to commands', async () => {
    Navigation.commands.popTo.mockReturnValue(Promise.resolve('result'));
    const params = {};
    const result = await Navigation.popTo('theContainerId');
    expect(result).toEqual('result');
    expect(Navigation.commands.popTo).toHaveBeenCalledWith('theContainerId');
    expect(Navigation.commands.popTo).toHaveBeenCalledTimes(1);
  });

  it('popToRoot delegates to commands', async () => {
    Navigation.commands.popToRoot.mockReturnValue(Promise.resolve('result'));
    const params = {};
    const result = await Navigation.popToRoot('theContainerId');
    expect(result).toEqual('result');
    expect(Navigation.commands.popToRoot).toHaveBeenCalledWith('theContainerId');
    expect(Navigation.commands.popToRoot).toHaveBeenCalledTimes(1);
  });
});
