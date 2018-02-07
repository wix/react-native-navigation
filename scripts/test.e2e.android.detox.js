const _ = require('lodash');
const exec = require('shell-utils').exec;

const release = _.includes(process.argv, '--release');

run();

function run() {
  const conf = release ? `release` : `debug`;
  exec.execSync(`detox build --configuration android.emu.${conf}`);
  exec.execSync(`detox test --configuration android.emu.${conf} ${process.env.CI ? '--cleanup' : ''}`);
}
