require 'json'

package = JSON.parse(File.read(File.join(__dir__, 'package.json')))

folly_compiler_flags = folly_flags()
use_hermes = ENV['USE_HERMES'] == nil || ENV['USE_HERMES'] == '1'
use_hermes_flag = use_hermes ? "-DUSE_HERMES=1" : ""

Pod::Spec.new do |s|
  s.name         = "ReactNativeNavigation"
  s.prepare_command = 'node autolink/postlink/__helpers__/generate_version_header.js'
  s.version      = package['version']
  s.summary      = package['description']

  s.authors      = "Wix.com"
  s.homepage     = package['homepage']
  s.license      = package['license']
  s.platform     = :ios, min_ios_version_supported

  s.module_name  = 'ReactNativeNavigation'

  s.source       = { :git => "https://github.com/wix/react-native-navigation.git", :tag => "#{s.version}" }
  s.source_files = 'ios/**/*.{h,m,mm,cpp}'
  s.exclude_files = "ios/ReactNativeNavigationTests/**/*.*", "ios/OCMock/**/*.*"

  s.public_header_files = [
    'ios/RNNAppDelegate.h',
    'ios/ReactNativeVersionExtracted.h'
  ]

  # Add Folly compiler flags to prevent coroutines header issues
  # Add Hermes flag when using Hermes engine
  s.compiler_flags = "#{folly_compiler_flags} #{use_hermes_flag}"

  # Add header search paths for React-Core private headers (needed for RCTCxxBridgeDelegate.h, etc.)
  s.pod_target_xcconfig = {
    'HEADER_SEARCH_PATHS' => '"$(PODS_ROOT)/Headers/Private/React-Core"',
    'DEFINES_MODULE' => 'YES'
  }

  # Let install_modules_dependencies handle all React Native dependencies
  install_modules_dependencies(s)

  s.requires_arc = true
  s.dependency 'HMSegmentedControl'
  s.frameworks = 'UIKit'
end
