const _ = require('lodash');
const exec = require('shell-utils').exec;

const android = _.includes(process.argv, '--android');
const release = _.includes(process.argv, '--release');

run();

function run() {
  const platform = android ? `android` : `ios`;
  const prefix = android ? `android.emu` : `ios.sim`;
  const suffix = release ? `release` : `debug`;
  const configuration = `${prefix}.${suffix}`;
  const cleanup = process.env.CI ? `--cleanup` : ``;

  exec.execSync(`detox build --configuration ${configuration}`);
  exec.execSync(`detox test --configuration ${configuration} --platform ${platform} ${cleanup}`);
}
