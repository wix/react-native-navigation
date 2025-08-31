// @ts-check
const fs = require('fs');
const XcodeLinker = require('./xcodeLinker');

jest.mock('fs');
jest.mock('glob', () => ({
  sync: jest.fn()
}));

describe('XcodeLinker', () => {
  let xcodeLinker;
  let mockFs;
  let mockGlob;

  beforeEach(() => {
    jest.clearAllMocks();
    mockFs = require('fs');
    mockGlob = require('glob');
    xcodeLinker = new XcodeLinker();
  });

  describe('_findIosDirectory', () => {
    it('should find ios directory with Podfile', () => {
      mockGlob.sync.mockReturnValue(['ios']);
      mockFs.existsSync.mockImplementation((path) => path === 'ios/Podfile');
      
      const result = xcodeLinker._findIosDirectory();
      
      expect(result).toBe('ios');
    });

    it('should fallback to current directory if Podfile exists', () => {
      mockGlob.sync.mockReturnValue([]);
      mockFs.existsSync.mockImplementation((path) => path === 'Podfile');
      
      const result = xcodeLinker._findIosDirectory();
      
      expect(result).toBe('.');
    });

    it('should return null if no iOS directory found', () => {
      mockGlob.sync.mockReturnValue([]);
      mockFs.existsSync.mockReturnValue(false);
      
      const result = xcodeLinker._findIosDirectory();
      
      expect(result).toBeNull();
    });
  });

  describe('_copyStaticFrameworksScripts', () => {
    beforeEach(() => {
      mockFs.existsSync.mockReturnValue(true);
      mockFs.copyFileSync = jest.fn();
      mockFs.chmodSync = jest.fn();
      xcodeLinker._findIosDirectory = jest.fn().mockReturnValue('ios');
    });

    it('should copy all script files', () => {
      xcodeLinker._copyStaticFrameworksScripts();
      
      expect(mockFs.copyFileSync).toHaveBeenCalledTimes(6); // 5 scripts + README
      expect(mockFs.chmodSync).toHaveBeenCalledTimes(5); // Only scripts get chmod
    });

    it('should throw error if iOS directory not found', () => {
      xcodeLinker._findIosDirectory = jest.fn().mockReturnValue(null);
      
      expect(() => xcodeLinker._copyStaticFrameworksScripts()).toThrow('Could not find iOS directory');
    });

    it('should throw error if automation scripts not found', () => {
      mockFs.existsSync.mockImplementation((path) => 
        !path.includes('static_frameworks_automation')
      );
      
      expect(() => xcodeLinker._copyStaticFrameworksScripts()).toThrow('Static frameworks automation scripts not found');
    });
  });

  describe('link', () => {
    beforeEach(() => {
      xcodeLinker._copyStaticFrameworksScripts = jest.fn();
      xcodeLinker._addStaticFrameworksBuildPhase = jest.fn();
    });

    it('should copy scripts and add build phase when Xcode project found', () => {
      xcodeLinker.xcodeProjectPath = 'ios/project.pbxproj';
      
      xcodeLinker.link();
      
      expect(xcodeLinker._copyStaticFrameworksScripts).toHaveBeenCalled();
      expect(xcodeLinker._addStaticFrameworksBuildPhase).toHaveBeenCalled();
    });

    it('should copy scripts only when no Xcode project found', () => {
      xcodeLinker.xcodeProjectPath = null;
      
      xcodeLinker.link();
      
      expect(xcodeLinker._copyStaticFrameworksScripts).toHaveBeenCalled();
      expect(xcodeLinker._addStaticFrameworksBuildPhase).not.toHaveBeenCalled();
    });

    it('should handle errors gracefully', () => {
      xcodeLinker._copyStaticFrameworksScripts = jest.fn().mockImplementation(() => {
        throw new Error('Test error');
      });
      
      // Should not throw
      expect(() => xcodeLinker.link()).not.toThrow();
    });
  });
});
