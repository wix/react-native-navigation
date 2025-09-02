// @ts-check
var path = require('./path');
var fs = require('fs');
var { logn, debugn, infon, errorn, warnn } = require('./log');

class PodfileLinker {
  constructor() {
    this.podfilePath = path.podFile;
  }

  link() {
    if (!this.podfilePath) {
      errorn(
        'Podfile not found! Does the file exist in the correct folder?\n   Please check the manual installation docs.'
      );
      return;
    }

    logn('Updating Podfile with static frameworks automation...');
    var podfileContent = fs.readFileSync(this.podfilePath, 'utf8');

    podfileContent = this._removeRNNPodLink(podfileContent);
    podfileContent = this._setMinimumIOSVersion(podfileContent);
    podfileContent = this._addStaticFrameworksSupport(podfileContent);

    fs.writeFileSync(this.podfilePath, podfileContent);
  }

  /**
   * Sets the minimum iOS version to iOS 11.0 which is the minimum version required by the library.
   */
  _setMinimumIOSVersion(contents) {
    const platformDefinition = contents.match(/platform :ios, '.*'/);
    const minimumIOSVersion = contents.match(/(?<=platform\s:ios,\s(?:"|'))(.*)(?=(?:"|'))/);

    if (parseFloat(minimumIOSVersion) < 11) {
      debugn('   Bump minumum iOS version to iOS 11.0');
      return contents.replace(platformDefinition, "platform :ios, '11.0'");
    }

    return contents;
  }

  /**
   * Removes the RNN pod added by react-native link script.
   */
  _removeRNNPodLink(contents) {
    const rnnPodLink = contents.match(/\s+.*pod 'ReactNativeNavigation'.+react-native-navigation'/);

    if (!rnnPodLink) {
      warnn('   RNN Pod has not been added to Podfile');
      return contents;
    }

    debugn('   Removing RNN Pod from Podfile');
    return contents.replace(rnnPodLink, '');
  }

  /**
   * Adds static frameworks automation support to the post_install hook.
   */
  _addStaticFrameworksSupport(contents) {
    // Check if static frameworks automation is already present
    if (contents.includes('Static Framework Compatibility')) {
      debugn('   Static frameworks automation already present in Podfile');
      return contents;
    }

    // Find existing post_install hook with proper nesting support
    const postInstallMatch = this._findPostInstallBlock(contents);

    if (postInstallMatch) {
      debugn('   Adding static frameworks automation to existing post_install hook');
      return this._addToExistingPostInstall(contents, postInstallMatch[0]);
    } else {
      debugn('   Adding new post_install hook with static frameworks automation');
      return this._addNewPostInstall(contents);
    }
  }

  _findPostInstallBlock(contents) {
    const lines = contents.split('\n');
    let postInstallStart = -1;
    let postInstallEnd = -1;
    let doEndCount = 0;

    for (let i = 0; i < lines.length; i++) {
      const line = lines[i].trim();

      // Find post_install start
      if (line.includes('post_install do |installer|')) {
        postInstallStart = i;
        doEndCount = 1; // We found one 'do'
        continue;
      }

      // If we're inside a post_install block
      if (postInstallStart !== -1) {
        // Count 'do' keywords (start of blocks)
        if (line.includes(' do ') || line.includes(' do|') || line.endsWith(' do')) {
          doEndCount++;
        }

        // Count 'end' keywords (end of blocks)
        if (line === 'end' || line.startsWith('end ')) {
          doEndCount--;

          // If we've balanced all do/end pairs, we found the post_install end
          if (doEndCount === 0) {
            postInstallEnd = i;
            break;
          }
        }
      }
    }

    if (postInstallStart !== -1 && postInstallEnd !== -1) {
      const postInstallLines = lines.slice(postInstallStart, postInstallEnd + 1);
      return [postInstallLines.join('\n')];
    }

    return null;
  }

  _addToExistingPostInstall(contents, existingPostInstall) {
    const staticFrameworksCode = `    
    # Static Framework Compatibility - Complete Automation (Added by React Native Navigation)
    if ENV['USE_FRAMEWORKS'] == 'static'
      Pod::UI.puts "Applying static framework compatibility fixes...".green
      
      # Apply source file fixes (after React Native processing)
      Pod::UI.puts "  Applying header guards..."
      system("#{__dir__}/apply_header_guards.sh")
      
      Pod::UI.puts "  Applying framework includes..."
      system("#{__dir__}/apply_framework_includes.sh")
      
      Pod::UI.puts "  Applying special fixes..."
      system("#{__dir__}/apply_special_fixes.sh")
      
      Pod::UI.puts "  Applying complex fixes..."
      system("#{__dir__}/apply_complex_fixes.sh")
      
      # Try to fix DerivedData if it exists
      Pod::UI.puts "  Checking for existing DerivedData..."
      if system("#{__dir__}/fix_static_frameworks.sh")
        Pod::UI.puts "✅ All static framework fixes applied successfully!".green
      else
        Pod::UI.puts "⚠️  DerivedData not found - will apply fixes during first build".yellow
        Pod::UI.puts "If build fails, run: cd ios && ./fix_static_frameworks.sh".yellow
      end
      
      # Add preprocessor macro for Objective-C code
      Pod::UI.puts "  Adding USE_FRAMEWORKS_STATIC preprocessor macro..."
      installer.pods_project.targets.each do |target|
        target.build_configurations.each do |config|
          config.build_settings['GCC_PREPROCESSOR_DEFINITIONS'] ||= []
          config.build_settings['GCC_PREPROCESSOR_DEFINITIONS'] << 'USE_FRAMEWORKS_STATIC=1'
        end
      end
    end`;

    // Insert before the final 'end' (handle indentation)
    const modifiedPostInstall = existingPostInstall.replace(/^(\s*)end(\s*)$/m, staticFrameworksCode + '\n$1end$2');
    return contents.replace(existingPostInstall, modifiedPostInstall);
  }

  _addNewPostInstall(contents) {
    const newPostInstallHook = `
post_install do |installer|
  # React Native post install
  react_native_post_install(
    installer,
    config[:reactNativePath],
    :mac_catalyst_enabled => false,
    # :ccache_enabled => true
  )
  
  # Static Framework Compatibility - Complete Automation (Added by React Native Navigation)
  if ENV['USE_FRAMEWORKS'] == 'static'
    installer.pods_project.targets.each do |target|
      target.build_configurations.each do |config|
        config.build_settings['GCC_PREPROCESSOR_DEFINITIONS'] ||= []
        config.build_settings['GCC_PREPROCESSOR_DEFINITIONS'] << 'USE_FRAMEWORKS_STATIC=1'
      end
    end
    
    Pod::UI.puts "Applying static framework compatibility fixes...".green
    
    # Apply source file fixes (after React Native processing)
    Pod::UI.puts "  Applying header guards..."
    system("#{__dir__}/apply_header_guards.sh")
    
    Pod::UI.puts "  Applying framework includes..."
    system("#{__dir__}/apply_framework_includes.sh")
    
    Pod::UI.puts "  Applying special fixes..."
    system("#{__dir__}/apply_special_fixes.sh")
    
    Pod::UI.puts "  Applying complex fixes..."
    system("#{__dir__}/apply_complex_fixes.sh")
    
    # Try to fix DerivedData if it exists
    Pod::UI.puts "  Checking for existing DerivedData..."
    if system("#{__dir__}/fix_static_frameworks.sh")
      Pod::UI.puts "✅ All static framework fixes applied successfully!".green
    else
      Pod::UI.puts "⚠️  DerivedData not found - will apply fixes during first build".yellow
      Pod::UI.puts "If build fails, run: cd ios && ./fix_static_frameworks.sh".yellow
    end
    
    # Add preprocessor macro for Objective-C code
    Pod::UI.puts "  Adding USE_FRAMEWORKS_STATIC preprocessor macro..."
    installer.pods_project.targets.each do |target|
      target.build_configurations.each do |config|
        config.build_settings['GCC_PREPROCESSOR_DEFINITIONS'] ||= []
        config.build_settings['GCC_PREPROCESSOR_DEFINITIONS'] << 'USE_FRAMEWORKS_STATIC=1'
      end
    end
  end
end`;

    // Add at the end of the file
    return contents + newPostInstallHook;
  }
}

module.exports = PodfileLinker;
