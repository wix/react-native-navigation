/**
 * Pure version-mapping functions for React Native version switching.
 * Maps RN minor versions to compatible dependency versions.
 */

const CLI_VERSION_MAP = {
    77: '15.0.1',
    78: '15.0.1',
    79: '18.0.0',
    82: '20.0.0',
    83: '20.0.0',
    84: '20.1.0',
    85: '20.1.0',
    88: '20.2.0',
};

function parseRnMinor(rnVersion) {
    const match = String(rnVersion).match(/^\d+\.(\d+)/);
    return match ? Number(match[1]) : NaN;
}

function getReactNativeToolsVersion(rnMinor, rnVersion) {
    if (rnMinor >= 85) {
        return rnVersion;
    }
    return `0.${rnMinor}.0`;
}

function getCliVersion(rnMinor) {
    return CLI_VERSION_MAP[rnMinor] || CLI_VERSION_MAP[84];
}

function getGradleVersion(rnMinor) {
    if (rnMinor >= 88) {
        return '9.4.1';
    }
    if (rnMinor >= 85) {
        return '9.3.1';
    }
    return rnMinor < 82 ? '8.14.1' : '9.0.0';
}

function getJestPresetVersion(rnMinor, rnVersion) {
    return rnMinor >= 85 ? rnVersion : null;
}

function getReanimatedOverride(rnMinor) {
    // Reanimated 4.7.x supports RN 0.86 - 0.88 and requires worklets 0.13.x.
    if (rnMinor >= 86) {
        return '4.7.1';
    }
    if (rnMinor >= 85) {
        return '4.3.0';
    }
    // Reanimated 3.x bundles its own worklets and supports RN 0.76 - 0.80.
    return rnMinor <= 79 ? '3.18.0' : null;
}

function getWorkletsOverride(rnMinor) {
    if (rnMinor >= 86) {
        return '0.13.0';
    }
    return rnMinor >= 85 ? '0.8.1' : null;
}

// RN 0.88 removed legacy headers (e.g. React/RCTRootContentView.h) that gesture-handler < 2.33 imports.
function getGestureHandlerOverride(rnMinor) {
    return rnMinor >= 88 ? '2.33.1' : null;
}

function shouldRemoveWorklets(rnMinor) {
    return rnMinor <= 79;
}

function getTestingLibraryOverride(rnMinor) {
    return rnMinor <= 77 ? '12.4.5' : null;
}

module.exports = {
    parseRnMinor,
    getReactNativeToolsVersion,
    getCliVersion,
    getGradleVersion,
    getJestPresetVersion,
    getReanimatedOverride,
    getWorkletsOverride,
    getGestureHandlerOverride,
    shouldRemoveWorklets,
    getTestingLibraryOverride,
};
