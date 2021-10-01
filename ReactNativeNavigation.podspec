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
    s.source_files        = "lib/ios/**/*.{h,m,mm}"
    s.exclude_files       = "lib/ios/ReactNativeNavigationTests/**/*.*", "lib/ios/OCMock/**/*.*"
  end
  
  s.subspec 'Fabric' do |ss|
    ss.xcconfig = { "HEADER_SEARCH_PATHS" => "\"$(PODS_ROOT)/boost-for-react-native\" \"$(PODS_ROOT)/RCT-Folly\"",
                    "OTHER_CFLAGS" => "$(inherited) -DRN_FABRIC_ENABLED -DFOLLY_NO_CONFIG -DFOLLY_MOBILE=1 -DFOLLY_USE_LIBCPP=1" }
    ss.dependency 'React-RCTFabric'
    ss.dependency 'React-Fabric'
    ss.dependency 'RCT-Folly/Fabric'
  end

  s.dependency 'React-Core'
  s.dependency 'React-RCTImage'
  s.dependency 'React-RCTText'
  s.dependency 'HMSegmentedControl'
  s.frameworks = 'UIKit'
end
