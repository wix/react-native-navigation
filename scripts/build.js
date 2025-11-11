const exec = require('shell-utils').exec;

const isWindows = process.platform === 'win32' ? true : false;

run();

function run() {
  if (isWindows) {
    exec.execSync(`del /F /S /Q lib\\module lib\\typescript`);
    exec.execSync(`tsc --project tsconfig.build.json && tsc --project tsconfig.mocks.json`);
  } else {
    exec.execSync(
      `rm -rf ./lib/module ./lib/typescript && tsc --project tsconfig.build.json && tsc --project tsconfig.mocks.json`
    );
  }
}
