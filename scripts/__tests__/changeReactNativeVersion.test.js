const fs = require('fs/promises');
const path = require('path');
const os = require('os');

let extractVersions, updatePackageJsonAt, updateGradleWrapper, main;

beforeEach(() => {
    jest.resetModules();
    const mod = require('../changeReactNativeVersion');
    extractVersions = mod.extractVersions;
    updatePackageJsonAt = mod.updatePackageJsonAt;
    updateGradleWrapper = mod.updateGradleWrapper;
    main = mod.main;
});

function makePackageJson(overrides = {}) {
    return {
        name: 'test-pkg',
        dependencies: {
            'react-native': '0.83.0',
            react: '19.2.0',
        },
        devDependencies: {
            'react-native': '0.83.0',
            react: '19.2.0',
            'react-test-renderer': '19.2.0',
            '@react-native/babel-preset': '0.83.0',
            '@react-native/eslint-config': '0.83.0',
            '@react-native/metro-config': '0.83.0',
            '@react-native/typescript-config': '0.83.0',
            '@react-native-community/cli': '20.0.0',
            '@react-native-community/cli-platform-android': '20.0.0',
            '@react-native-community/cli-platform-ios': '20.0.0',
            '@testing-library/react-native': '^13.0.1',
            'react-native-reanimated': '4.1.5',
            'react-native-worklets': '0.5.0',
        },
        ...overrides,
    };
}

const GRADLE_WRAPPER_CONTENT = `distributionBase=GRADLE_USER_HOME
distributionPath=wrapper/dists
distributionUrl=https\\://services.gradle.org/distributions/gradle-9.0.0-bin.zip
networkTimeout=10000
validateDistributionUrl=true
zipStoreBase=GRADLE_USER_HOME
zipStorePath=wrapper/dists
`;

const VERSION_CONFIGS = [
    {
        rnVersion: '0.77.3',
        reactVersion: '18.3.1',
        rnMinor: 77,
        expectedCli: '15.0.1',
        expectedTools: '0.77.0',
        expectedGradle: '8.14.1',
        expectedTestingLib: '12.4.5',
        expectedReanimated: '3.18.0',
        expectWorkletsRemoved: true,
    },
    {
        rnVersion: '0.78.3',
        reactVersion: '18.3.1',
        rnMinor: 78,
        expectedCli: '15.0.1',
        expectedTools: '0.78.0',
        expectedGradle: '8.14.1',
        expectedTestingLib: null,
        expectedReanimated: '3.18.0',
        expectWorkletsRemoved: true,
    },
    {
        rnVersion: '0.82.1',
        reactVersion: '19.0.0',
        rnMinor: 82,
        expectedCli: '20.0.0',
        expectedTools: '0.82.0',
        expectedGradle: '9.0.0',
        expectedTestingLib: null,
        expectedReanimated: null,
        expectWorkletsRemoved: false,
    },
    {
        rnVersion: '0.83.0',
        reactVersion: '19.2.0',
        rnMinor: 83,
        expectedCli: '20.0.0',
        expectedTools: '0.83.0',
        expectedGradle: '9.0.0',
        expectedTestingLib: null,
        expectedReanimated: null,
        expectWorkletsRemoved: false,
    },
    {
        rnVersion: '0.84.0',
        reactVersion: '19.2.3',
        rnMinor: 84,
        expectedCli: '20.1.0',
        expectedTools: '0.84.0',
        expectedGradle: '9.0.0',
        expectedTestingLib: null,
        expectedReanimated: null,
        expectWorkletsRemoved: false,
    },
];

