require 'json'

package = JSON.parse(File.read(File.join(__dir__, 'package.json')))

folly_compiler_flags = '-DFOLLY_NO_CONFIG -DFOLLY_MOBILE=1 -DFOLLY_USE_LIBCPP=1 -Wno-comma -Wno-shorten-64-to-32'
Pod::Spec.new do |s|
  s.name         = "ReactNativeNavigation"
  s.version      = package['version']
  s.summary      = package['description']

  s.authors      = "Wix.com"
  s.homepage     = package['homepage']
  s.license      = package['license']
  s.platform     = :ios, "9.0"

  s.module_name  = 'ReactNativeNavigation'

  s.source              = { :git => "https://github.com/wix/react-native-navigation.git", :tag => "#{s.version}" }
  s.source_files        = "lib/ios/**/*.{h,m,mm}"
  s.exclude_files       = "lib/ios/ReactNativeNavigationTests/**/*.*", "lib/ios/OCMock/**/*.*"
  s.compiler_flags      = folly_compiler_flags
  s.pod_target_xcconfig = {
    "HEADER_SEARCH_PATHS" => "\"$(PODS_ROOT)/boost-for-react-native\" \"$(PODS_ROOT)/Folly\"",
    "CLANG_CXX_LANGUAGE_STANDARD" => "c++14" 
  }

  s.dependency 'React'
  s.dependency 'React-RCTFabric'
  s.dependency 'React-Fabric'
  s.dependency 'Folly/Fabric'
  
  s.frameworks = 'UIKit'
end
