const Container = require('./Container');

const PASS_PROPS = {
  number: 9
};
const CONTAINER = {
  name: 'myScreen',
  passProps: PASS_PROPS
};

describe('ContainerRegistry', () => {
  it('parses container correctly', () => {
    const uut = new Container(CONTAINER);
    expect(uut.name).toBe('myScreen');
    expect(uut.passProps).toEqual(PASS_PROPS);
  });

  it('throws if container name is missing', () => {
    expect(() => new Container()).toThrowError('Container name is undefined');
  });
});
