const _ = require('lodash');
const exec = require('shell-utils').exec;

const release = _.includes(process.argv, '--release');

run();

function run() {
    const suffix = release ? `release` : `debug`;
    const configuration = `android.emu.${suffix}.locked`;

    exec.execSync(`detox test --configuration ${configuration} -f "DeviceLocked.test.js"`)
}
