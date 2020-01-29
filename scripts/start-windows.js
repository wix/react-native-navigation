const exec = require('shell-utils').exec;

run();

function run() {
  // exec.killPort(8081);
  // exec.execSync(`rd "./lib/dist" /Q/S`);
  exec.execSync(`"./node_modules/.bin/tsc"`);
  exec.execSync(`adb reverse tcp:8081 tcp:8081`);
  exec.execSync(`node ./node_modules/react-native/local-cli/cli.js start`);
}
