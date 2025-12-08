require 'json'

package = JSON.parse(File.read(File.join(__dir__, 'package.json')))
fabric_enabled = ENV['RCT_NEW_ARCH_ENABLED'] == '1'

Pod::Spec.new do |s|
  s.name         = "ReactNativeNavigation"
  s.prepare_command = 'node autolink/postlink/__helpers__/generate_version_header.js'
  s.version      = package['version']
  s.summary      = package['description']

  s.authors      = "Wix.com"
  s.homepage     = package['homepage']
  s.license      = package['license']
  s.platform     = :ios, "11.0"

  s.module_name  = 'ReactNativeNavigation'
  s.default_subspec = 'Core'

  s.subspec 'Core' do |ss|
    s.source              = { :git => "https://github.com/wix/react-native-navigation.git", :tag => "#{s.version}" }
    s.source_files        = 'ios/**/*.{h,m,mm,cpp}'
    s.exclude_files       = "ios/ReactNativeNavigationTests/**/*.*", "ios/OCMock/**/*.*"
    
    s.public_header_files = [
      'ios/RNNAppDelegate.h',
      'ios/ReactNativeVersionExtracted.h'
    ]
  end

  folly_compiler_flags = '-DFOLLY_NO_CONFIG -DFOLLY_MOBILE=1 -DFOLLY_USE_LIBCPP=1 -Wno-comma -Wno-shorten-64-to-32 -DFOLLY_CFG_NO_COROUTINES=1'

  # Base xcconfig settings
  xcconfig_settings = {
      'HEADER_SEARCH_PATHS' => '"$(PODS_ROOT)/boost" "$(PODS_ROOT)/boost-for-react-native"  "$(PODS_ROOT)/RCT-Folly" "$(PODS_ROOT)/Headers/Private/React-Core" "$(PODS_ROOT)/Headers/Private/Yoga"',
      "CLANG_CXX_LANGUAGE_STANDARD" => "c++20",
      "OTHER_CPLUSPLUSFLAGS" => "-DFOLLY_NO_CONFIG -DFOLLY_MOBILE=1 -DFOLLY_USE_LIBCPP=1",
  }

  xcconfig_settings["DEFINES_MODULE"] = "YES"
  
  s.pod_target_xcconfig = xcconfig_settings

  if fabric_enabled
    install_modules_dependencies(s)

    s.compiler_flags  = folly_compiler_flags + ' ' + '-DRCT_NEW_ARCH_ENABLED' + ' ' + '-DUSE_HERMES=1'
    s.requires_arc    = true

    s.dependency "React"
    s.dependency "React-RCTFabric"
    s.dependency "React-cxxreact"
    s.dependency "React-Fabric"
    s.dependency "React-Codegen"
    s.dependency "RCT-Folly"
    s.dependency "RCTRequired"
    s.dependency "RCTTypeSafety"
    s.dependency "ReactCommon"
    s.dependency "React-runtimeexecutor"
    s.dependency "React-rncore"
    s.dependency "React-RuntimeCore"
  else
    s.compiler_flags  = folly_compiler_flags
  end

  s.dependency 'React-Core'
  s.dependency 'React-CoreModules'
  s.dependency 'React-RCTImage'
  s.dependency 'React-RCTText'
  s.dependency 'HMSegmentedControl'
  s.frameworks = 'UIKit'
end
