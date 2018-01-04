#import "RNNTopTabOptions.h"
#import "RNNRootViewController.h"

@implementation RNNTopTabOptions

- (void)applyOn:(RNNRootViewController*)viewController {
	if (self.title) {
		[viewController.topTabsViewController viewController:viewController changedTitle:self.title];
	}
}

-(void)mergeWith:(NSDictionary *)otherOptions {
	for (id key in otherOptions) {
		[self setValue:[otherOptions objectForKey:key] forKey:key];
	}
}
@end

