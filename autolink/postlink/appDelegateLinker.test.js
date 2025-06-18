import fs from 'node:fs';
import * as mockHelpers from './__helpers__/fixtures';

jest.mock('./log');

describe('appDelegateLinker', () => {
  it('should work for RN 0.77 with Objective-C', () => {
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

  it('should work for RN 0.77 with Swift', () => {
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
});
