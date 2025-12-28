#!/usr/bin/env node
/*
  Updates gradle-wrapper.properties based on REACT_NATIVE_VERSION env var.
  - RN < 0.82: Gradle 8.14.1
  - RN >= 0.82: Gradle 9.0.0
*/

const fs = require('fs/promises');
const path = require('path');

const GRADLE_WRAPPER_PATH = path.join(
    process.cwd(),
    'playground',
    'android',
    'gradle',
    'wrapper',
    'gradle-wrapper.properties'
);

/**
 * Determine the Gradle version based on RN minor version.
 */
function getGradleVersion(rnMinor) {
    return rnMinor < 82 ? '8.14.1' : '9.0.0';
}

/**
 * Parse RN version string and return the minor version number.
 */
function parseRnMinor(rnVersion) {
    const match = String(rnVersion).match(/^\d+\.(\d+)/);
    return match ? Number(match[1]) : NaN;
}

/**
 * Update gradle-wrapper.properties with the specified Gradle version.
 */
async function updateGradleWrapper(gradleVersion) {
    const content = await fs.readFile(GRADLE_WRAPPER_PATH, 'utf8');
    
    const updatedContent = content.replace(
        /distributionUrl=https\\:\/\/services\.gradle\.org\/distributions\/gradle-[\d.]+-bin\.zip/,
        `distributionUrl=https\\://services.gradle.org/distributions/gradle-${gradleVersion}-bin.zip`
    );
    
    await fs.writeFile(GRADLE_WRAPPER_PATH, updatedContent, 'utf8');
    return gradleVersion;
}

async function main() {
    const rnVersion = process.env.REACT_NATIVE_VERSION;
    if (!rnVersion) {
        console.log('REACT_NATIVE_VERSION not set; skipping Gradle version update');
        return;
    }

    const rnMinor = parseRnMinor(rnVersion);
    if (Number.isNaN(rnMinor)) {
        console.error(`Could not parse RN minor version from: ${rnVersion}`);
        process.exit(1);
    }

    const gradleVersion = getGradleVersion(rnMinor);
    await updateGradleWrapper(gradleVersion);
    
    console.log(`Updated Gradle version to ${gradleVersion} (RN ${rnVersion}, minor=${rnMinor})`);
}

main().catch((err) => {
    console.error('[changeGradleVersion] Error:', err.message);
    process.exit(1);
});

