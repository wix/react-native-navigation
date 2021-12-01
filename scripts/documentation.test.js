jest.mock('shell-utils');
const exec = require('shell-utils').exec;

jest.mock('fs');
const fs = require('fs');

const documentation = require('./documentation');

describe('Documentation script', () => {
  beforeAll(() => {
    process.chdir = jest.fn();
  });

  afterEach(() => {
    jest.resetAllMocks();
  });

  it('should build new version', () => {
    const version = '1.0.0';
    fs.readFileSync.mockReturnValue(JSON.stringify([]));
    let initialWorkingDir = process.cwd();
    documentation.release('1.0.0');
    expect(exec.execSync).toHaveBeenCalledTimes(3);
    expect(process.chdir).toHaveBeenCalledWith(docsPath());

    expect(exec.execSync).toHaveBeenCalledWith(`npm install`);
    expect(exec.execSync).toHaveBeenCalledWith(`npm run docusaurus docs:version ${version}`);
    expect(exec.execSync).toHaveBeenCalledWith(`git add ../website`);
    expect(process.chdir).toHaveBeenCalledWith(process.cwd());
  });

  it('should override version', () => {
    fs.readFileSync.mockReturnValue(JSON.stringify(['1.0.0', '2.0.0']));

    const version = '2.0.0';
    const removeVersion = '2.0.0';
    documentation.release(version, removeVersion);

    expect(exec.execSync).toHaveBeenCalledTimes(5);

    expect(process.chdir).toHaveBeenCalledWith(docsPath());
    expect(exec.execSync).toHaveBeenCalledWith(
      `rm -rf ${docsPath()}/versioned_docs/version-${version}`
    );
    expect(exec.execSync).toHaveBeenCalledWith(
      `rm -f ${docsPath()}/versioned_sidebars/version-${version}-sidebars.json`
    );
    expect(exec.execSync).toHaveBeenCalledWith(`npm run docusaurus docs:version ${version}`);
    expect(exec.execSync).toHaveBeenCalledWith(`git add ../website`);
    expect(process.chdir).toHaveBeenCalledWith(process.cwd());
  });
});

function docsPath() {
  return `${process.cwd()}/website`;
}
