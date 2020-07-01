const exec = require('shell-utils').exec;

const isWindows = process.platform === 'win32' ? true : false;

run();

function run() {
  if (isWindows) {
    exec.execSync(`del /F /S /Q lib\\dist`);
  } else {
    exec.execSync(`rm -rf ./lib/dist`);
  }
  exec.execSync(`tsc --watch`);
}
