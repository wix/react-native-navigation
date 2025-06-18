#import "RNNSideMenuSideOptions.h"

@implementation RNNSideMenuSideOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super initWithDict:dict];

    self.visible = [BoolParser parse:dict key:@"visible"];
    self.enabled = [BoolParser parse:dict key:@"enabled"];
    self.width = [DoubleParser parse:dict key:@"width"];
    self.shouldStretchDrawer = [BoolParser parse:dict key:@"shouldStretchDrawer"];
    self.animationVelocity = [DoubleParser parse:dict key:@"animationVelocity"];
    self.openMode = [TextParser parse:dict key:@"openMode"];
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
    if (options.openMode.hasValue)
        self.openMode = options.openMode;
}

MMDrawerOpenMode MMDrawerOpenModeFromString(NSString *openModeString) {
    if ([openModeString isEqualToString:@"aboveContent"]) {
        return MMDrawerOpenModeAboveContent;
    }
    return MMDrawerOpenModePushContent;
}

@end
