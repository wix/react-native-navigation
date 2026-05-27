import { ModalLayoutBuilder } from './ModalLayoutBuilder';

describe('ModalLayoutBuilder', () => {
  let uut: ModalLayoutBuilder;

  beforeEach(() => {
    uut = new ModalLayoutBuilder();
  });

  it('wraps a single-segment match in a stack', () => {
    const layout = uut.build({
      url: 'myapp://home',
      path: [{ screen: 'Home', params: {} }],
      queryParams: {},
    });
    expect(layout).toEqual({
      stack: {
        children: [
          {
            component: { name: 'Home', passProps: {} },
          },
        ],
      },
    });
  });

  it('builds a push chain for multi-segment matches', () => {
    const layout = uut.build({
      url: 'myapp://settings/notifications',
      path: [
        { screen: 'Settings', params: {} },
        { screen: 'Notifications', params: {} },
      ],
      queryParams: {},
    });
    expect(layout.stack?.children).toEqual([
      { component: { name: 'Settings', passProps: {} } },
      { component: { name: 'Notifications', passProps: {} } },
    ]);
  });

  it('passes path params as passProps', () => {
    const layout = uut.build({
      url: 'myapp://user/42',
      path: [{ screen: 'Profile', params: { id: '42' } }],
      queryParams: {},
    });
    expect(layout.stack?.children?.[0].component?.passProps).toEqual({ id: '42' });
  });

  it('merges query params into every segment, with path params taking precedence', () => {
    const layout = uut.build({
      url: 'myapp://user/42?id=99&source=push',
      path: [{ screen: 'Profile', params: { id: '42' } }],
      queryParams: { id: '99', source: 'push' },
    });
    expect(layout.stack?.children?.[0].component?.passProps).toEqual({
      id: '42',
      source: 'push',
    });
  });

  it('applies query params to every segment of a nested match', () => {
    const layout = uut.build({
      url: 'myapp://settings/notifications?source=push',
      path: [
        { screen: 'Settings', params: {} },
        { screen: 'Notifications', params: {} },
      ],
      queryParams: { source: 'push' },
    });
    expect(layout.stack?.children?.[0].component?.passProps).toEqual({ source: 'push' });
    expect(layout.stack?.children?.[1].component?.passProps).toEqual({ source: 'push' });
  });

  it('filters the React-reserved "ref" query param from passProps', () => {
    const layout = uut.build({
      url: 'myapp://user/42?ref=notification&utm=push',
      path: [{ screen: 'Profile', params: { id: '42' } }],
      queryParams: { ref: 'notification', utm: 'push' },
    });
    expect(layout.stack?.children?.[0].component?.passProps).toEqual({
      id: '42',
      utm: 'push',
    });
  });

  it('filters the React-reserved "key" query param from passProps', () => {
    const layout = uut.build({
      url: 'myapp://home?key=abc',
      path: [{ screen: 'Home', params: {} }],
      queryParams: { key: 'abc' },
    });
    expect(layout.stack?.children?.[0].component?.passProps).toEqual({});
  });

  it('filters reserved keys when they appear as path params too', () => {
    const layout = uut.build({
      url: 'myapp://r/abc',
      path: [{ screen: 'Home', params: { ref: 'abc' } }],
      queryParams: {},
    });
    expect(layout.stack?.children?.[0].component?.passProps).toEqual({});
  });
});
