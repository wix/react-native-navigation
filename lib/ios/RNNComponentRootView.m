#import "RNNComponentRootView.h"

@implementation RNNComponentRootView

- (instancetype)init {
    self = [super init];
    self.sizeFlexibility = RCTRootViewSizeFlexibilityWidthAndHeight;
    return self;
}
- (NSString *)componentType {
    return ComponentTypeScreen;
}

@end
