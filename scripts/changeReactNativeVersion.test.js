const fs = require('fs/promises');
const { updatePackageJsonAt } = require('./changeReactNativeVersion');

jest.mock('fs/promises', () => ({ readFile: jest.fn(), writeFile: jest.fn() }));

it('restores Gesture Handler when switching RN 0.85 to 0.87 and back', async () => {
  let contents = JSON.stringify({
    devDependencies: {
      react: '19.2.3',
      'react-native': '0.85.2',
      'react-native-gesture-handler': '^2.29.1',
    },
  });
  fs.readFile.mockImplementation(async () => contents);
  fs.writeFile.mockImplementation(async (_path, value) => {
    contents = value;
  });
  await updatePackageJsonAt('package.json', {
    rnMinor: 87,
    rnVersion: '0.87.1',
    reactVersion: '19.2.3',
  });
  expect(JSON.parse(contents).devDependencies['react-native-gesture-handler']).toBe('3.2.1');
  await updatePackageJsonAt('package.json', {
    rnMinor: 85,
    rnVersion: '0.85.2',
    reactVersion: '19.2.3',
  });
  expect(JSON.parse(contents).devDependencies['react-native-gesture-handler']).toBe('^2.29.1');
});
