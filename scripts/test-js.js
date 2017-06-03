const exec = require('shell-utils').exec;

run();

function run() {
  exec.execSync(`BABEL_ENV=test eslint lib/src integration e2e scripts playground/src`);
  exec.execSync(`BABEL_ENV=test jest --coverage`);
}
