#import "RNNTopTabOptions.h"

@implementation RNNTopTabOptions

//- (void)applyOn:(RNNRootViewController*)viewController {
//	if (self.title) {
////		[viewController.topTabsViewController viewController:viewController changedTitle:self.title];
//	}
//}

- (instancetype)initWithDict:(NSDictionary *)dict
{
    self.title = [TextParser parse: dict key:@"title"];
    self.titleFontFamily = [TextParser parse: dict key:@"fontFamily"];
    self.badge = [TextParser parse:dict key:@"badge"];
    self.badgeColor = [ColorParser parse:dict key:@"badgeColor"];
    return self;
}

@end

