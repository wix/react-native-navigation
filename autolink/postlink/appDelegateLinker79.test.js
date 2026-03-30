import fs from 'node:fs';
import * as mockHelpers from './__helpers__/fixtures';

jest.mock('./log');

const { getReactNativeVersion } = require('./__helpers__/reactNativeVersion');
const rnVersion = getReactNativeVersion();
const shouldSkip = !rnVersion || rnVersion.minor < 79;

// Conditionally skip entire test suite based on RN version
(shouldSkip ? describe.skip : describe)('appDelegateLinker', () => {
    it('should work with Swift bridgeless RN 0.79', () => {
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

