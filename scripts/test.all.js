const exec = require('shell-utils').exec;

function run() {
  exec.execSync(`yarn run clean`);
  exec.execSync(`yarn run test-js`);
  exec.execSync(`yarn run test-unit-android -- release && yarn run test-unit-ios -- release`);
  exec.exec(`yarn run test-e2e-android -- release`);
  exec.exec(`yarn run test-e2e-ios -- release`);
}

run();
