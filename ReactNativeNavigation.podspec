require 'json'

package = JSON.parse(File.read(File.join(__dir__, 'package.json')))

fabric_enabled = ENV['RCT_NEW_ARCH_ENABLED'] == '1'

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
    s.source_files    = 'lib/ios/**/*.{h,m,mm,cpp}'
    s.exclude_files       = "lib/ios/ReactNativeNavigationTests/**/*.*", "lib/ios/OCMock/**/*.*"
  end

  if fabric_enabled
    folly_compiler_flags = '-DFOLLY_NO_CONFIG -DFOLLY_MOBILE=1 -DFOLLY_USE_LIBCPP=1 -Wno-comma -Wno-shorten-64-to-32'
    fabric_flags = fabric_enabled ? '-DRCT_NEW_ARCH_ENABLED' : ''
    s.pod_target_xcconfig = {
    'HEADER_SEARCH_PATHS' => '"$(PODS_ROOT)/boost" "$(PODS_ROOT)/boost-for-react-native"  "$(PODS_ROOT)/RCT-Folly" "$(PODS_ROOT)/Headers/Private/React-Core"',
      "CLANG_CXX_LANGUAGE_STANDARD" => "c++17",
    }
    s.compiler_flags  = folly_compiler_flags + ' ' + '-DRCT_NEW_ARCH_ENABLED'
    s.requires_arc    = true

    s.dependency "React"
    s.dependency "React-RCTFabric"
    s.dependency "React-cxxreact"
    s.dependency "React-Fabric"
    s.dependency "React-Codegen"
    s.dependency "RCT-Folly"
    s.dependency "RCTRequired"
    s.dependency "RCTTypeSafety"
    s.dependency "ReactCommon/turbomodule/core"
    s.dependency "React-runtimeexecutor"
    s.dependency "React-rncore"
  end
  s.dependency 'React-Core'
  s.dependency 'React-CoreModules'
  s.dependency 'React-RCTImage'
  s.dependency 'React-RCTText'
  s.dependency 'HMSegmentedControl'
  s.frameworks = 'UIKit'
end
