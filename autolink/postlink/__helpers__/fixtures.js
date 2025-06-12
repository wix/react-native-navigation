const prepareFixtureDuplicate = ({ rnVersion, userFixtureFileName, patchedFixtureFileName }) => {
  const fs = require('node:fs');
  const path = require('node:path');
  const os = require('node:os');

  const userFixtureRelPath = _getRelativeFixturePath(rnVersion, userFixtureFileName);

  const userFixturePath = path.resolve(userFixtureRelPath);
  const patchedFixturePath = path.resolve(os.tmpdir(), patchedFixtureFileName);
  fs.copyFileSync(userFixturePath, patchedFixturePath);

  return patchedFixturePath;
};

const _getRelativeFixturePath = (rnVersion, fixtureFileName) => {
  const path = require('node:path');
  return path.join('autolink', 'fixtures', `rn${rnVersion}`, fixtureFileName);
};

module.exports = {
  prepareFixtureDuplicate,
  prepareFixtureDuplicate77: ({ userFixtureFileName, patchedFixtureFileName }) =>
    prepareFixtureDuplicate({ rnVersion: '77', userFixtureFileName, patchedFixtureFileName }),
};
