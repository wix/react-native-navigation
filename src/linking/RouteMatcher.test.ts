import { RouteMatcher } from './RouteMatcher';
import { ScreensConfig } from './types';

describe('RouteMatcher', () => {
  let uut: RouteMatcher;

  beforeEach(() => {
    uut = new RouteMatcher();
  });

  describe('buildRouteTree', () => {
    it('builds nodes from simple string configs', () => {
      const screens: ScreensConfig = {
        Home: 'home',
        Profile: 'user/:id',
      };
      const tree = uut.buildRouteTree(screens);
      expect(tree).toHaveLength(2);
      expect(tree[0]).toEqual({
        screen: 'Home',
        pattern: 'home',
        segments: ['home'],
        children: [],
      });
      expect(tree[1]).toEqual({
        screen: 'Profile',
        pattern: 'user/:id',
        segments: ['user', ':id'],
        children: [],
      });
    });

    it('builds nested route tree', () => {
      const screens: ScreensConfig = {
        Settings: {
          path: 'settings',
          screens: {
            Notifications: 'notifications',
            Privacy: 'privacy',
          },
        },
      };
      const tree = uut.buildRouteTree(screens);
      expect(tree).toHaveLength(1);
      expect(tree[0].screen).toBe('Settings');
      expect(tree[0].children).toHaveLength(2);
      expect(tree[0].children[0].screen).toBe('Notifications');
      expect(tree[0].children[1].screen).toBe('Privacy');
    });

    it('handles grouping nodes with no path', () => {
      const screens: ScreensConfig = {
        Main: {
          screens: {
            Feed: 'feed',
          },
        },
      };
      const tree = uut.buildRouteTree(screens);
      expect(tree[0].pattern).toBeNull();
      expect(tree[0].segments).toEqual([]);
      expect(tree[0].children[0].screen).toBe('Feed');
    });
  });

  describe('match', () => {
    it('matches a simple path', () => {
      const screens: ScreensConfig = {
        Home: 'home',
        Profile: 'user/:id',
      };
      uut.setRouteTree(uut.buildRouteTree(screens));

      const result = uut.match('home', 'myapp://home', {});
      expect(result).toEqual({
        url: 'myapp://home',
        path: [{ screen: 'Home', params: {} }],
        queryParams: {},
      });
    });

    it('extracts path parameters', () => {
      const screens: ScreensConfig = {
        Profile: 'user/:id',
      };
      uut.setRouteTree(uut.buildRouteTree(screens));

      const result = uut.match('user/42', 'myapp://user/42', {});
      expect(result).toEqual({
        url: 'myapp://user/42',
        path: [{ screen: 'Profile', params: { id: '42' } }],
        queryParams: {},
      });
    });

    it('matches nested routes', () => {
      const screens: ScreensConfig = {
        Settings: {
          path: 'settings',
          screens: {
            Notifications: 'notifications',
          },
        },
      };
      uut.setRouteTree(uut.buildRouteTree(screens));

      const result = uut.match(
        'settings/notifications',
        'myapp://settings/notifications',
        {}
      );
      expect(result).toEqual({
        url: 'myapp://settings/notifications',
        path: [
          { screen: 'Settings', params: {} },
          { screen: 'Notifications', params: {} },
        ],
        queryParams: {},
      });
    });

    it('matches through grouping nodes (no path)', () => {
      const screens: ScreensConfig = {
        Main: {
          screens: {
            Feed: 'feed',
          },
        },
      };
      uut.setRouteTree(uut.buildRouteTree(screens));

      const result = uut.match('feed', 'myapp://feed', {});
      expect(result).toEqual({
        url: 'myapp://feed',
        path: [
          { screen: 'Main', params: {} },
          { screen: 'Feed', params: {} },
        ],
        queryParams: {},
      });
    });

    it('returns null for unmatched path', () => {
      const screens: ScreensConfig = { Home: 'home' };
      uut.setRouteTree(uut.buildRouteTree(screens));

      const result = uut.match('unknown', 'myapp://unknown', {});
      expect(result).toBeNull();
    });

    it('returns null when route tree is not set', () => {
      const result = uut.match('home', 'myapp://home', {});
      expect(result).toBeNull();
    });

    it('passes through query params', () => {
      const screens: ScreensConfig = { Search: 'search' };
      uut.setRouteTree(uut.buildRouteTree(screens));

      const result = uut.match('search', 'myapp://search?q=test', { q: 'test' });
      expect(result).toEqual({
        url: 'myapp://search?q=test',
        path: [{ screen: 'Search', params: {} }],
        queryParams: { q: 'test' },
      });
    });

    it('matches multi-segment patterns with params', () => {
      const screens: ScreensConfig = {
        Post: 'user/:userId/post/:postId',
      };
      uut.setRouteTree(uut.buildRouteTree(screens));

      const result = uut.match('user/5/post/99', 'myapp://user/5/post/99', {});
      expect(result).toEqual({
        url: 'myapp://user/5/post/99',
        path: [{ screen: 'Post', params: { userId: '5', postId: '99' } }],
        queryParams: {},
      });
    });

    it('does not match partial path', () => {
      const screens: ScreensConfig = {
        Profile: 'user/:id',
      };
      uut.setRouteTree(uut.buildRouteTree(screens));

      const result = uut.match('user/42/extra', 'myapp://user/42/extra', {});
      expect(result).toBeNull();
    });

    it('matches deeply nested routes with params', () => {
      const screens: ScreensConfig = {
        App: {
          path: 'app',
          screens: {
            User: {
              path: 'user/:id',
              screens: {
                Posts: 'posts',
              },
            },
          },
        },
      };
      uut.setRouteTree(uut.buildRouteTree(screens));

      const result = uut.match('app/user/7/posts', 'myapp://app/user/7/posts', {});
      expect(result).toEqual({
        url: 'myapp://app/user/7/posts',
        path: [
          { screen: 'App', params: {} },
          { screen: 'User', params: { id: '7' } },
          { screen: 'Posts', params: {} },
        ],
        queryParams: {},
      });
    });
  });
});
