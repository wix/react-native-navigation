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

describe('activityLinker', () => {
  beforeEach(() => {});

  it('should work for RN 0.68', () => {
    jest.mock('../postlink/path', () => {
      const { copyFileSync } = require('fs');
      const { tmpdir } = require('os');
      const path = require('path');

      const tmpMainActivityPath = path.resolve(tmpdir(), 'rnn-tests_MainActivity.java');

      copyFileSync(
        path.resolve('autolink/fixtures/rn68/MainActivity.java.template'),
        tmpMainActivityPath
      );

      return {
        mainActivityJava: tmpMainActivityPath,
      };
    });

    const ActivityLinker = require('./activityLinker');
    const linker = new ActivityLinker();

    linker.link();
    const mainActivityContent = fs.readFileSync(linker.activityPath, 'utf8');
    expect(mainActivityContent).toMatchSnapshot();
  });

  it('should work for RN 0.69', () => {
    jest.mock('../postlink/path', () => {
      const { copyFileSync } = require('fs');
      const { tmpdir } = require('os');
      const path = require('path');

      const tmpMainActivityPath = path.resolve(tmpdir(), 'rnn-tests_MainActivity.java');

      copyFileSync(
        path.resolve('autolink/fixtures/rn69/MainActivity.java.template'),
        tmpMainActivityPath
      );

      return {
        mainActivityJava: tmpMainActivityPath,
      };
    });

    const ActivityLinker = require('./activityLinker');
    const linker = new ActivityLinker();

    linker.link();
    const mainActivityContent = fs.readFileSync(linker.activityPath, 'utf8');
    expect(mainActivityContent).toMatchSnapshot();
  });
});
