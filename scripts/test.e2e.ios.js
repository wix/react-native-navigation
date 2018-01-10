const _ = require('lodash');
const exec = require('shell-utils').exec;

const release = _.includes(process.argv, '--release');

run();

function run() {
  const conf = release ? `release` : `debug`;
  exec.execSync(`detox build --configuration ios.sim.${conf}`);
  exec.execSync(`detox test --configuration ios.sim.${conf} ${process.env.CI ? '--cleanup' : ''}`);
}
