import fs from 'node:fs';
import * as mockHelpers from './__helpers__/fixtures';

jest.mock('./log');

describe('activityLinker', () => {
  it('should work for RN 0.77', () => {
    jest.mock('./path', () => {
      const mainActivityPath = mockHelpers.prepareFixtureDuplicate77({
        rnVersion: '77',
        userFixtureFileName: 'MainActivity.kt.template',
        patchedFixtureFileName: 'rnn-tests_MainActivity.kt',
      });

      return {
        mainActivityKotlin: mainActivityPath,
      };
    });

    const ActivityLinker = require('./activityLinker');
    const linker = new ActivityLinker();
    linker.link();

    const mainActivityContent = fs.readFileSync(linker.activityPath, 'utf8');
    expect(mainActivityContent).toMatchSnapshot();
  });
});
