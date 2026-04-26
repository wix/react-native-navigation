#!/usr/bin/env node
/*
  Updates package.json React Native and React versions based on REACT_NATIVE_VERSION env var.
  - Fetches the React peer dependency from the npm registry for the specified RN version
  - Updates package.json dependencies/devDependencies accordingly
  - Updates @react-native/* and @react-native-community/cli* packages
  - Updates Gradle wrapper version based on RN version
*/

const fs = require('fs/promises');
const path = require('path');
const {
    parseRnMinor,
    getReactNativeToolsVersion,
    getCliVersion,
    getGradleVersion,
    getJestPresetVersion,
    getReanimatedOverride,
    getWorkletsOverride,
    shouldRemoveWorklets,
    getTestingLibraryOverride,
} = require('./versionMapping');

const REACT_NATIVE_TOOLS_PACKAGES = [
    '@react-native/babel-preset',
    '@react-native/eslint-config',
    '@react-native/metro-config',
    '@react-native/typescript-config',
];

const CLI_PACKAGES = [
    '@react-native-community/cli',
    '@react-native-community/cli-platform-android',
    '@react-native-community/cli-platform-ios',
];

/**
 * Fetch and compute the versions we need in order to update package.json files.
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
    const rnMinor = parseRnMinor(rnVersion);

    if (Number.isNaN(rnMinor)) {
        throw new Error(`Could not parse minor version from "${rnVersion}"`);
    }

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

    const hasReactNativeInDependencies = packageJson.dependencies['react-native'] !== undefined;
    const hasReactNativeInDevDependencies = packageJson.devDependencies['react-native'] !== undefined;
    const hasReactInDependencies = packageJson.dependencies['react'] !== undefined;
    const hasReactInDevDependencies = packageJson.devDependencies['react'] !== undefined;

    if (hasReactNativeInDependencies || !hasReactNativeInDevDependencies) {
        packageJson.dependencies['react-native'] = targetRn;
    }
    if (hasReactInDependencies || !hasReactInDevDependencies) {
        packageJson.dependencies['react'] = reactVersion;
    }

    if (hasReactNativeInDevDependencies) {
        packageJson.devDependencies['react-native'] = targetRn;
    }
    if (hasReactInDevDependencies) {
        packageJson.devDependencies['react'] = reactVersion;
    }
    if (packageJson.devDependencies['react-test-renderer'] !== undefined) {
        packageJson.devDependencies['react-test-renderer'] = reactVersion;
    }

    const toolsVersion = getReactNativeToolsVersion(rnMinor, targetRn);
    for (const pkg of REACT_NATIVE_TOOLS_PACKAGES) {
        if (packageJson.devDependencies[pkg] !== undefined) {
            packageJson.devDependencies[pkg] = toolsVersion;
        }
    }

    const cliVersion = getCliVersion(rnMinor);
    for (const pkg of CLI_PACKAGES) {
        if (packageJson.devDependencies[pkg] !== undefined) {
            packageJson.devDependencies[pkg] = cliVersion;
        }
    }

    const testingLibOverride = getTestingLibraryOverride(rnMinor);
    if (testingLibOverride) {
        packageJson.devDependencies['@testing-library/react-native'] = testingLibOverride;
    }

    const jestPresetVersion = getJestPresetVersion(rnMinor, targetRn);
    if (jestPresetVersion) {
        packageJson.devDependencies['@react-native/jest-preset'] = jestPresetVersion;
    } else {
        delete packageJson.devDependencies['@react-native/jest-preset'];
    }

    const reanimatedOverride = getReanimatedOverride(rnMinor);
    if (reanimatedOverride) {
        packageJson.devDependencies['react-native-reanimated'] = reanimatedOverride;
    }

    const workletsOverride = getWorkletsOverride(rnMinor);
    if (workletsOverride && packageJson.devDependencies['react-native-worklets'] !== undefined) {
        packageJson.devDependencies['react-native-worklets'] = workletsOverride;
    }

    if (shouldRemoveWorklets(rnMinor)) {
        delete packageJson.devDependencies['react-native-worklets'];
    }

    await fs.writeFile(packageJsonPath, JSON.stringify(packageJson, null, 2) + '\n', 'utf8');
}

function logChanges(packageJsonPath, versions, basePath) {
    const { rnVersion, reactVersion, rnMinor } = versions;
    const label = path.relative(basePath, path.dirname(packageJsonPath)) || 'root';
    console.log(
        `Changed dependencies (${label}):\n  react-native: ${rnVersion}\n  react: ${reactVersion}`
    );
    if (!Number.isNaN(rnMinor)) {
        console.log(
            `Aligned testing libs for RN minor ${rnMinor} (${label})`
        );
    }
}

/**
 * Update gradle-wrapper.properties with the appropriate Gradle version.
 */
async function updateGradleWrapper(rnMinor, gradleWrapperPath) {
    const gradleVersion = getGradleVersion(rnMinor);
    const content = await fs.readFile(gradleWrapperPath, 'utf8');

    const updatedContent = content.replace(
        /distributionUrl=https\\:\/\/services\.gradle\.org\/distributions\/gradle-[\d.]+-bin\.zip/,
        `distributionUrl=https\\://services.gradle.org/distributions/gradle-${gradleVersion}-bin.zip`
    );

    await fs.writeFile(gradleWrapperPath, updatedContent, 'utf8');
    console.log(`Updated Gradle version to ${gradleVersion}`);
}

async function main(basePath) {
    basePath = basePath || process.cwd();

    const rnVersion = process.env.REACT_NATIVE_VERSION;
    if (!rnVersion) {
        console.log('REACT_NATIVE_VERSION not set; skipping version update');
        return;
    }

    const versions = await extractVersions(rnVersion);

    const targets = [
        path.join(basePath, 'package.json'),
        path.join(basePath, 'playground', 'package.json'),
    ];

    for (const packageJsonPath of targets) {
        await updatePackageJsonAt(packageJsonPath, versions);
        logChanges(packageJsonPath, versions, basePath);
    }

    const gradleWrapperPath = path.join(
        basePath, 'playground', 'android', 'gradle', 'wrapper', 'gradle-wrapper.properties'
    );
    await updateGradleWrapper(versions.rnMinor, gradleWrapperPath);
}

if (require.main === module) {
    main().catch((err) => {
        console.error('[changeReactNativeVersion] Error:', err.message);
        process.exit(1);
    });
}

module.exports = { extractVersions, updatePackageJsonAt, updateGradleWrapper, main };
