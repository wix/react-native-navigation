describe('sanity', () => {

  beforeEach(async () => await global.device.reloadReactNative());

  it('should push a screen', async () => {
    await element(by.id('pushScreen')).tap();

    await expect(element(by.label('Pushed Screen'))).toBeVisible();
  });

});
