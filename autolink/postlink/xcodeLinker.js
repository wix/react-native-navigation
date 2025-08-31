// @ts-check
var path = require('./path');
var fs = require('fs');
var glob = require('glob');
var { logn, debugn, infon, errorn, warnn } = require('./log');

class XcodeLinker {
  constructor() {
    this.xcodeProjectPath = this._findXcodeProject();
    this.ignoreFolders = {
      ignore: ['node_modules/**', '**/build/**', '**/Build/**', '**/DerivedData/**', '**/*-tvOS*/**'],
    };
  }

  link() {
    logn('Setting up static frameworks automation...');
    
    try {
      this._copyStaticFrameworksScripts();
      
      if (this.xcodeProjectPath) {
        this._addStaticFrameworksBuildPhase();
        logn('✅ Static frameworks automation added to Xcode project');
      } else {
        logn('✅ Static frameworks scripts copied (Xcode integration via Podfile only)');
      }
    } catch (error) {
      warnn('Failed to setup static frameworks automation: ' + error.message);
      warnn('You can manually copy scripts from node_modules/react-native-navigation/autolink/ios-static-frameworks/');
    }
  }

  _copyStaticFrameworksScripts() {
    const nodeModulesPath = require('path').resolve(__dirname, '../ios-static-frameworks');
    const iosDirectory = this._findIosDirectory();
    
    if (!iosDirectory) {
      throw new Error('Could not find iOS directory');
    }
    
    if (!fs.existsSync(nodeModulesPath)) {
      throw new Error('Static frameworks automation scripts not found in node_modules');
    }
    
    debugn('Copying static frameworks scripts to: ' + iosDirectory);
    
    // Copy all .sh files from automation directory
    const scriptFiles = ['apply_header_guards.sh', 'apply_framework_includes.sh', 'apply_special_fixes.sh', 'apply_complex_fixes.sh', 'fix_static_frameworks.sh'];
    
    scriptFiles.forEach(scriptFile => {
      const sourcePath = require('path').join(nodeModulesPath, scriptFile);
      const targetPath = require('path').join(iosDirectory, scriptFile);
      
      if (fs.existsSync(sourcePath)) {
        fs.copyFileSync(sourcePath, targetPath);
        fs.chmodSync(targetPath, '755'); // Make executable
        debugn('   Copied: ' + scriptFile);
      }
    });
    
    // Copy README for reference
    const readmePath = require('path').join(nodeModulesPath, 'README.md');
    const targetReadmePath = require('path').join(iosDirectory, 'static_frameworks_README.md');
    if (fs.existsSync(readmePath)) {
      fs.copyFileSync(readmePath, targetReadmePath);
      debugn('   Copied: README.md -> static_frameworks_README.md');
    }
  }

  _findIosDirectory() {
    // Look for iOS directory (should contain Podfile)
    const iosDir = glob.sync('**/ios', { ignore: ['node_modules/**', '**/build/**', '**/Build/**'] })[0];
    if (iosDir && fs.existsSync(require('path').join(iosDir, 'Podfile'))) {
      return iosDir;
    }
    
    // Fallback: if Podfile is in root, assume ios/ directory
    if (fs.existsSync('Podfile')) {
      return '.';
    }
    
    return null;
  }

  _findXcodeProject() {
    const xcodeProjects = glob.sync('**/project.pbxproj', this.ignoreFolders);
    if (xcodeProjects.length === 0) {
      return null;
    }
    
    // Find the main iOS project (not test projects)
    const mainProject = xcodeProjects.find(project => 
      !project.includes('Test') && !project.includes('test') && project.includes('ios/')
    ) || xcodeProjects[0];
    
    debugn('Found Xcode project: ' + mainProject);
    return mainProject;
  }

  _addStaticFrameworksBuildPhase() {
    if (!this.xcodeProjectPath) return;

    const projectContent = fs.readFileSync(this.xcodeProjectPath, 'utf8');
    
    // Check if our build phase already exists
    if (projectContent.includes('Static Frameworks Compatibility')) {
      debugn('   Static frameworks build phase already exists');
      return;
    }

    try {
      const xcode = require('xcode');
      const project = xcode.project(this.xcodeProjectPath);
      
      project.parseSync();
      
      // Find the main target
      const targets = project.getTargets();
      const mainTarget = targets.find(target => 
        !target.name.includes('Test') && !target.name.includes('test')
      );
      
      if (!mainTarget) {
        throw new Error('Could not find main target in Xcode project');
      }

      debugn('   Adding build phase to target: ' + mainTarget.name);

      // Add the build phase script
      const buildPhaseScript = `if [ "\\${USE_FRAMEWORKS}" = "static" ]; then
    echo "Applying static framework fixes..."
    "\\${SRCROOT}/fix_static_frameworks.sh"
else
    echo "Static framework fixes not needed (USE_FRAMEWORKS != static)"
fi`;

      project.addBuildPhase(
        [],  // files (empty for run script)
        'PBXShellScriptBuildPhase',
        'Static Frameworks Compatibility',
        mainTarget.uuid,
        {
          shellScript: buildPhaseScript,
          shellPath: '/bin/sh'
        }
      );

      // Write the modified project back
      fs.writeFileSync(this.xcodeProjectPath, project.writeSync());
      
      debugn('   Build phase added with shell script for static frameworks');
    } catch (error) {
      if (error.code === 'MODULE_NOT_FOUND' && error.message.includes('xcode')) {
        warnn('   xcode package not installed - skipping Xcode project modification');
        warnn('   Static frameworks automation will work via Podfile integration only');
      } else {
        throw error;
      }
    }
  }
}

module.exports = XcodeLinker;
