const exec = require('shell-utils').exec;
const rimraf = require('rimraf');

const isWindows = process.platform === 'win32' ? true : false;

run();

function run() {
    if (isWindows) {
        rimraf.sync(`./lib/dist`);
        exec.execSync(`tsc --watch`);
    } else {
        exec.execSync(`rm -rf ./lib/dist && tsc --watch`);
    }
}