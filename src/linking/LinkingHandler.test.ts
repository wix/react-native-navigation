import { LinkingHandler, LinkingAPI } from './LinkingHandler';
import { LinkingConfig, RouteMatch } from './types';
import { Layout } from '../interfaces/Layout';

describe('LinkingHandler', () => {
  let uut: LinkingHandler;
  let mockShowModal: jest.Mock<Promise<string>, [Layout]>;
  let mockLinking: jest.Mocked<LinkingAPI>;
  let urlListener: ((event: { url: string }) => void) | null;

  const baseConfig: LinkingConfig = {
    prefixes: ['myapp://', 'https://myapp.com'],
    config: {
      screens: {
        Home: 'home',
        Profile: 'user/:id',
        Settings: {
          path: 'settings',
          screens: { Notifications: 'notifications' },
        },
      },
    },
  };

  beforeEach(() => {
    mockShowModal = jest.fn().mockResolvedValue('modalId');
    urlListener = null;
    mockLinking = {
      addEventListener: jest.fn((_type, handler) => {
        urlListener = handler;
        return { remove: jest.fn() };
      }),
      getInitialURL: jest.fn().mockResolvedValue(null),
    } as unknown as jest.Mocked<LinkingAPI>;
    uut = new LinkingHandler(mockShowModal, mockLinking);
  });

  afterEach(() => {
    uut.teardown();
  });

  describe('resolve', () => {
    beforeEach(() => uut.configure(baseConfig));

    it('resolves a simple URL to a route match', () => {
      expect(uut.resolve('myapp://home')).toEqual({
        url: 'myapp://home',
        path: [{ screen: 'Home', params: {} }],
        queryParams: {},
      });
    });

    it('resolves a URL with path params', () => {
      expect(uut.resolve('myapp://user/42')).toEqual({
        url: 'myapp://user/42',
        path: [{ screen: 'Profile', params: { id: '42' } }],
        queryParams: {},
      });
    });

    it('resolves a nested route as a chain', () => {
      const match = uut.resolve('myapp://settings/notifications');
      expect(match?.path).toEqual([
        { screen: 'Settings', params: {} },
        { screen: 'Notifications', params: {} },
      ]);
    });

    it('returns null when the prefix does not match', () => {
      expect(uut.resolve('other://home')).toBeNull();
    });

    it('returns null when no route matches', () => {
      expect(uut.resolve('myapp://unknown')).toBeNull();
    });

    it('returns null before configure() is called', () => {
      const fresh = new LinkingHandler(mockShowModal, mockLinking);
      expect(fresh.resolve('myapp://home')).toBeNull();
    });
  });

  describe('default modal presentation', () => {
    it('defers links until rootReady is signalled, then auto-flushes', () => {
      uut.configure(baseConfig);
      uut.handleURL('myapp://home');
      expect(mockShowModal).not.toHaveBeenCalled();
      uut.setRootReady();
      expect(mockShowModal).toHaveBeenCalledTimes(1);
      expect(mockShowModal).toHaveBeenCalledWith({
        stack: {
          children: [{ component: { name: 'Home', passProps: {} } }],
        },
      });
    });

    it('processes links immediately once rootReady', () => {
      uut.configure(baseConfig);
      uut.setRootReady();
      uut.handleURL('myapp://user/42');
      expect(mockShowModal).toHaveBeenCalledWith({
        stack: {
          children: [{ component: { name: 'Profile', passProps: { id: '42' } } }],
        },
      });
    });

    it('builds a push chain inside the modal stack for nested matches', () => {
      uut.configure(baseConfig);
      uut.setRootReady();
      uut.handleURL('myapp://settings/notifications');
      expect(mockShowModal).toHaveBeenCalledWith({
        stack: {
          children: [
            { component: { name: 'Settings', passProps: {} } },
            { component: { name: 'Notifications', passProps: {} } },
          ],
        },
      });
    });

    it('merges query params into passProps', () => {
      uut.configure(baseConfig);
      uut.setRootReady();
      uut.handleURL('myapp://user/42?source=push');
      expect(mockShowModal).toHaveBeenCalledWith({
        stack: {
          children: [
            {
              component: {
                name: 'Profile',
                passProps: { id: '42', source: 'push' },
              },
            },
          ],
        },
      });
    });

    it('filters React-reserved query params from passProps', () => {
      uut.configure(baseConfig);
      uut.setRootReady();
      uut.handleURL('myapp://user/42?ref=push&key=abc&utm=foo');
      expect(mockShowModal).toHaveBeenCalledWith({
        stack: {
          children: [
            {
              component: {
                name: 'Profile',
                passProps: { id: '42', utm: 'foo' },
              },
            },
          ],
        },
      });
    });
  });

  describe('getModal override', () => {
    it('uses the supplied builder instead of the default', () => {
      const customLayout: Layout = {
        component: { name: 'Custom' },
      };
      const getModal = jest.fn().mockReturnValue(customLayout);
      uut.configure({ ...baseConfig, getModal });
      uut.setRootReady();
      uut.handleURL('myapp://home');
      expect(getModal).toHaveBeenCalledTimes(1);
      const passedMatch = getModal.mock.calls[0][0];
      expect(passedMatch.path).toEqual([{ screen: 'Home', params: {} }]);
      expect(mockShowModal).toHaveBeenCalledWith(customLayout);
    });

    it('skips presentation when getModal returns undefined', () => {
      const getModal = jest.fn().mockReturnValue(undefined);
      uut.configure({ ...baseConfig, getModal });
      uut.setRootReady();
      uut.handleURL('myapp://home');
      expect(getModal).toHaveBeenCalled();
      expect(mockShowModal).not.toHaveBeenCalled();
    });
  });

  describe('onLink override', () => {
    it('calls onLink and bypasses showModal entirely', () => {
      const onLink = jest.fn();
      uut.configure({ ...baseConfig, onLink });
      uut.setRootReady();
      uut.handleURL('myapp://user/42');
      expect(onLink).toHaveBeenCalledTimes(1);
      const passedMatch = onLink.mock.calls[0][0];
      expect(passedMatch.path).toEqual([{ screen: 'Profile', params: { id: '42' } }]);
      expect(mockShowModal).not.toHaveBeenCalled();
    });

    it('takes precedence over getModal', () => {
      const onLink = jest.fn();
      const getModal = jest.fn();
      uut.configure({ ...baseConfig, onLink, getModal });
      uut.setRootReady();
      uut.handleURL('myapp://home');
      expect(onLink).toHaveBeenCalled();
      expect(getModal).not.toHaveBeenCalled();
    });
  });

  describe('fallback', () => {
    it('is called when no prefix matches', () => {
      const fallback = jest.fn();
      uut.configure({ ...baseConfig, fallback });
      uut.setRootReady();
      uut.handleURL('other://home');
      expect(fallback).toHaveBeenCalledWith('other://home');
      expect(mockShowModal).not.toHaveBeenCalled();
    });

    it('is called when no route matches', () => {
      const fallback = jest.fn();
      uut.configure({ ...baseConfig, fallback });
      uut.setRootReady();
      uut.handleURL('myapp://unknown');
      expect(fallback).toHaveBeenCalledWith('myapp://unknown');
      expect(mockShowModal).not.toHaveBeenCalled();
    });

    it('is silent when not supplied', () => {
      uut.configure(baseConfig);
      uut.setRootReady();
      expect(() => uut.handleURL('myapp://unknown')).not.toThrow();
      expect(mockShowModal).not.toHaveBeenCalled();
    });
  });

  describe('isReady predicate', () => {
    it('queues links while isReady returns false', () => {
      let ready = false;
      uut.configure({ ...baseConfig, isReady: () => ready });
      uut.setRootReady();
      uut.handleURL('myapp://home');
      expect(mockShowModal).not.toHaveBeenCalled();
      ready = true;
      uut.setLinkingReady(true);
      expect(mockShowModal).toHaveBeenCalledTimes(1);
    });

    it('setLinkingReady(true) overrides a false isReady', () => {
      uut.configure({ ...baseConfig, isReady: () => false });
      uut.setRootReady();
      uut.setLinkingReady(true);
      uut.handleURL('myapp://home');
      expect(mockShowModal).toHaveBeenCalled();
    });

    it('setLinkingReady(false) blocks even when isReady is absent', () => {
      uut.configure(baseConfig);
      uut.setRootReady();
      uut.setLinkingReady(false);
      uut.handleURL('myapp://home');
      expect(mockShowModal).not.toHaveBeenCalled();
    });
  });

  describe('Linking subscription', () => {
    it('dispatches URL events through the pipeline', () => {
      uut.configure(baseConfig);
      uut.setRootReady();
      urlListener?.({ url: 'myapp://home' });
      expect(mockShowModal).toHaveBeenCalled();
    });

    it('reads the initial URL on configure', async () => {
      mockLinking.getInitialURL.mockResolvedValueOnce('myapp://home');
      uut.configure(baseConfig);
      uut.setRootReady();
      await Promise.resolve();
      await Promise.resolve();
      expect(mockShowModal).toHaveBeenCalled();
    });

    it('removes the listener on teardown', () => {
      const remove = jest.fn();
      (mockLinking.addEventListener as jest.Mock).mockReturnValueOnce({ remove });
      uut.configure(baseConfig);
      uut.teardown();
      expect(remove).toHaveBeenCalled();
    });

    it('reconfiguring tears down the previous subscription', () => {
      const remove = jest.fn();
      (mockLinking.addEventListener as jest.Mock).mockReturnValueOnce({ remove });
      uut.configure(baseConfig);
      uut.configure(baseConfig);
      expect(remove).toHaveBeenCalled();
    });
  });

  describe('setRootReady', () => {
    it('is idempotent', () => {
      uut.configure(baseConfig);
      uut.handleURL('myapp://home');
      uut.setRootReady();
      uut.setRootReady();
      expect(mockShowModal).toHaveBeenCalledTimes(1);
    });

    it('persists across reconfigure', () => {
      uut.setRootReady();
      uut.configure(baseConfig);
      uut.handleURL('myapp://home');
      expect(mockShowModal).toHaveBeenCalled();
    });
  });

  describe('match payload', () => {
    it('passes the full match object to onLink', () => {
      let captured: RouteMatch | null = null;
      uut.configure({
        ...baseConfig,
        onLink: (match) => {
          captured = match;
        },
      });
      uut.setRootReady();
      uut.handleURL('myapp://user/42?source=push');
      expect(captured).toEqual({
        url: 'myapp://user/42?source=push',
        path: [{ screen: 'Profile', params: { id: '42' } }],
        queryParams: { source: 'push' },
      });
    });
  });
});
