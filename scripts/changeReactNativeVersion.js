#!/usr/bin/env node
/*
  Updates package.json React Native and React versions based on REACT_NATIVE_VERSION env var.
  - Fetches the React peer dependency from the npm registry for the specified RN version
  - Updates package.json dependencies/devDependencies accordingly
*/

const fs = require('fs/promises');
const path = require('path');

async function main() {
    const reactNativeVersion = process.env.REACT_NATIVE_VERSION;
    if (!reactNativeVersion) {
        console.log('REACT_NATIVE_VERSION not set; skipping version update');
        return;
    }

    const pkgPath = path.join(process.cwd(), 'package.json');
    const pkgRaw = await fs.readFile(pkgPath, 'utf8');
    const packageJson = JSON.parse(pkgRaw);

    const registryUrl = `https://registry.npmjs.org/react-native/${reactNativeVersion}`;
    const res = await fetch(registryUrl);
    if (!res.ok) {
        throw new Error(`Failed to fetch ${registryUrl}: ${res.status} ${res.statusText}`);
    }
    const data = await res.json();

    const reactPeer = data?.peerDependencies?.react;
    if (!reactPeer) {
        throw new Error(`No peerDependencies.react found for react-native@${reactNativeVersion}`);
    }
    const reactVersion = String(reactPeer).replace(/^\^/, '');

    // Update dependencies if present
    packageJson.dependencies = packageJson.dependencies || {};
    packageJson.dependencies['react-native'] = reactNativeVersion;
    packageJson.dependencies['react'] = reactVersion;

    // Also update devDependencies if they reference RN/React
    if (packageJson.devDependencies) {
        if (packageJson.devDependencies['react-native'] !== undefined) {
            packageJson.devDependencies['react-native'] = reactNativeVersion;
        }
        if (packageJson.devDependencies['react'] !== undefined) {
            packageJson.devDependencies['react'] = reactVersion;
        }
    }


    const rnMinorMatch = String(reactNativeVersion).match(/^\d+\.(\d+)/);
    const rnMinor = rnMinorMatch ? Number(rnMinorMatch[1]) : NaN;

    packageJson.devDependencies = packageJson.devDependencies || {};
    if (rnMinor <= 77) {
        packageJson.devDependencies['react-test-renderer'] = '18.2.0';
        packageJson.devDependencies['@testing-library/react-native'] = '12.4.5';
    }

    await fs.writeFile(pkgPath, JSON.stringify(packageJson, null, 2) + '\n', 'utf8');

    console.log(
        `Changed dependencies:\n  react-native: ${reactNativeVersion}\n  react: ${reactVersion}`
    );
    if (!Number.isNaN(rnMinor)) {
        const rtr = packageJson.devDependencies['react-test-renderer'];
        const rtl = packageJson.devDependencies['@testing-library/react-native'];
        console.log(
            `Aligned testing libs for RN minor ${rnMinor}:\n  react-test-renderer: ${rtr}\n  @testing-library/react-native: ${rtl}`
        );
    }
}

main().catch((err) => {
    console.error('[changeReactNativeVersion] Error:', err.message);
    process.exit(1);
});


