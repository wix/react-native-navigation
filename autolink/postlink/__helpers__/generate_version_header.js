#!/usr/bin/env node
const fs = require('fs');
const path = require('path');

function findProjectRoot(startDir) {
  let currentDir = startDir;
  
  // Look for markers that indicate project root
  // We want the package.json that has react-native-navigation as a dependency
  for (let i = 0; i < 10; i++) {
    const packageJsonPath = path.join(currentDir, 'package.json');
    
    if (fs.existsSync(packageJsonPath)) {
      try {
        const packageData = JSON.parse(fs.readFileSync(packageJsonPath, 'utf8'));
        
        // This is the project root if it has react-native-navigation as a dependency
        if (packageData.dependencies?.['react-native-navigation'] || 
            packageData.devDependencies?.['react-native-navigation']) {
          console.log(`Found project root: ${currentDir}`);
          return packageJsonPath;
        }
      } catch (e) {
        // Invalid JSON, continue searching
      }
    }
    
    const parent = path.resolve(currentDir, '..');
    if (parent === currentDir) {
      // Reached filesystem root
      break;
    }
    currentDir = parent;
  }
  
  return null;
}

function generateVersionHeader() {
    const startDir = __dirname;
    
    console.log(`Searching for project package.json from: ${startDir}`);
    
    const packageJsonPath = findProjectRoot(startDir);
    
    if (!packageJsonPath || !fs.existsSync(packageJsonPath)) {
      console.log('Warning: Project package.json not found');
      return;
    }
    
    console.log(`Using package.json: ${packageJsonPath}`);
    
    const packageContent = fs.readFileSync(packageJsonPath, 'utf8');
    const packageData = JSON.parse(packageContent);
    
    const rnVersion = packageData.dependencies?.['react-native'] || 
                      packageData.devDependencies?.['react-native'];
    
    if (!rnVersion) {
      console.log('Warning: react-native not found in package.json');
      return;
    }
    
    console.log(`Found React Native version: ${rnVersion}`);
    
    // Parse version
    const cleanVersion = rnVersion.replace(/^[\^~>=<]+/, '');
    const versionParts = cleanVersion.split('.');
    
    const major = parseInt(versionParts[0]) || 0;
    const minor = parseInt(versionParts[1]) || 0;
    const patch = parseInt(versionParts[2]) || 0;
    
    // Generate header content
    const headerContent = `//
  // ReactNativeVersionExtracted.h
  // React Native version: ${rnVersion}
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
    
    // Find RNN root by looking for its package.json
    const projectRoot = path.dirname(packageJsonPath);
    const rnnRoot = path.join(projectRoot, 'node_modules', 'react-native-navigation');
    
    // Alternative: search upwards from script location for RNN's package.json
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