const childProcess = require('child_process');
const fs = require('fs');
const os = require('os');
const path = require('path');

const documentation = require('./documentation');

describe('Documentation script', () => {
  let docsPath;
  let versionsPath;
  let execFileSync;

  beforeEach(() => {
    docsPath = fs.mkdtempSync(path.join(os.tmpdir(), 'rnn-documentation-'));
    versionsPath = path.join(docsPath, 'versions.json');
    fs.mkdirSync(path.join(docsPath, 'versioned_docs'), { recursive: true });
    fs.mkdirSync(path.join(docsPath, 'versioned_sidebars'), { recursive: true });
    fs.writeFileSync(versionsPath, JSON.stringify(['2.0.0', '1.0.0']));
    execFileSync = jest.spyOn(childProcess, 'execFileSync').mockImplementation(() => {});
  });

  afterEach(() => {
    jest.restoreAllMocks();
    fs.rmSync(docsPath, { recursive: true, force: true });
  });

  it('generates a new version without changing the caller cwd', () => {
    const cwd = process.cwd();

    documentation.release('3.0.0', undefined, { docsPath });

    expect(execFileSync).toHaveBeenCalledWith(
      'npm',
      ['run', 'docusaurus', '--', 'docs:version', '3.0.0'],
      { cwd: docsPath, stdio: 'inherit' }
    );
    expect(process.cwd()).toBe(cwd);
    expect(readVersions()).toEqual(['2.0.0', '1.0.0']);
  });

  it.each(['../escape', '1.0.0; touch /tmp/pwned', '$(touch /tmp/pwned)', ''])(
    'rejects unsafe version %s before mutation',
    (version) => {
      const before = fs.readFileSync(versionsPath, 'utf8');
      const cwd = process.cwd();

      expect(() => documentation.release(version, undefined, { docsPath })).toThrow(
        /dotted numeric version/
      );
      expect(execFileSync).not.toHaveBeenCalled();
      expect(fs.readFileSync(versionsPath, 'utf8')).toBe(before);
      expect(process.cwd()).toBe(cwd);
    }
  );

  it('rejects removal of an unknown version before mutation', () => {
    const before = fs.readFileSync(versionsPath, 'utf8');
    const cwd = process.cwd();

    expect(() => documentation.release('3.0.0', '4.0.0', { docsPath })).toThrow(
      'Documentation version does not exist: 4.0.0'
    );
    expect(execFileSync).not.toHaveBeenCalled();
    expect(fs.readFileSync(versionsPath, 'utf8')).toBe(before);
    expect(process.cwd()).toBe(cwd);
  });

  it.each(['../escape', '1.0.0; touch /tmp/pwned', '$(touch /tmp/pwned)'])(
    'rejects unsafe removal version %s before mutation',
    (removeVersion) => {
      const before = fs.readFileSync(versionsPath, 'utf8');
      const cwd = process.cwd();

      expect(() => documentation.release('3.0.0', removeVersion, { docsPath })).toThrow(
        /dotted numeric version/
      );
      expect(execFileSync).not.toHaveBeenCalled();
      expect(fs.readFileSync(versionsPath, 'utf8')).toBe(before);
      expect(process.cwd()).toBe(cwd);
    }
  );

  it('rejects regenerating an existing version unless removing that same version', () => {
    const before = fs.readFileSync(versionsPath, 'utf8');
    const cwd = process.cwd();

    expect(() => documentation.release('2.0.0', undefined, { docsPath })).toThrow(
      'Documentation version already exists: 2.0.0'
    );
    expect(execFileSync).not.toHaveBeenCalled();
    expect(fs.readFileSync(versionsPath, 'utf8')).toBe(before);
    expect(process.cwd()).toBe(cwd);
  });

  it('removes an existing version and its generated files before regenerating it', () => {
    const versionDocs = path.join(docsPath, 'versioned_docs', 'version-2.0.0');
    const versionSidebar = path.join(docsPath, 'versioned_sidebars', 'version-2.0.0-sidebars.json');
    fs.mkdirSync(versionDocs);
    fs.writeFileSync(path.join(versionDocs, 'README.md'), 'old docs');
    fs.writeFileSync(versionSidebar, '{}');

    documentation.release('2.0.0', '2.0.0', { docsPath });

    expect(fs.existsSync(versionDocs)).toBe(false);
    expect(fs.existsSync(versionSidebar)).toBe(false);
    expect(readVersions()).toEqual(['1.0.0']);
    expect(execFileSync).toHaveBeenCalledTimes(1);
  });

  it('removes the last existing version from the versions list', () => {
    fs.writeFileSync(versionsPath, JSON.stringify(['1.0.0']));

    documentation.release('2.0.0', '1.0.0', { docsPath });

    expect(readVersions()).toEqual([]);
    expect(execFileSync).toHaveBeenCalledTimes(1);
  });

  function readVersions() {
    return JSON.parse(fs.readFileSync(versionsPath, 'utf8'));
  }
});
