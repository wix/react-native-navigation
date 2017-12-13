const exec = require('shell-utils').exec;

run();

function run() {
  exec.killPort(8081);
  exec.execSync(`watchman watch-del-all || true`);
  exec.execSync(`adb reverse tcp:8081 tcp:8081 || true`);
  exec.execSync(`node ./node_modules/react-native/local-cli/cli.js start --root ./playground/`);
}
