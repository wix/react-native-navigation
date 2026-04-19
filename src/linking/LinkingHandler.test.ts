import { LinkingHandler, LinkingAPI } from './LinkingHandler';
import { LinkingConfig, RouteMatch } from './types';

describe('LinkingHandler', () => {
  let uut: LinkingHandler;
  let mockSetRoot: jest.Mock;
  let mockLinking: LinkingAPI;

  const baseConfig: LinkingConfig = {
    prefixes: ['myapp://', 'https://myapp.com'],
    config: {
      screens: {
        Home: 'home',
        Profile: 'user/:id',
        Settings: {
          path: 'settings',
          screens: {
            Notifications: 'notifications',
          },
        },
      },
    },
  };

  beforeEach(() => {
    mockSetRoot = jest.fn().mockResolvedValue('rootId');
    mockLinking = {
      addEventListener: jest.fn(() => ({ remove: jest.fn() })),
      getInitialURL: jest.fn(() => Promise.resolve(null)),
    };
    uut = new LinkingHandler(mockSetRoot, mockLinking);
  });

  afterEach(() => {
    uut.teardown();
  });

  describe('resolve', () => {
    it('resolves a simple URL to a route match', () => {
      uut.configure(baseConfig);
      const result = uut.resolve('myapp://home');
      expect(result).toEqual({
        url: 'myapp://home',
        path: [{ screen: 'Home', params: {} }],
        queryParams: {},
      });
    });

    it('resolves URL with path params', () => {
      uut.configure(baseConfig);
      const result = uut.resolve('myapp://user/42');
      expect(result).toEqual({
        url: 'myapp://user/42',
        path: [{ screen: 'Profile', params: { id: '42' } }],
        queryParams: {},
      });
    });

    it('resolves nested route', () => {
      uut.configure(baseConfig);
      const result = uut.resolve('myapp://settings/notifications');
      expect(result).toEqual({
        url: 'myapp://settings/notifications',
        path: [
          { screen: 'Settings', params: {} },
          { screen: 'Notifications', params: {} },
        ],
        queryParams: {},
      });
    });

    it('resolves URL with query params', () => {
      uut.configure(baseConfig);
      const result = uut.resolve('myapp://home?ref=push');
      expect(result).toEqual({
        url: 'myapp://home?ref=push',
        path: [{ screen: 'Home', params: {} }],
        queryParams: { ref: 'push' },
      });
    });

    it('returns null for unmatched URL', () => {
      uut.configure(baseConfig);
      expect(uut.resolve('myapp://unknown')).toBeNull();
    });

    it('returns null for unmatched prefix', () => {
      uut.configure(baseConfig);
      expect(uut.resolve('other://home')).toBeNull();
    });

    it('returns null when not configured', () => {
      expect(uut.resolve('myapp://home')).toBeNull();
    });
  });

  describe('handleURL with getLayout', () => {
    it('calls setRoot with layout from getLayout', () => {
      const layout = { root: { component: { name: 'Home' } } };
      const config: LinkingConfig = {
        ...baseConfig,
        getLayout: jest.fn().mockReturnValue(layout),
      };
      uut.configure(config);
      uut.handleURL('myapp://home');

      expect(config.getLayout).toHaveBeenCalledWith({
        url: 'myapp://home',
        path: [{ screen: 'Home', params: {} }],
        queryParams: {},
      });
      expect(mockSetRoot).toHaveBeenCalledWith(layout);
    });

    it('does not call setRoot if getLayout returns undefined', () => {
      const config: LinkingConfig = {
        ...baseConfig,
        getLayout: jest.fn().mockReturnValue(undefined),
      };
      uut.configure(config);
      uut.handleURL('myapp://home');

      expect(mockSetRoot).not.toHaveBeenCalled();
    });
  });

  describe('handleURL with onLink', () => {
    it('calls onLink instead of getLayout when provided', () => {
      const onLink = jest.fn();
      const getLayout = jest.fn();
      const config: LinkingConfig = {
        ...baseConfig,
        onLink,
        getLayout,
      };
      uut.configure(config);
      uut.handleURL('myapp://user/5');

      expect(onLink).toHaveBeenCalledWith({
        url: 'myapp://user/5',
        path: [{ screen: 'Profile', params: { id: '5' } }],
        queryParams: {},
      });
      expect(getLayout).not.toHaveBeenCalled();
      expect(mockSetRoot).not.toHaveBeenCalled();
    });
  });

  describe('deferred links', () => {
    it('queues link when isReady returns false', () => {
      let ready = false;
      const getLayout = jest.fn().mockReturnValue({ root: { component: { name: 'Home' } } });
      const config: LinkingConfig = {
        ...baseConfig,
        getLayout,
        isReady: () => ready,
      };
      uut.configure(config);

      uut.handleURL('myapp://home');
      expect(getLayout).not.toHaveBeenCalled();

      ready = true;
      uut.setReady(true);
      expect(getLayout).toHaveBeenCalledTimes(1);
    });

    it('processes link immediately when isReady returns true', () => {
      const getLayout = jest.fn().mockReturnValue({ root: { component: { name: 'Home' } } });
      const config: LinkingConfig = {
        ...baseConfig,
        getLayout,
        isReady: () => true,
      };
      uut.configure(config);

      uut.handleURL('myapp://home');
      expect(getLayout).toHaveBeenCalledTimes(1);
    });

    it('flushes multiple queued links in order', () => {
      let ready = false;
      const handled: string[] = [];
      const config: LinkingConfig = {
        ...baseConfig,
        onLink: (match: RouteMatch) => handled.push(match.url),
        isReady: () => ready,
      };
      uut.configure(config);

      uut.handleURL('myapp://home');
      uut.handleURL('myapp://user/1');
      expect(handled).toEqual([]);

      ready = true;
      uut.setReady(true);
      expect(handled).toEqual(['myapp://home', 'myapp://user/1']);
    });
  });

  describe('subscribe', () => {
    it('subscribes to Linking url events on configure', () => {
      uut.configure(baseConfig);
      expect(mockLinking.addEventListener).toHaveBeenCalledTimes(1);
      expect((mockLinking.addEventListener as jest.Mock).mock.calls[0][0]).toBe('url');
    });

    it('checks initial URL on configure', () => {
      uut.configure(baseConfig);
      expect(mockLinking.getInitialURL).toHaveBeenCalled();
    });

    it('handles initial URL if present', async () => {
      const layout = { root: { component: { name: 'Home' } } };
      (mockLinking.getInitialURL as jest.Mock).mockResolvedValue('myapp://home');

      uut.configure({ ...baseConfig, getLayout: () => layout });

      await Promise.resolve();
      expect(mockSetRoot).toHaveBeenCalledWith(layout);
    });
  });

  describe('teardown', () => {
    it('clears config and queue', () => {
      uut.configure(baseConfig);
      uut.teardown();
      expect(uut.resolve('myapp://home')).toBeNull();
    });
  });

  describe('configure replaces previous config', () => {
    it('uses new config after reconfigure', () => {
      uut.configure(baseConfig);
      expect(uut.resolve('myapp://home')).not.toBeNull();

      const newConfig: LinkingConfig = {
        prefixes: ['other://'],
        config: { screens: { Dashboard: 'dash' } },
      };
      uut.configure(newConfig);

      expect(uut.resolve('myapp://home')).toBeNull();
      expect(uut.resolve('other://dash')).toEqual({
        url: 'other://dash',
        path: [{ screen: 'Dashboard', params: {} }],
        queryParams: {},
      });
    });
  });
});
