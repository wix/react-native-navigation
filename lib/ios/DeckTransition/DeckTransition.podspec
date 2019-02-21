Pod::Spec.new do |spec|
  spec.name				= 'DeckTransition'
  spec.version          = '2.1.0'
  spec.summary          = 'An attempt to recreate the iOS 10 now playing transition'
  spec.description      = <<-DESC
						  DeckTransition is an attempt to recreate the iOS 10 Apple Music now playing and iMessage App Store transition.
						  DESC
  spec.homepage         = 'https://github.com/HarshilShah/DeckTransition'
  spec.license          = { :type => 'MIT', :file => 'LICENSE' }
  spec.author           = { 'Harshil Shah' => 'harshilshah1910@me.com' }
  spec.social_media_url = 'https://twitter.com/harshilshah1910'

  spec.source           = { :git => 'https://github.com/HarshilShah/DeckTransition.git', :tag => spec.version.to_s }
  spec.source_files     = 'Source/**/*.{h,swift}'

  spec.framework        = 'UIKit'
  spec.ios.deployment_target = '9.0'

end
