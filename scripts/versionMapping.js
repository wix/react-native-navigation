/**
 * Pure version-mapping functions for React Native version switching.
 * Maps RN minor versions to compatible dependency versions.
 */

const CLI_VERSION_MAP = {
    77: '15.0.1',
    78: '15.0.1',
    82: '20.0.0',
    83: '20.0.0',
    84: '20.1.0',
};

function parseRnMinor(rnVersion) {
    const match = String(rnVersion).match(/^\d+\.(\d+)/);
    return match ? Number(match[1]) : NaN;
}

function getReactNativeToolsVersion(rnMinor) {
    return `0.${rnMinor}.0`;
}

function getCliVersion(rnMinor) {
    return CLI_VERSION_MAP[rnMinor] || CLI_VERSION_MAP[84];
}

function getGradleVersion(rnMinor) {
    return rnMinor < 82 ? '8.14.1' : '9.0.0';
}

function getReanimatedOverride(rnMinor) {
    return rnMinor <= 78 ? '3.18.0' : null;
}

function shouldRemoveWorklets(rnMinor) {
    return rnMinor <= 78;
}

function getTestingLibraryOverride(rnMinor) {
    return rnMinor <= 77 ? '12.4.5' : null;
}

module.exports = {
    parseRnMinor,
    getReactNativeToolsVersion,
    getCliVersion,
    getGradleVersion,
    getReanimatedOverride,
    shouldRemoveWorklets,
    getTestingLibraryOverride,
};
