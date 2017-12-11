const _ = require('lodash');
const exec = require('shell-utils').exec;

const release = _.includes(process.argv, '--release');

run();

function run() {
  const conf = release ? `release` : `debug`;
  exec.execAsyncSilent(`npm run start`);
  exec.execSync(`detox build --configuration android.emu.${conf} && detox test --configuration android.emu.${conf} ${process.env.CI ? '--cleanup' : ''}`);
}

