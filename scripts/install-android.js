const includes = require('lodash/includes')
const exec = require('shell-utils').exec;

const release = includes(process.argv, '--release');

run();

function run() {
  exec.execSync(`cd playground/android && ./gradlew ${release ? 'installRelease' : 'installDebug'}`);
}
