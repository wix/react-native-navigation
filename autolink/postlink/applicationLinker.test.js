import fs from 'node:fs';
import * as mockHelpers from './__helpers__/fixtures';

jest.mock('./log');

describe('applicationLinker', () => {
    it('links the RN 0.87 ReactHost template without introducing a legacy host', () => {
        jest.mock('./path', () => ({}));
        const ApplicationLinker = require('./applicationLinker');
        const linker = new ApplicationLinker();
        linker.applicationPath = mockHelpers.prepareFixtureDuplicate({
            rnVersion: '87',
            userFixtureFileName: 'MainApplication.kt.template',
            patchedFixtureFileName: 'rnn-tests_MainApplication87.kt',
        });
        linker.link();
        const content = fs.readFileSync(linker.applicationPath, 'utf8');
        expect(content).toContain('class MainApplication : NavigationApplication()');
        expect(content).toContain('override val reactHost: ReactHost by lazy');
        expect(content).toContain('PackageList(this).packages.apply');
        expect(content).not.toContain('loadReactNative');
        expect(content).not.toContain('reactNativeHost');
        expect(linker.navigationHostSuccess).toBe(true);
        linker.link();
        expect(fs.readFileSync(linker.applicationPath, 'utf8')).toBe(content);
    });

    it('should work for RN 0.77', () => {
        jest.mock('./path', () => {
            const mainApplicationPath = mockHelpers.prepareFixtureDuplicate77({
                userFixtureFileName: 'MainApplication.kt.template',
                patchedFixtureFileName: 'rnn-tests_MainApplication.kt',
            });
            return {
                mainApplicationKotlin: mainApplicationPath,
            };
        });

        const ApplicationLinker = require('./applicationLinker');
        const linker = new ApplicationLinker();
        linker.link();

        const mainApplicationContent = fs.readFileSync(linker.applicationPath, 'utf8');
        expect(mainApplicationContent).toMatchSnapshot();
    });
});
