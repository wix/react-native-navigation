const _ = require('lodash');
const exec = require('shell-utils').exec;

const android = _.includes(process.argv, '--android');
const release = _.includes(process.argv, '--release');
const skipBuild = _.includes(process.argv, '--skipBuild');
const headless = _.includes(process.argv, '--headless');
const multi = _.includes(process.argv, '--multi');

run();

function run() {
    const platform = android ? `android` : `ios`;
    const prefix = android ? `android.emu` : `ios.sim`;
    const suffix = release ? `release` : `debug`;
    const configuration = `${prefix}.${suffix}`;
    const cleanup = process.env.CI ? `--cleanup` : ``;
    const headless$ = android ? headless ? `--headless` : `` : ``;
    const workers = multi ? 2 : 1;

    if (!skipBuild) {
        exec.execSync(`detox build --configuration ${configuration}`);
    }
    exec.execSync(`detox test --configuration ${configuration} --platform ${platform} ${cleanup} ${headless$} ${!android ? `-w ${workers}` : ``}`);
}
