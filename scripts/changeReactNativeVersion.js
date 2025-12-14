#!/usr/bin/env node
/*
  Updates package.json React Native and React versions based on REACT_NATIVE_VERSION env var.
  - Fetches the React peer dependency from the npm registry for the specified RN version
  - Updates package.json dependencies/devDependencies accordingly
*/

const fs = require('fs/promises');
const path = require('path');

/**
 * Fetch and compute the versions we need in order to update package.json files.
 * Returns an object with:
 * - rnVersion: the exact React Native version
 * - reactVersion: the matching React version (per RN's peerDependencies)
 * - rnMinor: RN minor number (e.g., for 0.77.3 it's 77)
 */
async function extractVersions(rnVersion) {
    const registryUrl = `https://registry.npmjs.org/react-native/${rnVersion}`;
    const res = await fetch(registryUrl);
    if (!res.ok) {
        throw new Error(`Failed to fetch ${registryUrl}: ${res.status} ${res.statusText}`);
    }
    const data = await res.json();

    const reactPeer = data?.peerDependencies?.react;
    if (!reactPeer) {
        throw new Error(`No peerDependencies.react found for react-native@${rnVersion}`);
    }

    const reactVersion = String(reactPeer).replace(/^\^/, '');
    const rnMinorMatch = String(rnVersion).match(/^\d+\.(\d+)/);
    const rnMinor = rnMinorMatch ? Number(rnMinorMatch[1]) : NaN;

    return { rnVersion, reactVersion, rnMinor };
}

/**
 * Update a specific package.json file in-place using the provided versions.
 */
async function updatePackageJsonAt(packageJsonPath, versions) {
    const pkgRaw = await fs.readFile(packageJsonPath, 'utf8');
    const packageJson = JSON.parse(pkgRaw);

    const { rnVersion: targetRn, reactVersion, rnMinor } = versions;

    packageJson.dependencies = packageJson.dependencies || {};
    packageJson.devDependencies = packageJson.devDependencies || {};

    // Update RN and React in dependencies
    packageJson.dependencies['react-native'] = targetRn;
    packageJson.dependencies['react'] = reactVersion;
    packageJson.devDependencies['react-test-renderer'] = reactVersion;

    // Also update in devDependencies if present
    if (packageJson.devDependencies['react-native'] !== undefined) {
        packageJson.devDependencies['react-native'] = targetRn;
    }
    if (packageJson.devDependencies['react'] !== undefined) {
        packageJson.devDependencies['react'] = reactVersion;
    }

    // Align testing libs for older RN minors (<= 77)
    if (rnMinor <= 77) {
        packageJson.devDependencies['@testing-library/react-native'] = '12.4.5';
    }

    if (rnMinor <= 78) {
        packageJson.devDependencies['react-native-reanimated'] = '3.18.0';
        delete packageJson.devDependencies['react-native-worklets'];
    }

    await fs.writeFile(packageJsonPath, JSON.stringify(packageJson, null, 2) + '\n', 'utf8');
}

function logChanges(packageJsonPath, versions) {
    const { rnVersion, reactVersion, rnMinor } = versions;
    const label = path.relative(process.cwd(), path.dirname(packageJsonPath)) || 'root';
    console.log(
        `Changed dependencies (${label}):\n  react-native: ${rnVersion}\n  react: ${reactVersion}`
    );
    if (!Number.isNaN(rnMinor)) {
        console.log(
            `Aligned testing libs for RN minor ${rnMinor} (${label})`
        );
    }
}

async function main() {
    const rnVersion = process.env.REACT_NATIVE_VERSION;
    if (!rnVersion) {
        console.log('REACT_NATIVE_VERSION not set; skipping version update');
        return;
    }

    // Compute the versions once, then apply to desired package.json files
    const versions = await extractVersions(rnVersion);

    const targets = [
        path.join(process.cwd(), 'package.json'),
        path.join(process.cwd(), 'playground', 'package.json'),
    ];

    for (const packageJsonPath of targets) {
        await updatePackageJsonAt(packageJsonPath, versions);
        logChanges(packageJsonPath, versions);
    }
}

main().catch((err) => {
    console.error('[changeReactNativeVersion] Error:', err.message);
    process.exit(1);
});


