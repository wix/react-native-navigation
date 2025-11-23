/** @type {Detox.DetoxConfig} */
const config = {
  testRunner: {
    args: {
      config: 'e2e/jest.config.js',
    },
  },
  artifacts: {
    plugins: {
      log: 'all',
      screenshot: {
        shouldTakeAutomaticSnapshots: true,
        takeWhen: {},
      },
      timeline: 'all',
      uiHierarchy: 'enabled',
      video: 'failing',
    },
  },
  devices: {
    simulator: {
      type: 'ios.simulator',
      device: {
        type: 'iPhone 13 Pro Max',
        os: '15.5',
      },
    },
    emulator: {
      type: 'android.emulator',
      device: {
        avdName: 'Pixel_3a_API_35',
      },
      systemUI: {
        pointerLocationBar: 'hide',
        touches: 'show',
        navigationMode: '3-button',
        statusBar: {
          notifications: 'hide',
          clock: '1200',
          wifiSignal: 'strong',
          cellSignal: 'none',
          battery: 'full',
          charging: false,
        },
      },
    },
  },
  apps: {
    'ios.debug': {
      type: 'ios.app',
      binaryPath: 'ios/DerivedData/playground/Build/Products/Debug-iphonesimulator/playground.app',
      start: 'react-native start',
      build: 'RCT_NO_LAUNCH_PACKAGER=true xcodebuild build -scheme playground -workspace ios/playground.xcworkspace -sdk iphonesimulator -configuration Debug -derivedDataPath ios/DerivedData/playground ONLY_ACTIVE_ARCH=YES -quiet -UseModernBuildSystem=YES',
    },
    'ios.release': {
      type: 'ios.app',
      binaryPath: 'ios/DerivedData/playground/Build/Products/Release-iphonesimulator/playground.app',
      build: 'RCT_NO_LAUNCH_PACKAGER=true xcodebuild build -scheme playground_release -workspace ios/playground.xcworkspace -sdk iphonesimulator -configuration Release -derivedDataPath ios/DerivedData/playground ONLY_ACTIVE_ARCH=YES -quiet -UseModernBuildSystem=YES',
    },
    'android.debug': {
      type: 'android.apk',
      binaryPath: 'android/app/build/outputs/apk/debug/app-debug.apk',
      start: 'react-native start',
      build: 'cd android && ./gradlew app:assembleDebug app:assembleAndroidTest -DtestBuildType=debug',
      reversePorts: [
        8081,
      ],
    },
    'android.release': {
      type: 'android.apk',
      binaryPath: 'android/app/build/outputs/apk/release/app-release.apk',
      build: 'cd android && ./gradlew app:assembleRelease app:assembleAndroidTest -DtestBuildType=release',
    },
  },
  configurations: {
    'ios.none': {
      binaryPath: 'ios/DerivedData/playground/Build/Products/Debug-iphonesimulator/playground.app',
      type: 'ios.none',
      name: 'iPhone 13',
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
    'android.manual': {
      device: 'emulator',
      app: 'android.debug',
      behavior: {
        launchApp: 'manual',
      },
      session: {
        autoStart: true,
        debugSynchronization: 0,
        server: 'ws://localhost:8099',
        sessionId: 'test',
      },
      testRunner: {
        args: {
          testTimeout: 999999,
        },
      },
      artifacts: false,
    },
    'android.emu.release': {
      app: 'android.release',
      device: 'emulator',
    },
  },
};

module.exports = config;

