import { RouteMatcher } from './RouteMatcher';
import { ScreensConfig } from './types';

describe('RouteMatcher', () => {
  let uut: RouteMatcher;

  beforeEach(() => {
    uut = new RouteMatcher();
  });

  function configure(screens: ScreensConfig) {
    uut.setRouteTree(uut.buildRouteTree(screens));
  }

  it('matches a flat route', () => {
    configure({ Home: 'home' });
    expect(uut.match('home', 'myapp://home', {})).toEqual({
      url: 'myapp://home',
      path: [{ screen: 'Home', params: {} }],
      queryParams: {},
    });
  });

  it('extracts a single path parameter', () => {
    configure({ Profile: 'user/:id' });
    expect(uut.match('user/42', 'myapp://user/42', {})).toEqual({
      url: 'myapp://user/42',
      path: [{ screen: 'Profile', params: { id: '42' } }],
      queryParams: {},
    });
  });

  it('extracts multiple path parameters', () => {
    configure({ Post: 'user/:userId/post/:postId' });
    const result = uut.match('user/5/post/99', 'myapp://user/5/post/99', {});
    expect(result?.path[0].params).toEqual({ userId: '5', postId: '99' });
  });

  it('returns null when no route matches', () => {
    configure({ Home: 'home' });
    expect(uut.match('unknown', 'myapp://unknown', {})).toBeNull();
  });

  it('returns null when path is longer than any pattern', () => {
    configure({ Home: 'home' });
    expect(uut.match('home/extra', 'myapp://home/extra', {})).toBeNull();
  });

  it('matches the first registered route on ambiguity', () => {
    configure({
      Profile: 'user/:id',
      User: 'user/:userId',
    });
    const result = uut.match('user/42', 'myapp://user/42', {});
    expect(result?.path[0].screen).toBe('Profile');
  });

  it('matches a nested route as a chain', () => {
    configure({
      Settings: {
        path: 'settings',
        screens: { Notifications: 'notifications' },
      },
    });
    expect(uut.match('settings/notifications', 'myapp://settings/notifications', {})).toEqual({
      url: 'myapp://settings/notifications',
      path: [
        { screen: 'Settings', params: {} },
        { screen: 'Notifications', params: {} },
      ],
      queryParams: {},
    });
  });

  it('matches a parent-only nested route', () => {
    configure({
      Settings: {
        path: 'settings',
        screens: { Notifications: 'notifications' },
      },
    });
    expect(uut.match('settings', 'myapp://settings', {})).toEqual({
      url: 'myapp://settings',
      path: [{ screen: 'Settings', params: {} }],
      queryParams: {},
    });
  });

  it('supports grouping nodes that consume no segments', () => {
    configure({
      Main: {
        screens: {
          Feed: 'feed',
          Search: 'search',
        },
      },
    });
    const result = uut.match('feed', 'myapp://feed', {});
    expect(result?.path).toEqual([
      { screen: 'Main', params: {} },
      { screen: 'Feed', params: {} },
    ]);
  });

  it('passes query params through to the match result', () => {
    configure({ Home: 'home' });
    const result = uut.match('home', 'myapp://home?ref=push', { ref: 'push' });
    expect(result?.queryParams).toEqual({ ref: 'push' });
  });

  it('returns null when no route tree has been configured', () => {
    expect(uut.match('home', 'myapp://home', {})).toBeNull();
  });

  it('matches nested params at multiple levels', () => {
    configure({
      Org: {
        path: 'org/:orgId',
        screens: { Project: 'project/:projectId' },
      },
    });
    const result = uut.match('org/1/project/2', 'myapp://org/1/project/2', {});
    expect(result?.path).toEqual([
      { screen: 'Org', params: { orgId: '1' } },
      { screen: 'Project', params: { projectId: '2' } },
    ]);
  });
});
