#import "RNNSideMenuOptions.h"
#import "SideMenuOpenGestureModeParser.h"

@implementation RNNSideMenuOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super initWithDict:dict];

    self.left = [[RNNSideMenuSideOptions alloc] initWithDict:dict[@"left"]];
    self.right = [[RNNSideMenuSideOptions alloc] initWithDict:dict[@"right"]];
    self.animationType = [TextParser parse:dict key:@"animationType"];
    self.openGestureMode = [SideMenuOpenGestureModeParser parse:dict key:@"openGestureMode"];

    return self;
}

- (void)mergeOptions:(RNNSideMenuOptions *)options {
    [self.left mergeOptions:options.left];
    [self.right mergeOptions:options.right];

    if (options.animationType.hasValue)
        self.animationType = options.animationType;
    if (options.openGestureMode.hasValue)
        self.openGestureMode = options.openGestureMode;
}

@end
