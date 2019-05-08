#import "RNNLayoutOptions.h"
#import <React/RCTConvert.h>
#import "UIViewController+RNNOptions.h"

@implementation RNNLayoutOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
	self = [super init];
	
	self.backgroundColor = [ColorParser parse:dict key:@"backgroundColor"];
	self.direction = [TextParser parse:dict key:@"direction"];
	self.orientation = dict[@"orientation"];

	return self;
}

- (UIInterfaceOrientationMask)supportedOrientations {
	NSArray* orientationsArray = [self.orientation isKindOfClass:[NSString class]] ? @[self.orientation] : self.orientation;
	NSUInteger supportedOrientationsMask = 0;
	if (!orientationsArray || [self.orientation isEqual:@"default"]) {
		return [[UIApplication sharedApplication] supportedInterfaceOrientationsForWindow:[[UIApplication sharedApplication] keyWindow]];
	} else {
		for (NSString* orientation in orientationsArray) {
			if ([orientation isEqualToString:@"all"]) {
				supportedOrientationsMask = UIInterfaceOrientationMaskAll;
				break;
			}
			if ([orientation isEqualToString:@"landscape"]) {
				supportedOrientationsMask = (supportedOrientationsMask | UIInterfaceOrientationMaskLandscape);
			}
			if ([orientation isEqualToString:@"portrait"]) {
				supportedOrientationsMask = (supportedOrientationsMask | UIInterfaceOrientationMaskPortrait);
			}
			if ([orientation isEqualToString:@"upsideDown"]) {
				supportedOrientationsMask = (supportedOrientationsMask | UIInterfaceOrientationMaskPortraitUpsideDown);
			}
		}
	}

	return supportedOrientationsMask;
}


@end
