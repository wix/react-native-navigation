import { LayoutType } from './LayoutType';

describe('LayoutType', () => {
  it('is an enum', () => {
    expect(LayoutType.Component).toEqual('Component');
    expect(LayoutType.Stack).toEqual('Stack');

    const name = 'Stack';
    expect(LayoutType[name]).toEqual(LayoutType.Stack);
  });
});
