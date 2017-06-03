jest.mock('./Navigation', () => ({ startApp: () => 'import' }));
import Navigation from './index';

describe('index', () => {
  let uut;

  beforeEach(() => {
    jest.mock('./Navigation', () => ({ startApp: () => 'require' }));
    uut = require('./index');
  });

  it('exposes Navigation', () => {
    expect(uut.startApp()).toEqual('require');
    expect(Navigation.startApp()).toEqual('import');
  });
});
