/* tslint:disable: no-console */
const exec = require('shell-utils').exec;

async function run() {
  exec.execSync(`npm run clean`);
  exec.execSync(`npm run test-js`);
  exec.execAsyncSilent(`npm run start`);
  await exec.execAsyncAll(
    `npm run test-unit-android`,
    `npm run test-unit-ios`,
    `npm run test-snapshot-ios`
  );
  await exec.execAsyncAll(`npm run test-e2e-android`, `npm run test-e2e-ios`);
  exec.execSync(`npm run clean`);
  console.log('ALL PASSED!!!');
}

run();
