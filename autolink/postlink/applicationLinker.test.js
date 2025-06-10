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
    beforeEach(() => { });

    it('should work for RN 0.77', () => {
        jest.mock('../postlink/path', () => {
            const { copyFileSync } = require('fs');
            const { tmpdir } = require('os');
            const path = require('path');

            const tmpMainApplicationPath = path.resolve(tmpdir(), 'rnn-tests_MainApplication.kt');

            copyFileSync(
                path.resolve('autolink/fixtures/rn77/MainApplication.kt.template'),
                tmpMainApplicationPath
            );

            return {
                mainApplicationKotlin: tmpMainApplicationPath,
            };
        });

        const ApplicationLinker = require('./applicationLinker');
        const linker = new ApplicationLinker();

        linker.link();
        const mainApplicationContent = fs.readFileSync(linker.applicationPath, 'utf8');
        expect(mainApplicationContent).toMatchSnapshot();
    });
}); 