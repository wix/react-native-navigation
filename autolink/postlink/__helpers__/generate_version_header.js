#!/usr/bin/env node
const fs = require('fs');
const path = require('path');
const { getReactNativeVersion, findProjectPackageJson } = require('./reactNativeVersion');

// Logging helper that writes to both stderr and a log file
function log(message) {
  console.error(message);

  // Also write to a log file for debugging if stderr is suppressed
  try {
    const logFile = path.join(__dirname, '../../../ios/rnn_version_detection.log');
    const timestamp = new Date().toISOString();
    fs.appendFileSync(logFile, `[${timestamp}] ${message}\n`, 'utf8');
  } catch (e) {
    // Ignore log file errors
  }
}

function generateVersionHeader() {
  const startDir = __dirname;

  log(`[RNN] === React Native Version Detection ===`);
  log(`[RNN] Script location (__dirname): ${startDir}`);
  log(`[RNN] Working directory (cwd): ${process.cwd()}`);

  const packageJsonPath = findProjectPackageJson();

  if (!packageJsonPath) {
    log('[RNN] ❌ ERROR: Project package.json not found');
    log('[RNN] This usually means the script could not locate your React Native project.');
    return;
  }

  log(`[RNN] ✓ Found package.json: ${packageJsonPath}`);

  // Determine actual source of version
  const projectRoot = path.dirname(packageJsonPath);
  const rnPackageJsonPath = path.join(projectRoot, 'node_modules', 'react-native', 'package.json');
  let versionSource = packageJsonPath;
  let versionSourceType = 'package.json';

  if (fs.existsSync(rnPackageJsonPath)) {
    versionSource = rnPackageJsonPath;
    versionSourceType = 'node_modules/react-native/package.json (installed version)';
  }

  const versionInfo = getReactNativeVersion();

  if (!versionInfo) {
    log('[RNN] ❌ ERROR: react-native not found in package.json or node_modules');
    log('[RNN] Make sure react-native is installed and listed as a dependency.');
    return;
  }

  log(`[RNN] ✓ React Native ${versionInfo.raw} (source: ${versionSourceType})`);

  const { major, minor, patch } = versionInfo;

  // Generate header content
  const headerContent = `//
  // ReactNativeVersionExtracted.h
  // React Native version: ${versionInfo.raw}
  // Generated on: ${new Date().toISOString()}
  // Source: ${versionSource}
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
    log('[RNN] ❌ ERROR: Could not find react-native-navigation root directory');
    return;
  }

  const outputFile = path.join(rnnPackageJson, 'ios/ReactNativeVersionExtracted.h');

  fs.writeFileSync(outputFile, headerContent, 'utf8');
  log(`[RNN] ✅ Generated header: ${outputFile}`);
  log(`[RNN] ✅ Version constants: ${major}.${minor}.${patch}`);
  log(`[RNN] === Completed Successfully ===`);
}

// Run if called directly
if (require.main === module) {
  generateVersionHeader();
}

module.exports = { generateVersionHeader };