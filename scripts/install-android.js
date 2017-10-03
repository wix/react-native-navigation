const _ = require('lodash');
const exec = require('shell-utils').exec;

const release = _.includes(process.argv, '--release');

run();

function run() {
  exec.execSync(`cd playground/android && ./gradlew ${release ? 'installRelease' : 'installDebug'}`);
}
