/** @type {Detox.DetoxConfig} */
module.exports = {
  testRunner: {
    $0: 'jest',
    args: {
      config: 'e2e/config.js',
      _: ['e2e'],
    },
    jest: {
      setupTimeout: 120000,
      reportSpecs: false,
      reportWorkerAssign: false,
    },
  },
  apps: {
    'ios.debug': {
      type: 'ios.app',
      binaryPath:
        'playground/ios/DerivedData/playground/Build/Products/Debug-iphonesimulator/playground.app',
      build: ':',
    },
    'ios.release': {
      type: 'ios.app',
      binaryPath:
        'playground/ios/DerivedData/playground/Build/Products/Release-iphonesimulator/playground.app',
      build:
        'RCT_NO_LAUNCH_PACKAGER=true xcodebuild build -scheme playground_release -workspace playground/ios/playground.xcworkspace -sdk iphonesimulator -configuration Release -derivedDataPath playground/ios/DerivedData/playground ONLY_ACTIVE_ARCH=YES -quiet -UseModernBuildSystem=YES',
    },
    'android.debug': {
      type: 'android.apk',
      binaryPath: 'playground/android/app/build/outputs/apk/debug/app-debug.apk',
      build:
        'cd playground/android && ./gradlew app:assembleDebug app:assembleAndroidTest -DtestBuildType=debug',
    },
    'android.release': {
      type: 'android.apk',
      binaryPath: 'playground/android/app/build/outputs/apk/release/app-release.apk',
      build:
        'cd playground/android && ./gradlew app:assembleRelease app:assembleAndroidTest -DtestBuildType=release',
    },
  },
  devices: {
    simulator: {
      type: 'ios.simulator',
      device: {
        type: 'iPhone 13 Pro Max',
      },
    },
    'genymotion.emulator.name': {
      type: 'android.genycloud',
      device: {
        recipeUUID: 'a50a71d6-da90-4c67-bdfa-5b602b0bbd15',
      },
    },
    emulator: {
      type: 'android.emulator',
      device: {
        avdName: 'Pixel_API_28',
      },
    },
  },
  configurations: {
    'ios.none': {
      binaryPath:
        'playground/ios/DerivedData/playground/Build/Products/Debug-iphonesimulator/playground.app',
      type: 'ios.none',
      name: 'iPhone 11',
      session: {
        server: 'ws://localhost:8099',
        sessionId: 'playground',
      },
    },
    'ios.sim.debug': {
      app: 'ios.debug',
      device: 'simulator',
    },
    'ios.sim.release': {
      app: 'ios.release',
      device: 'simulator',
    },
    'android.emu.debug': {
      app: 'android.debug',
      device: 'emulator',
    },
    'android.emu.release': {
      app: 'android.release',
      device: 'emulator',
    },
    'android.genycloud.release': {
      app: 'android.release',
      device: 'genymotion.emulator.name',
    },
  },
};
