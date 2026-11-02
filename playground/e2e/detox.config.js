// `playground` runs on the app-delegate life cycle, `playgroundScene` (Info-Scene.plist) on the
// UIScene life cycle. Same sources, same bundle id, so only one of them is installed at a time.
// DETOX_IOS_DERIVED_DATA lets a second Xcode keep its own build products.
const derivedDataPath = process.env.DETOX_IOS_DERIVED_DATA || 'ios/DerivedData/playground';
const iosApp = (scheme, configuration) => {
  const product = scheme.replace('_release', '');
  return {
    type: 'ios.app',
    binaryPath: `${derivedDataPath}/Build/Products/${configuration}-iphonesimulator/${product}.app`,
    build: `RCT_NO_LAUNCH_PACKAGER=true xcodebuild build -scheme ${scheme} -workspace ios/playground.xcworkspace -sdk iphonesimulator -configuration ${configuration} -derivedDataPath ${derivedDataPath} ONLY_ACTIVE_ARCH=YES -quiet -UseModernBuildSystem=YES`,
  };
};

// DETOX_IOS_DEVICE_ID pins one simulator by UDID when several match the device type and OS.
const iosSimulator = (type, os) => ({
  type: 'ios.simulator',
  device: process.env.DETOX_IOS_DEVICE_ID ? { id: process.env.DETOX_IOS_DEVICE_ID } : { type, os },
});

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
    simulator: iosSimulator(
      process.env.DETOX_IOS_DEVICE || 'iPhone 17 Pro Max',
      process.env.DETOX_IOS_OS || '26.1'
    ),
    // iPhone Duo (foldable, iOS 27.1+) ships in its own simulator runtime.
    simulatorDuo: iosSimulator('iPhone Duo', process.env.DETOX_IOS_OS || '27.2'),
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
      ...iosApp('playground', 'Debug'),
      start: 'react-native start',
    },
    'ios.release': iosApp('playground_release', 'Release'),
    'ios.debug.scene': {
      ...iosApp('playgroundScene', 'Debug'),
      start: 'react-native start',
    },
    'ios.release.scene': iosApp('playgroundScene_release', 'Release'),
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
    'ios.sim.debug.scene': {
      app: 'ios.debug.scene',
      device: 'simulator',
    },
    'ios.sim.release.scene': {
      app: 'ios.release.scene',
      device: 'simulator',
    },
    'ios.sim.release.scene.duo': {
      app: 'ios.release.scene',
      device: 'simulatorDuo',
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

