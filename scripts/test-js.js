const exec = require('shell-utils').exec;
const _ = require('lodash');

const fix = _.includes(process.argv, '--fix') ? '--fix' : '';

const dirs = [
  'lib/src',
  'integration',
  'e2e',
  'scripts',
  'playground/src'
];

run();

function run() {
  exec.execSync('node -v');
  const paths = _.chain(dirs).map((d) => `'${d}/**/*.[tj]s*'`).join(' ').value();
  exec.execSync(`tslint ${paths} ${fix} --format verbose`);
  assertAllTsFilesInSrc();
  exec.execSync(`jest --coverage`);
}

function assertAllTsFilesInSrc() {
  const allFiles = exec.execSyncRead('find ./lib/src -type f');
  const lines = _.split(allFiles, '\n');
  const offenders = _.filter(lines, (f) => !f.endsWith('.ts') && !f.endsWith('.tsx'));
  if (offenders.length) {
    throw new Error(`\n\nOnly ts/tsx files are allowed:\n${offenders.join('\n')}\n\n\n`);
  }
}
