// @ts-check
var fs = require('fs');
var nodePath = require('path');
var { warnn } = require('../log');

/**
 * Find the project root package.json
 * @returns {string|null} Path to project's package.json or null if not found
 */
function findProjectPackageJson() {
    var searchDirs = [process.cwd(), __dirname];

    for (var j = 0; j < searchDirs.length; j++) {
        var searchDir = searchDirs[j];
        for (var i = 0; i < 10; i++) {
            var packagePath = nodePath.join(searchDir, 'package.json');
            if (fs.existsSync(packagePath)) {
                try {
                    var pkg = JSON.parse(fs.readFileSync(packagePath, 'utf8'));
                    if ((pkg.dependencies && pkg.dependencies['react-native']) ||
                        (pkg.devDependencies && pkg.devDependencies['react-native'])) {
                        return packagePath;
                    }
                } catch (e) { }
            }
            var parent = nodePath.dirname(searchDir);
            if (parent === searchDir) break;
            searchDir = parent;
        }
    }

    return null;
}

/**
 * Get React Native version as parsed object
 * @returns {Object|null} { major, minor, patch, raw } or null
 */
function getReactNativeVersion() {
    var projectPackageJsonPath = findProjectPackageJson();
    if (!projectPackageJsonPath) {
        warnn('Could not find package.json to detect React Native version');
        return null;
    }

    try {
        var packageJson = JSON.parse(fs.readFileSync(projectPackageJsonPath, 'utf8'));
        var rnVersion = packageJson.dependencies && packageJson.dependencies['react-native'] ||
            packageJson.devDependencies && packageJson.devDependencies['react-native'];

        if (!rnVersion) {
            warnn('React Native not found in package.json');
            return null;
        }

        // Parse version (remove ^, ~, >=, etc.)
        var cleanVersion = rnVersion.replace(/^[\^~>=<]+/, '');
        var parts = cleanVersion.split('.');

        return {
            major: parseInt(parts[0]) || 0,
            minor: parseInt(parts[1]) || 0,
            patch: parseInt(parts[2]) || 0,
            raw: rnVersion
        };
    } catch (e) {
        warnn('Error detecting React Native version: ' + e.message);
        return null;
    }
}

module.exports = {
    findProjectPackageJson: findProjectPackageJson,
    getReactNativeVersion: getReactNativeVersion
};

