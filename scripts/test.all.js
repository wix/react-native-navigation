/* eslint no-console:0 */
const exec = require('shell-utils').exec;

async function run() {
  exec.execSync(`yarn run clean`);
  exec.execSync(`yarn run test-js`);
  exec.execAsyncSilent(`yarn run start`);
  await exec.execAsyncAll(`yarn run test-unit-android`, `yarn run test-unit-ios`);
  await exec.execAsyncAll(`yarn run test-e2e-android`, `yarn run test-e2e-ios`);
  exec.execSync(`yarn run clean`);
  console.log('ALL PASSED!!!');
}

run();
