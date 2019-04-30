/* tslint:disable: no-console */
const exec = require('shell-utils').exec;

async function run() {
  exec.execSync(`npm run clean`);
  exec.execSync(`npm run test-js`);

  if (process.env.JENKINS_CI) {
    const sdkmanager = '/usr/local/share/android-sdk/tools/bin/sdkmanager';
    exec.execSync(`echo y | ${sdkmanager} --update && echo y | ${sdkmanager} --licenses`);
}

  exec.execAsyncSilent(`npm run start`);
  await exec.execAsyncAll(`npm run test-unit-android`, `npm run test-unit-ios`);
  await exec.execAsyncAll(`npm run test-e2e-android`, `npm run test-e2e-ios`);
  exec.execSync(`npm run clean`);
  console.log('ALL PASSED!!!');
}

run();
