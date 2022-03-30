module.exports = {
  preset: 'react-native',
  transformIgnorePatterns: [
    'node_modules/(?!(@react-native|react-native|react-native-ui-lib|react-native-animatable)/)',
  ],
  transform: {
    '\\.[jt]sx?$': 'babel-jest',
  },
  roots: [
    '<rootDir>/lib/src/',
    '<rootDir>/playground/src/',
    '<rootDir>/integration/',
    '<rootDir>/scripts/',
    '<rootDir>/e2e/',
  ],
  setupFilesAfterEnv: ['./jest-setup.js'],
  testPathIgnorePatterns: ['/node_modules/'],
  moduleNameMapper: {
    'react-native-navigation/Mock': '<rootDir>/lib/Mock',
    'react-native-navigation': '<rootDir>/lib/src',
    '\\.(jpg|jpeg|png|gif|eot|otf|webp|svg|ttf|woff|woff2|mp4|webm|wav|mp3|m4a|aac|oga)$':
      '<rootDir>/playground/img/layouts@2x.png',
  },
  collectCoverageFrom: [
    'lib/src/**/*.ts',
    'lib/src/**/*.tsx',
    'integration/**/*.js',
    '!lib/dist/index.js',
    '!lib/dist/Navigation.js',
    '!lib/dist/adapters/**/*',
    '!lib/dist/interfaces/**/*',
    '!lib/dist/**/*.test.*',
    '!integration/**/*.test.*',
    '!integration/*.test.*',
    '!e2e/**/*test.js',
  ],
  resetMocks: true,
  resetModules: true,
  coverageReporters: ['json', 'lcov', 'text', 'html'],
};
