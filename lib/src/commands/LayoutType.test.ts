import { LayoutType, isLayoutType } from './LayoutType';

describe('LayoutType', () => {
  it('is an enum', () => {
    expect(LayoutType.Component).toEqual('Component');
    expect(LayoutType.Stack).toEqual('Stack');

    const name = 'Stack';
    expect(LayoutType[name]).toEqual(LayoutType.Stack);
    expect(LayoutType['asdasd']).toEqual(undefined); //tslint:disable-line
  });

  it('isLayoutType', () => {
    expect(isLayoutType('')).toBe(false);
    expect(isLayoutType('asdasd')).toBe(false);
    expect(isLayoutType('TopTabs')).toBe(true);
    expect(isLayoutType('isLayoutType')).toBe(false);
  });
});
