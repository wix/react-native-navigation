const includes = require('lodash-es/includes')
const exec = require('shell-utils').exec;

const release = includes(process.argv, '--release');

run();

function run() {
  exec.execSync(`cd playground/android && ./gradlew ${release ? 'installRelease' : 'installDebug'}`);
}
