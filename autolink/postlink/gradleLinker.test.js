import fs from 'fs';
import * as mockHelpers from './__helpers__/fixtures';

jest.mock('./log');

describe('gradleLinker', () => {
    it('should work for RN 0.77', () => {
        jest.mock('./path', () => {
            const tmpBuildGradlePath = mockHelpers.prepareFixtureDuplicate77({
               userFixtureFileName: 'build.gradle.template',
               patchedFixtureFileName: 'rnn-tests_build.gradle',
            });

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
