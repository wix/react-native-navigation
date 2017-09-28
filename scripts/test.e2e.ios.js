const _ = require('lodash');
const exec = require('shell-utils').exec;

const release = _.includes(process.argv, 'release');

function buildProjForDetox() {
  const scheme = release ? `playground_release` : `playground`;
  const conf = release ? `Release` : `Debug`;

  execWithXcprettyIfPossible(`cd ./playground/ios &&
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
                              ./node_modules/mocha/bin/mocha e2e
                                --timeout ${5 * 60 * 1000}
                                --recursive
                                --bail
                                ${process.env.CI ? '--cleanup' : ''}`);
}

function e2e() {
  runDetoxUsingMocha();
}

function run() {
  buildProjForDetox();
  e2e();
}

function execWithXcprettyIfPossible(cmd) {
  if (exec.which('xcpretty')) {
    exec.execSync(`${cmd} | xcpretty`);
  } else {
    exec.execSync(cmd);
  }
}

run();
