jest.mock('./../adapters/NativeCommandsSender', () => require('./mocks/NativeCommandsSender.tsx'));
jest.mock('./../adapters/NativeEventsReceiver', () => require('./mocks/NativeEventsReceiver.ts'));

export const Application = require('./Application').Application;
