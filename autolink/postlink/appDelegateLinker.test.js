import fs from 'node:fs';
import * as mockHelpers from './__helpers__/fixtures';

jest.mock('./log');

describe('appDelegateLinker', () => {
  it('should work for RN 0.77 & 0.78 with Objective-C', () => {
    const { getReactNativeVersion } = require('./__helpers__/reactNativeVersion');
    const rnVersion = getReactNativeVersion();

    if (rnVersion && rnVersion.minor >= 79) {
      console.log(`Skipping RN 0.77 test (current version: ${rnVersion.raw})`);
      return;
    }

    jest.mock('./path', () => {
      const appDelegatePath = mockHelpers.prepareFixtureDuplicate77({
        userFixtureFileName: 'AppDelegate.mm.template',
        patchedFixtureFileName: 'rnn-tests_AppDelegate.mm',
      });
      return {
        appDelegate: appDelegatePath,
      };
    });

    const AppDelegateLinker = require('./appDelegateLinker');
    const linker = new AppDelegateLinker();

    linker.link();
    const appDelegateContent = fs.readFileSync(linker.appDelegatePath, 'utf8');
    expect(appDelegateContent).toMatchSnapshot();
  });

  it('should work for RN 0.77 & 0.78 with Swift', () => {
    const { getReactNativeVersion } = require('./__helpers__/reactNativeVersion');
    const rnVersion = getReactNativeVersion();

    if (rnVersion && rnVersion.minor >= 79) {
      console.log(`Skipping RN 0.77 test (current version: ${rnVersion.raw})`);
      return;
    }

    jest.mock('./path', () => {
      const tmpAppDelegatePath = mockHelpers.prepareFixtureDuplicate77({
        userFixtureFileName: 'AppDelegate.swift.template',
        patchedFixtureFileName: 'rnn-tests_AppDelegate.swift',
      });

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

  it('should work with Swift bridgeless RN 0.79', () => {
    const { getReactNativeVersion } = require('./__helpers__/reactNativeVersion');
    const rnVersion = getReactNativeVersion();

    if (!rnVersion || rnVersion.minor < 79) {
      console.log(`Skipping RN 0.79 test (current version: ${rnVersion?.raw || 'unknown'})`);
      return;
    }

    jest.mock('./path', () => {
      const tmpAppDelegatePath = mockHelpers.prepareFixtureDuplicate79({
        userFixtureFileName: 'AppDelegate.swift.template',
        patchedFixtureFileName: 'rnn-tests_AppDelegate79.swift',
      });

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
