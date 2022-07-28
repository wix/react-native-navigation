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

describe('appDelegateLinker', () => {
  beforeEach(() => {});

  it('should work for RN 0.68', () => {
    jest.mock('../postlink/path', () => {
      const { copyFileSync } = require('fs');
      const { tmpdir } = require('os');
      const path = require('path');

      const tmpAppDelegatePath = path.resolve(tmpdir(), 'rnn-tests_AppDelegate.mm');

      copyFileSync(
        path.resolve('autolink/fixtures/rn68/AppDelegate.mm.template'),
        tmpAppDelegatePath
      );

      return {
        appDelegate: tmpAppDelegatePath,
      };
    });

    const AppDelegateLinker = require('./appDelegateLinker');
    const linker = new AppDelegateLinker();

    linker.link();
    const appDelegateContent = fs.readFileSync(linker.appDelegatePath, 'utf8');
    expect(appDelegateContent).toMatchSnapshot();
  });

  it('should work for RN 0.69', () => {
    jest.mock('../postlink/path', () => {
      const { copyFileSync } = require('fs');
      const { tmpdir } = require('os');
      const path = require('path');

      const tmpAppDelegatePath = path.resolve(tmpdir(), 'rnn-tests_AppDelegate.mm');

      copyFileSync(
        path.resolve('autolink/fixtures/rn69/AppDelegate.mm.template'),
        tmpAppDelegatePath
      );

      return {
        appDelegate: tmpAppDelegatePath,
      };
    });

    const AppDelegateLinker = require('./appDelegateLinker');
    const linker = new AppDelegateLinker();

    linker.link();
    const appDelegateContent = fs.readFileSync(linker.appDelegatePath, 'utf8');
    expect(appDelegateContent).toMatchSnapshot();
  });
});