describe('changeReactNativeVersion', () => {
    let tmpDir;

    beforeEach(async () => {
        tmpDir = await fs.mkdtemp(path.join(os.tmpdir(), 'rnn-test-'));
    });

    afterEach(async () => {
        await fs.rm(tmpDir, { recursive: true, force: true });
    });

    describe('updatePackageJsonAt', () => {
        describe.each(VERSION_CONFIGS)(
            'RN $rnVersion',
            ({
                rnVersion,
                reactVersion,
                rnMinor,
                expectedCli,
                expectedTools,
                expectedTestingLib,
                expectedReanimated,
                expectWorkletsRemoved,
            }) => {
                let result;

                beforeEach(async () => {
                    const pkgPath = path.join(tmpDir, 'package.json');
                    await fs.writeFile(pkgPath, JSON.stringify(makePackageJson(), null, 2));
                    await updatePackageJsonAt(pkgPath, { rnVersion, reactVersion, rnMinor });
                    result = JSON.parse(await fs.readFile(pkgPath, 'utf8'));
                });

                it('updates react-native version', () => {
                    expect(result.dependencies['react-native']).toBe(rnVersion);
                    expect(result.devDependencies['react-native']).toBe(rnVersion);
                });

                it('updates react and react-test-renderer versions', () => {
                    expect(result.dependencies.react).toBe(reactVersion);
                    expect(result.devDependencies.react).toBe(reactVersion);
                    expect(result.devDependencies['react-test-renderer']).toBe(reactVersion);
                });

                it('updates @react-native/* packages', () => {
                    expect(result.devDependencies['@react-native/babel-preset']).toBe(expectedTools);
                    expect(result.devDependencies['@react-native/eslint-config']).toBe(expectedTools);
                    expect(result.devDependencies['@react-native/metro-config']).toBe(expectedTools);
                    expect(result.devDependencies['@react-native/typescript-config']).toBe(expectedTools);
                });

                it('updates @react-native-community/cli* packages', () => {
                    expect(result.devDependencies['@react-native-community/cli']).toBe(expectedCli);
                    expect(result.devDependencies['@react-native-community/cli-platform-android']).toBe(expectedCli);
                    expect(result.devDependencies['@react-native-community/cli-platform-ios']).toBe(expectedCli);
                });

                it('handles testing-library override correctly', () => {
                    if (expectedTestingLib) {
                        expect(result.devDependencies['@testing-library/react-native']).toBe(expectedTestingLib);
                    } else {
                        expect(result.devDependencies['@testing-library/react-native']).toBe('^13.0.1');
                    }
                });

                it('handles reanimated override correctly', () => {
                    if (expectedReanimated) {
                        expect(result.devDependencies['react-native-reanimated']).toBe(expectedReanimated);
                    } else {
                        expect(result.devDependencies['react-native-reanimated']).toBe('4.1.5');
                    }
                });

                it('handles worklets removal correctly', () => {
                    if (expectWorkletsRemoved) {
                        expect(result.devDependencies['react-native-worklets']).toBeUndefined();
                    } else {
                        expect(result.devDependencies['react-native-worklets']).toBe('0.5.0');
                    }
                });
            }
        );

        it('skips devDependencies keys that do not exist in source', async () => {
            const minimalPkg = {
                name: 'minimal',
                dependencies: { 'react-native': '0.83.0', react: '19.2.0' },
                devDependencies: { 'react-test-renderer': '19.2.0' },
            };
            const pkgPath = path.join(tmpDir, 'package.json');
            await fs.writeFile(pkgPath, JSON.stringify(minimalPkg, null, 2));
            await updatePackageJsonAt(pkgPath, { rnVersion: '0.84.0', reactVersion: '19.2.3', rnMinor: 84 });
            const result = JSON.parse(await fs.readFile(pkgPath, 'utf8'));
            expect(result.devDependencies['@react-native/babel-preset']).toBeUndefined();
            expect(result.devDependencies['@react-native-community/cli']).toBeUndefined();
        });
    });

    describe('updateGradleWrapper', () => {
        it('writes correct Gradle version for RN < 82', async () => {
            const gradlePath = path.join(tmpDir, 'gradle-wrapper.properties');
            await fs.writeFile(gradlePath, GRADLE_WRAPPER_CONTENT);
            await updateGradleWrapper(77, gradlePath);
            const content = await fs.readFile(gradlePath, 'utf8');
            expect(content).toContain('gradle-8.14.1-bin.zip');
        });

        it('writes correct Gradle version for RN >= 82', async () => {
            const gradlePath = path.join(tmpDir, 'gradle-wrapper.properties');
            await fs.writeFile(gradlePath, GRADLE_WRAPPER_CONTENT);
            await updateGradleWrapper(84, gradlePath);
            const content = await fs.readFile(gradlePath, 'utf8');
            expect(content).toContain('gradle-9.0.0-bin.zip');
        });
    });

    describe('extractVersions', () => {
        beforeEach(() => {
            global.fetch = jest.fn();
        });

        afterEach(() => {
            delete global.fetch;
        });

        it('extracts versions from npm registry response', async () => {
            global.fetch.mockResolvedValue({
                ok: true,
                json: async () => ({ peerDependencies: { react: '^19.2.3' } }),
            });

            const result = await extractVersions('0.84.0');
            expect(result).toEqual({ rnVersion: '0.84.0', reactVersion: '19.2.3', rnMinor: 84 });
            expect(global.fetch).toHaveBeenCalledWith('https://registry.npmjs.org/react-native/0.84.0');
        });

        it('throws on fetch failure', async () => {
            global.fetch.mockResolvedValue({ ok: false, status: 404, statusText: 'Not Found' });
            await expect(extractVersions('0.99.0')).rejects.toThrow('Failed to fetch');
        });

        it('throws when no react peer dep found', async () => {
            global.fetch.mockResolvedValue({
                ok: true,
                json: async () => ({ peerDependencies: {} }),
            });
            await expect(extractVersions('0.84.0')).rejects.toThrow('No peerDependencies.react');
        });
    });

    describe('main', () => {
        it('skips when REACT_NATIVE_VERSION is not set', async () => {
            delete process.env.REACT_NATIVE_VERSION;
            const consoleSpy = jest.spyOn(console, 'log').mockImplementation();
            await main(tmpDir);
            const loggedMessage = consoleSpy.mock.calls[0]?.[0] || '';
            expect(loggedMessage).toContain('not set');
            consoleSpy.mockRestore();
        });
    });
});
