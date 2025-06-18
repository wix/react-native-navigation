import fs from 'node:fs';
import * as mockHelpers from './__helpers__/fixtures';

jest.mock('./log');

describe('applicationLinker', () => {
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
