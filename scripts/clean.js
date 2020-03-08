const exec = require('shell-utils').exec;

const isWindows = process.platform === 'win32' ? true : false;

if (isWindows) runWin32();
else run();

function run() {
  exec.killPort(8081);
  exec.execSync(`watchman watch-del-all || true`);
  exec.execSync(`adb reverse tcp:8081 tcp:8081 || true`);
  exec.execSync(`rm -rf lib/ios/DerivedData`);
  exec.execSync(`rm -rf playground/ios/DerivedData`);
  exec.execSync(`rm -rf lib/android/build`);
  exec.execSync(`rm -rf lib/android/app/build`);
  exec.execSync(`rm -rf playground/android/build`);
  exec.execSync(`rm -rf playground/android/app/build`);
  exec.execSync(`rm -rf lib/dist`);
}

function runWin32() {
  exec.execSync(`adb reverse tcp:8081 tcp:8081 || true`);

  exec.execSync('del /F /S /Q lib\\android\\build');
  exec.execSync('del /F /S /Q lib\\android\\app\\build');
  exec.execSync('del /F /S /Q playground\\android\\build');
  exec.execSync('del /F /S /Q playground\\android\\app\\build');
  exec.execSync('del /F /S /Q lib\\dist');
}
