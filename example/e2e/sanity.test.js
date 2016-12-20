describe('sanity', function() {
  beforeEach(function(done) {
    global.simulator.reloadReactNativeApp(done);
  });

  it('should have screen one showing', function() {
    expect(element(by.label('Screen One'))).toBeVisible();
  });
});
