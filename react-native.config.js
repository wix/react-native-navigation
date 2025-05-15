module.exports = {
  dependency: {
    platforms: {
      ios: {},
      android: {
        sourceDir: './lib/android/app/',
        packageImportPath: 'import com.reactnativenavigation.react.NavigationPackage;',
        packageInstance: 'new NavigationPackage()',
      },
    },
  },
  project: {
    android: {
      sourceDir: './playground/android/',
    },
  },
};
