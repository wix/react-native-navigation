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

describe('gradleLinker', () => {
    beforeEach(() => { });

    it('should work for RN 0.77', () => {
        jest.mock('../postlink/path', () => {
            const { copyFileSync } = require('fs');
            const { tmpdir } = require('os');
            const path = require('path');

            const tmpBuildGradlePath = path.resolve(tmpdir(), 'rnn-tests_build.gradle');

            copyFileSync(
                path.resolve('autolink/fixtures/rn77/build.gradle.template'),
                tmpBuildGradlePath
            );

            return {
                rootGradle: tmpBuildGradlePath,
            };
        });

        const GradleLinker = require('./gradleLinker');
        const linker = new GradleLinker();

        linker.link();
        const buildGradleContent = fs.readFileSync(linker.gradlePath, 'utf8');
        expect(buildGradleContent).toMatchSnapshot();
    });
}); 