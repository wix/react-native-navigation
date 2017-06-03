const exec = require('shell-utils').exec;

run();

function run() {
  exec.killPort(8081);
  exec.execSync(`watchman watch-del-all || true`);
  exec.execSync(`adb reverse tcp:8081 tcp:8081 || true`);
  exec.execSync(`rm -rf lib/ios/DerivedData/`);
  exec.execSync(`rm -rf playground/ios/DerivedData/`);
  exec.execSync(`cd lib/android && ./gradlew clean`);
  exec.execSync(`cd playground/android && ./gradlew clean`);
  exec.execSync(`cd AndroidE2E && ./gradlew clean`);
}
