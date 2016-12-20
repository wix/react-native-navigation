const cp = require('child_process');

function exec(cmd) {
  cp.execSync(cmd, {stdio: ['inherit', 'inherit', 'inherit']});
}

function execSilent(cmd) {
  cp.execSync(cmd, {stdio: ['inherit', 'ignore', 'inherit']});
}

function kill(process) {
  execSilent(`pkill -f "${process}" || true`);
}

function buildXcodeReleaseProj() {
//exec(`RCT_NO_LAUNCH_PACKAGER=true cd ios && xcodebuild \
//-scheme example_Detox clean build \
//-project example.xcodeproj \
//-destination "platform=iOS Simulator,name=iPhone 7,OS=10.1" \
//-derivedDataPath ./DerivedData/example`);
}

function e2e() {
  kill(`detox-server`);

  try {
    cp.exec(`./node_modules/.bin/detox-server > ./detox-server.log 2>&1`);
    exec(`BABEL_ENV=test ./node_modules/mocha/bin/mocha e2e --opts ./e2e/mocha.opts`);
  } finally {
    kill(`detox-server`);
    kill(`Simulator`);
    kill(`CoreSimulator`);
    exec(`cat ./detox-server.log`);
    exec(`sleep 5`);
  }
}

function run() {
  buildXcodeReleaseProj();
  e2e();
}

run();

