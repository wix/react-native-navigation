/* tslint:disable: no-console */
const exec = require('shell-utils').exec;

async function run() {
  exec.execSync(`yarn run clean`);
  exec.execSync(`yarn run test-js`);
  exec.execSync(`yarn run test-autolink`);
  exec.execAsyncSilent(`yarn start`);
  await exec.execAsyncAll(
    `yarn run test-unit-android`,
    `yarn run test-unit-ios`,
    `yarn run test-snapshot-ios`
  );
  await exec.execAsyncAll(`yarn run test-e2e-android`, `yarn run test-e2e-ios`);
  exec.execSync(`yarn run clean`);
  console.log('ALL PASSED!!!');
}

run();
