#import "RNNSideMenuSideOptions.h"

@implementation RNNSideMenuSideOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super initWithDict:dict];

    self.visible = [RNNBoolParser parse:dict key:@"visible"];
    self.enabled = [RNNBoolParser parse:dict key:@"enabled"];
    self.width = [RNNDoubleParser parse:dict key:@"width"];
    self.shouldStretchDrawer = [RNNBoolParser parse:dict key:@"shouldStretchDrawer"];
    self.animationVelocity = [RNNDoubleParser parse:dict key:@"animationVelocity"];

    return self;
}

- (void)mergeOptions:(RNNSideMenuSideOptions *)options {
    if (options.visible.hasValue)
        self.visible = options.visible;
    if (options.enabled.hasValue)
        self.enabled = options.enabled;
    if (options.width.hasValue)
        self.width = options.width;
    if (options.shouldStretchDrawer.hasValue)
        self.shouldStretchDrawer = options.shouldStretchDrawer;
    if (options.animationVelocity.hasValue)
        self.animationVelocity = options.animationVelocity;
}

@end
