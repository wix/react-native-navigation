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

    // PRIORITY: Check if we're in RNN's own directory structure (workspace/CI scenario)
    // In this case, __dirname would be like: /path/to/rnn/autolink/postlink/__helpers__
    // And we want to find: /path/to/rnn/playground/package.json
    var currentPath = __dirname;

    // Walk up to find RNN root (containing this autolink folder)
    for (var k = 0; k < 5; k++) {
        // Check if this looks like RNN root by checking for playground subdirectory
        var playgroundPath = nodePath.join(currentPath, 'playground');
        var playgroundPackageJson = nodePath.join(playgroundPath, 'package.json');

        if (fs.existsSync(playgroundPackageJson)) {
            try {
                var pkg = JSON.parse(fs.readFileSync(playgroundPackageJson, 'utf8'));
                if ((pkg.dependencies && pkg.dependencies['react-native']) ||
                    (pkg.devDependencies && pkg.devDependencies['react-native'])) {
                    // Found it! Prioritize this path
                    searchDirs.unshift(playgroundPath);
                    break;
                }
            } catch (e) { }
        }

        var parent = nodePath.dirname(currentPath);
        if (parent === currentPath) break;
        currentPath = parent;
    }

    // If we're inside a package (like in node_modules or a workspace), 
    // also try searching from common project locations
    currentPath = __dirname;
    for (var k = 0; k < 10; k++) {
        var basename = nodePath.basename(currentPath);

        // If we're in node_modules, the parent is likely the project root
        if (basename === 'node_modules') {
            searchDirs.push(nodePath.dirname(currentPath));
            break;
        }

        // If we find a workspace scenario (e.g., we're in the workspace root but project is in subdirectory)
        // Check for common subdirectories like 'playground', 'example', 'app'
        var commonProjectDirs = ['playground', 'example', 'app', 'demo'];
        for (var m = 0; m < commonProjectDirs.length; m++) {
            var potentialProjectDir = nodePath.join(currentPath, commonProjectDirs[m]);
            if (fs.existsSync(potentialProjectDir)) {
                searchDirs.push(potentialProjectDir);
            }
        }

        var parent = nodePath.dirname(currentPath);
        if (parent === currentPath) break;
        currentPath = parent;
    }

    for (var j = 0; j < searchDirs.length; j++) {
        var searchDir = searchDirs[j];
        for (var i = 0; i < 10; i++) {
            var packagePath = nodePath.join(searchDir, 'package.json');
            if (fs.existsSync(packagePath)) {
                try {
                    var pkg = JSON.parse(fs.readFileSync(packagePath, 'utf8'));
                    if ((pkg.dependencies && pkg.dependencies['react-native']) ||
                        (pkg.devDependencies && pkg.devDependencies['react-native'])) {
                        // Exclude react-native-navigation's own package.json to avoid false positives
                        // in workspace/monorepo scenarios
                        if (pkg.name !== 'react-native-navigation') {
                            return packagePath;
                        }
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
 * Get React Native version as parsed object from node_modules
 * @returns {Object|null} { major, minor, patch, raw } or null
 */
function getReactNativeVersion() {
    var projectPackageJsonPath = findProjectPackageJson();
    if (!projectPackageJsonPath) {
        warnn('Could not find package.json to detect React Native version');
        return null;
    }

    var projectRoot = nodePath.dirname(projectPackageJsonPath);

    // First, try to read from node_modules/react-native/package.json (actual installed version)
    var rnPackageJsonPath = nodePath.join(projectRoot, 'node_modules', 'react-native', 'package.json');

    try {
        if (fs.existsSync(rnPackageJsonPath)) {
            var rnPackageJson = JSON.parse(fs.readFileSync(rnPackageJsonPath, 'utf8'));
            var rnVersion = rnPackageJson.version;

            if (rnVersion) {
                var parts = rnVersion.split('.');
                return {
                    major: parseInt(parts[0]) || 0,
                    minor: parseInt(parts[1]) || 0,
                    patch: parseInt(parts[2]) || 0,
                    raw: rnVersion
                };
            }
        }
    } catch (e) {
        // Fall through to backup method
    }

    // Fallback: read from project's package.json dependencies
    try {
        var packageJson = JSON.parse(fs.readFileSync(projectPackageJsonPath, 'utf8'));
        var rnVersion = packageJson.dependencies && packageJson.dependencies['react-native'] ||
            packageJson.devDependencies && packageJson.devDependencies['react-native'];

        if (!rnVersion) {
            warnn('React Native not found in package.json or node_modules');
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

