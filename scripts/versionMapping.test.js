const {
  getGradleVersion,
  getCliVersion,
  getReanimatedOverride,
  getWorkletsOverride,
  getGestureHandlerOverride,
} = require('./versionMapping');

describe('React Native version mapping', () => {
  it('uses the RN 0.87 Android toolchain', () => {
    expect(getGradleVersion(87)).toBe('9.4.1');
    expect(getCliVersion(87)).toBe('20.1.0');
    expect(getReanimatedOverride(87)).toBe('4.6.0');
    expect(getWorkletsOverride(87)).toBe('0.12.1');
    expect(getGestureHandlerOverride(87)).toBe('3.2.1');
  });

  it('preserves the toolchains for older supported versions', () => {
    expect(getGradleVersion(85)).toBe('9.3.1');
    expect(getGradleVersion(84)).toBe('9.0.0');
    expect(getGradleVersion(77)).toBe('8.14.1');
    expect(getReanimatedOverride(85)).toBe('4.3.0');
    expect(getWorkletsOverride(85)).toBe('0.8.1');
    expect(getGestureHandlerOverride(85)).toBe('^2.29.1');
  });
});
