import fs from 'fs';

/**
 * Mocks
 */

jest.mock('../postlink/log', () => ({
  log: console.log,
  logn: console.log,
  warn: console.log,
  warnn: console.log,
  info: console.log,
  infon: console.log,
  debug: console.log,
  debugn: console.log,
  errorn: console.log,
}));

/**
 * Tests
 */

describe('applicationLinker', () => {
  it('should work for RN 0.72', () => {
    jest.mock('../postlink/path', () => {
      const { copyFileSync } = require('fs');
      const { tmpdir } = require('os');
      const path = require('path');

      const tmpApplicationJavaPath = path.resolve(tmpdir(), 'rnn-tests_MainApplication.java');

      copyFileSync(
        path.resolve('autolink/fixtures/rn72/MainApplication.java.template'),
        tmpApplicationJavaPath
      );

      return {
        mainApplicationJava: tmpApplicationJavaPath,
      };
    });

    const ApplicationLinker = require('./applicationLinker');
    const linker = new ApplicationLinker();

    linker.link();
    const mainActivityContent = fs.readFileSync(linker.applicationPath, 'utf8');
    expect(mainActivityContent).toMatchSnapshot();
  });

  it('should work for RN 0.73', () => {
    jest.mock('../postlink/path', () => {
      const { copyFileSync } = require('fs');
      const { tmpdir } = require('os');
      const path = require('path');

      const tmpApplicationJavaPath = path.resolve(tmpdir(), 'rnn-tests_MainApplication.kt');

      copyFileSync(
        path.resolve('autolink/fixtures/rn73/MainApplication.kt.template'),
        tmpApplicationJavaPath
      );

      return {
        mainApplicationJava: tmpApplicationJavaPath,
      };
    });

    const ApplicationLinker = require('./applicationLinker');
    const linker = new ApplicationLinker();

    linker.link();
    const mainActivityContent = fs.readFileSync(linker.applicationPath, 'utf8');
    expect(mainActivityContent).toMatchSnapshot();
  });
});
