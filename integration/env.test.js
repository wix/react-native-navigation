const eq = require('lodash/eq');
const isEqual = require('lodash/isEqual');

describe('testing that the environment is working properly', () => {
  it('object spread', () => {
    const { x, y, ...z } = { x: 1, y: 2, a: 3, b: 4 };
    expect(x).toEqual(1);
    expect(y).toEqual(2);
    expect(z).toEqual({ a: 3, b: 4 });
  });

  it('async await', async () => {
    const result = await new Promise((r) => r('hello'));
    expect(result).toEqual('hello');
  });

  it('equality tests', () => {
    expect(eq('hello', 'hello')).toBe(true);
    expect(isEqual('hello', 'hello')).toBe(true);

    expect(eq('hello', Object('hello'))).toBe(false);
    expect(isEqual('hello', Object('hello'))).toBe(true);

    const a = {};
    const b = {};

    expect(eq(a, b)).toBe(false);
    expect(isEqual(a, b)).toBe(true);
  });
});
