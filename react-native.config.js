module.exports = {
  dependency: {
    platforms: {
      ios: {},
      android: {
        sourceDir: './lib/android/app/',
        packageImportPath: 'import com.reactnativenavigation.react.NavigationPackage;',
        packageInstance: 'new NavigationPackage(reactNativeHost)',
      },
    },
  },
  project: {
    android: {
      sourceDir: './playground/android/',
    },
  },
};
