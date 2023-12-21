module.exports = (path, options) => {
  return options.defaultResolver(path, {
    ...options,
    packageFilter: (pkg) => {
      return {
        ...pkg,
        // Alter the value of `main` before resolving the package
        main: pkg['react-native'] || pkg.main,
      };
    },
  });
};
