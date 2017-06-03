const _ = require('lodash');
const exec = require('shell-utils').exec;

const release = _.includes(process.argv, 'release');

function buildProjForDetox() {
  const scheme = release ? `playground_release` : `playground`;
  const conf = release ? `Release` : `Debug`;

  exec.execSync(`cd ./playground/ios &&
            RCT_NO_LAUNCH_PACKAGER=true
            xcodebuild build
            -scheme ${scheme}
            -project playground.xcodeproj
            -sdk iphonesimulator
            -configuration ${conf}
            -derivedDataPath ./DerivedData/playground
            ONLY_ACTIVE_ARCH=YES`);
}

function runDetoxUsingMocha() {
  const detoxAppBuildPath = `playground/ios/DerivedData/playground/Build/Products/${release ? 'Release' : 'Debug'}-iphonesimulator/playground.app`;

  exec.execSync(`detoxAppBuildPath="${detoxAppBuildPath}"
                              BABEL_ENV=test
                              ./node_modules/mocha/bin/mocha e2e
                                --timeout ${5 * 60 * 1000}
                                --recursive
                                --bail`);
}

function e2e() { //eslint-disable-line
  try {
    runDetoxUsingMocha();
  } finally {
    if (process.env.CI) {
      exec.kill(`Simulator`);
      exec.kill(`CoreSimulator`);
      exec.execSync(`sleep 5`);
    }
  }
}

function run() {
  if (!exec.which(`fbsimctl`)) {
    throw new Error(`fbsimctl must be installed: "brew tap facebook/fb && brew install fbsimctl"`);
  }
  buildProjForDetox();
  e2e();
}

run();
