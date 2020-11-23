jest.mock('shell-utils');
const exec = require('shell-utils').exec;

jest.mock('fs');
const fs = require('fs');

const documentation = require('./documentation');

describe('Documentation script', () => {
  afterEach(() => {
    jest.resetAllMocks();
  });

  it('should build new version', () => {
    const version = '1.0.0';
    fs.readFileSync.mockReturnValue(JSON.stringify([]))

    documentation.release('1.0.0');
    expect(exec.execSync).toHaveBeenCalledTimes(2);
    expect(exec.execSync).toHaveBeenCalledWith(`npm --prefix ${process.cwd()}/website run docusaurus docs:version ${version}`);
    expect(exec.execSync).toHaveBeenCalledWith(`git add website`);
  });

  it('should override version', () => {
    fs.readFileSync.mockReturnValue(JSON.stringify([
      "1.0.0",
      "2.0.0"
    ]))

    const version = '2.0.0';
    documentation.release(version);

    expect(exec.execSync).toHaveBeenCalledTimes(4);
    expect(exec.execSync).toHaveBeenCalledWith(`rm -rf ${docsPath()}/versioned_docs/version-${version}`);
    expect(exec.execSync).toHaveBeenCalledWith(`rm -f ${docsPath()}/versioned_sidebars/version-${version}-sidebars.json`);
    expect(exec.execSync).toHaveBeenCalledWith(`npm --prefix ${docsPath()} run docusaurus docs:version ${version}`);
    expect(exec.execSync).toHaveBeenCalledWith(`git add website`);
  });
});

function docsPath() {
  return `${process.cwd()}/website`;
}
