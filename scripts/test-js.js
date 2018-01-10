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
  const paths = _.chain(dirs).map((d) => `'${d}/**'`).join(' ').value();
  exec.execSync(`xo ${paths} ${fix}`);
  exec.execSync(`jest --coverage`);
}
