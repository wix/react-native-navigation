#!/usr/bin/env node
const fs = require('fs');
const path = require('path');
const { getReactNativeVersion, findProjectPackageJson } = require('./reactNativeVersion');

function generateVersionHeader() {
  const startDir = __dirname;

  console.log(`Searching for project package.json from: ${startDir}`);

  const packageJsonPath = findProjectPackageJson();

  if (!packageJsonPath) {
    console.log('Warning: Project package.json not found');
    return;
  }

  console.log(`Using package.json: ${packageJsonPath}`);

  const versionInfo = getReactNativeVersion();

  if (!versionInfo) {
    console.log('Warning: react-native not found in package.json');
    return;
  }

  console.log(`Found React Native version: ${versionInfo.raw}`);

  const { major, minor, patch } = versionInfo;

  // Generate header content
  const headerContent = `//
  // ReactNativeVersionExtracted.h
  // React Native version: ${versionInfo.raw}
  // Generated on: ${new Date().toISOString()}
  // Source: ${packageJsonPath}
  //
  
  #ifndef ReactNativeVersionExtracted_h
  #define ReactNativeVersionExtracted_h
  
  static const int REACT_NATIVE_VERSION_MAJOR = ${major};
  static const int REACT_NATIVE_VERSION_MINOR = ${minor};
  static const int REACT_NATIVE_VERSION_PATCH = ${patch};
  
  #define RN_VERSION_MAJOR ${major}
  #define RN_VERSION_MINOR ${minor}
  #define RN_VERSION_PATCH ${patch}
  
  #endif
  `;

  // Find RNN root by looking upwards from script location for RNN's package.json
  let currentDir = __dirname;
  let rnnPackageJson = null;

  for (let i = 0; i < 5; i++) {
    const potential = path.join(currentDir, 'package.json');
    if (fs.existsSync(potential)) {
      const pkg = JSON.parse(fs.readFileSync(potential, 'utf8'));
      // This is RNN's package.json if name matches
      if (pkg.name === 'react-native-navigation') {
        rnnPackageJson = currentDir;
        break;
      }
    }
    currentDir = path.resolve(currentDir, '..');
  }

  if (!rnnPackageJson) {
    console.log('Warning: Could not find react-native-navigation root');
    return;
  }

  const outputFile = path.join(rnnPackageJson, 'ios/ReactNativeVersionExtracted.h');

  fs.writeFileSync(outputFile, headerContent, 'utf8');
  console.log(`✅ Generated ${outputFile}`);
  console.log(`   Version: ${major}.${minor}.${patch}`);
}

// Run if called directly
if (require.main === module) {
  generateVersionHeader();
}

module.exports = { generateVersionHeader };