const RESET_MODULE_EXCEPTIONS = ['react', 'react-redux'];
const shonoActualRegistryCache = {};
RESET_MODULE_EXCEPTIONS.forEach((moduleName) => {
  jest.doMock(
    moduleName,
    () => {
      if (!shonoActualRegistryCache[moduleName]) {
        shonoActualRegistryCache[moduleName] = jest.requireActual(moduleName);
      }
      return shonoActualRegistryCache[moduleName];
    },
    { virtual: true }
  );
});

const { mockDetox } = require('detox-testing-library-rnn-adapter');

jest.mock('react-native-gesture-handler', () => {
  return {
    gestureHandlerRootHOC: jest.fn(),
  };
});


mockDetox(() => require('./playground/index'));

// detox-testing-library-rnn-adapter sets global.element to identity; real Detox chains .atIndex().
const origElement = global.element;
global.element = (matcher) => {
  const el = origElement(matcher);
  if (el == null || typeof el.atIndex === 'function') {
    return el;
  }
  return new Proxy(el, {
    get(target, prop, receiver) {
      if (prop === 'atIndex') {
        return function atIndex() {
          return target;
        };
      }
      return Reflect.get(target, prop, receiver);
    },
  });
};

beforeEach(() => {
  const { mockNativeComponents } = require('react-native-navigation/Mock');
  mockNativeComponents();
  mockUILib();
});

setImmediate = (callback) => callback();

const mockUILib = () => {
  const NativeModules = require('react-native').NativeModules;
  NativeModules.KeyboardTrackingViewTempManager = {};
  NativeModules.StatusBarManager = {
    getHeight: () => 40,
  };
};
