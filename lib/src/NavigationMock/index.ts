jest.mock('./../adapters/NativeCommandsSender', () => require('./NativeCommandsSender.tsx'));
jest.mock('./../adapters/NativeEventsReceiver', () => require('./NativeEventsReceiver.ts'));

export const Application = require('./Application').Application;
