const {
    parseRnMinor,
    getReactNativeToolsVersion,
    getCliVersion,
    getGradleVersion,
    getReanimatedOverride,
    shouldRemoveWorklets,
    getTestingLibraryOverride,
} = require('../versionMapping');

describe('versionMapping', () => {
    describe('parseRnMinor', () => {
        it('parses minor from standard version', () => {
            expect(parseRnMinor('0.84.0')).toBe(84);
        });

        it('parses minor from patch version', () => {
            expect(parseRnMinor('0.77.3')).toBe(77);
        });

        it('returns NaN for invalid input', () => {
            expect(parseRnMinor('invalid')).toBeNaN();
        });
    });

    describe('getReactNativeToolsVersion', () => {
        it('returns 0.{minor}.0 for each minor', () => {
            expect(getReactNativeToolsVersion(84)).toBe('0.84.0');
            expect(getReactNativeToolsVersion(83)).toBe('0.83.0');
            expect(getReactNativeToolsVersion(77)).toBe('0.77.0');
        });
    });

    describe('getCliVersion', () => {
        it('returns correct CLI version for each supported RN minor', () => {
            expect(getCliVersion(77)).toBe('15.0.1');
            expect(getCliVersion(78)).toBe('15.0.1');
            expect(getCliVersion(82)).toBe('20.0.0');
            expect(getCliVersion(83)).toBe('20.0.0');
            expect(getCliVersion(84)).toBe('20.1.0');
        });
    });

    describe('getGradleVersion', () => {
        it('returns 8.14.1 for RN < 82', () => {
            expect(getGradleVersion(77)).toBe('8.14.1');
            expect(getGradleVersion(78)).toBe('8.14.1');
            expect(getGradleVersion(81)).toBe('8.14.1');
        });

        it('returns 9.0.0 for RN >= 82', () => {
            expect(getGradleVersion(82)).toBe('9.0.0');
            expect(getGradleVersion(83)).toBe('9.0.0');
            expect(getGradleVersion(84)).toBe('9.0.0');
        });
    });

    describe('getReanimatedOverride', () => {
        it('returns 3.18.0 for RN <= 78', () => {
            expect(getReanimatedOverride(77)).toBe('3.18.0');
            expect(getReanimatedOverride(78)).toBe('3.18.0');
        });

        it('returns null for RN > 78', () => {
            expect(getReanimatedOverride(79)).toBeNull();
            expect(getReanimatedOverride(84)).toBeNull();
        });
    });

    describe('shouldRemoveWorklets', () => {
        it('returns true for RN <= 78', () => {
            expect(shouldRemoveWorklets(77)).toBe(true);
            expect(shouldRemoveWorklets(78)).toBe(true);
        });

        it('returns false for RN > 78', () => {
            expect(shouldRemoveWorklets(79)).toBe(false);
            expect(shouldRemoveWorklets(84)).toBe(false);
        });
    });

    describe('getTestingLibraryOverride', () => {
        it('returns 12.4.5 for RN <= 77', () => {
            expect(getTestingLibraryOverride(77)).toBe('12.4.5');
        });

        it('returns null for RN > 77', () => {
            expect(getTestingLibraryOverride(78)).toBeNull();
            expect(getTestingLibraryOverride(84)).toBeNull();
        });
    });
});
