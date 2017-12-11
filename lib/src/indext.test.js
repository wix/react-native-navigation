
describe('index', () => {
  it('imports', () => {
    const mocked = {};
    jest.mock('./Navigation', () => mocked);

    const Navigation = require('./index');
    expect(Navigation).toBeDefined();
    expect(Navigation.FloatingActionButton).toBeDefined();
    expect(Navigation.CoordinatorLayout).toBeDefined();
  });
});
