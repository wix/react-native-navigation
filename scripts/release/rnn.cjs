// Repository policy is separate from the release state machine. Other repositories
// must supply their own package/version/build policy before reusing the runner.
module.exports = {
  repository: 'wix/react-native-navigation',
  branch: 'master',
  workflow: '.github/workflows/release.yml',
  packageName: 'react-native-navigation',
  nativeStatus: 'buildkite/react-native-navigation',
  nativeStatusUrl: 'https://buildkite.com/wix-mobile-oss/react-native-navigation/builds/',
  nativeStatusCreator: { login: 'buildkite-limited-access[bot]', id: 20291210 },
  label: 'release',
  requiredFiles: [
    'package.json',
    'README.md',
    'LICENSE',
    'src/index.ts',
    'src/Mock/index.ts',
    'lib/module/index.js',
    'lib/module/Mock/index.js',
    'lib/typescript/index.d.ts',
    'lib/typescript/Mock/index.d.ts',
    'lib/module/package.json',
    'ReactNativeNavigation.podspec',
    'android/build.gradle',
    'autolink/postlink/run.js',
  ],
};
