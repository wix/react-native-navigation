const exec = require('shell-utils').exec;

const isWindows = process.platform === 'win32' ? true : false;

const sourceExts = ['js', 'json', 'ts', 'tsx'];
if (process.argv.indexOf('--e2e') > 0) {
 sourceExts.unshift('e2e.js');
}

if (isWindows) runWin32();
else run();

function run() {
  exec.execSync(`react-native start --sourceExts ${sourceExts}`);
}

function runWin32() {
  exec.execSync(`react-native start --sourceExts ${sourceExts}`);
}
