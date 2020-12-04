const includes = require('lodash/includes');
const exec = require('shell-utils').exec;

const release = includes(process.argv, '--release');

const isWindows = process.platform === 'win32' ? true : false;

run();

function run() {
  if (isWindows) {
    exec.execSync(
      `cd playground/android && gradlew ${release ? 'installRelease' : 'installDebug'}`
    );
  } else {
    exec.execSync(
      `cd playground/android && ./gradlew ${release ? 'installRelease' : 'installDebug'}`
    );
  }
}
