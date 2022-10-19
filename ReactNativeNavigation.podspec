require 'json'

package = JSON.parse(File.read(File.join(__dir__, 'package.json')))

Pod::Spec.new do |s|
  s.name         = "ReactNativeNavigation"
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
    # s.source_files        = "lib/ios/**/*.{h,m,mm}"
    s.exclude_files       = "lib/ios/ReactNativeNavigationTests/**/*.*", "lib/ios/OCMock/**/*.*"
  end
  
  reactJson = JSON.parse(File.read(File.join(__dir__, "node_modules", "react-native", "package.json")))
  reactVersion = reactJson["version"]
  rnVersion = reactVersion.split('.')[1]

  fabric_enabled = ENV['RCT_NEW_ARCH_ENABLED'] == '1'

  folly_prefix = ""
  if rnVersion.to_i >= 64
    folly_prefix = "RCT-"
  end

  folly_flags = '-DFOLLY_NO_CONFIG -DFOLLY_MOBILE=1 -DFOLLY_USE_LIBCPP=1 -Wno-comma -Wno-shorten-64-to-32 -DRNVERSION=' + rnVersion
  folly_compiler_flags = folly_flags + ' ' + '-Wno-comma -Wno-shorten-64-to-32'
  boost_compiler_flags = '-Wno-documentation'
  fabric_flags = ''
  if fabric_enabled
    fabric_flags = '-DRN_FABRIC_ENABLED -DRCT_NEW_ARCH_ENABLED'
  end

  s.source_files = [
    "lib/ios/**/*.{mm,h,m}",
    "Common/cpp/**/*.cpp",
    "Common/cpp/headers/**/*.h"
  ]

  s.preserve_paths = [
    "Common/cpp/hidden_headers/**"
  ]

  s.pod_target_xcconfig    = {
    "USE_HEADERMAP" => "YES",
    "HEADER_SEARCH_PATHS" => "\"$(PODS_TARGET_SRCROOT)/ReactCommon\" \"$(PODS_TARGET_SRCROOT)\" \"$(PODS_ROOT)/#{folly_prefix}Folly\" \"$(PODS_ROOT)/boost\" \"$(PODS_ROOT)/boost-for-react-native\" \"$(PODS_ROOT)/DoubleConversion\" \"$(PODS_ROOT)/Headers/Private/React-Core\"",
    "CLANG_CXX_LANGUAGE_STANDARD" => "c++17",
  }

  s.compiler_flags = folly_compiler_flags + ' ' + boost_compiler_flags
  s.xcconfig               = {
    "HEADER_SEARCH_PATHS" => "\"$(PODS_ROOT)/boost\" \"$(PODS_ROOT)/boost-for-react-native\" \"$(PODS_ROOT)/glog\" \"$(PODS_ROOT)/#{folly_prefix}Folly\" \"$(PODS_ROOT)/RCT-Folly\" \"${PODS_ROOT}/Headers/Public/React-hermes\" \"${PODS_ROOT}/Headers/Public/hermes-engine\"",
                               "OTHER_CFLAGS" => "$(inherited)" + " " + folly_flags + " " + fabric_flags }

  s.dependency "React-RCTFabric"
  s.dependency "React-Codegen"
  s.dependency "RCT-Folly"


  s.dependency 'React-Core'
  s.dependency 'React-RCTImage'
  s.dependency 'React-RCTText'
  s.dependency 'HMSegmentedControl'
  s.frameworks = 'UIKit'
end
