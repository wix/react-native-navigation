jest.mock('./../adapters/NativeCommandsSender', () => require('./mocks/NativeCommandsSender'));
jest.mock('./../adapters/NativeEventsReceiver', () => require('./mocks/NativeEventsReceiver'));

export const Application = require('./Application').Application;
