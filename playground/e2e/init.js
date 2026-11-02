require('detox-testing-library-rnn-adapter').extendDetox();

// DETOX_DUO_POSE=closed|open runs a whole suite on the iPhone Duo's outer or inner display.
if (process.env.DETOX_DUO_POSE) {
  const duo = require('./duo');
  beforeAll(() => duo.setPose(process.env.DETOX_DUO_POSE), 60000);
}
