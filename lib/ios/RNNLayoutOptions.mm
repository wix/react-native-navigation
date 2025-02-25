#import "RNNLayoutOptions.h"
#import "UIViewController+RNNOptions.h"
#import <React/RCTConvert.h>

@implementation RNNLayoutOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super initWithDict:dict];
    self.backgroundColor = [ColorParser parse:dict key:@"backgroundColor"];
    self.componentBackgroundColor = [ColorParser parse:dict key:@"componentBackgroundColor"];
    self.direction = [TextParser parse:dict key:@"direction"];
    self.orientation = dict[@"orientation"];
    self.autoHideHomeIndicator = [BoolParser parse:dict key:@"autoHideHomeIndicator"];
    self.insets = [[RNNInsetsOptions alloc] initWithDict:dict[@"insets"]];
    return self;
}

- (void)mergeOptions:(RNNLayoutOptions *)options {
    if (options.backgroundColor.hasValue)
        self.backgroundColor = options.backgroundColor;
    if (options.componentBackgroundColor.hasValue)
        self.componentBackgroundColor = options.componentBackgroundColor;
    if (options.direction.hasValue)
        self.direction = options.direction;
    if (options.orientation)
        self.orientation = options.orientation;
    if (options.autoHideHomeIndicator)
        self.autoHideHomeIndicator = options.autoHideHomeIndicator;
    if (options.insets.hasValue) {
        self.insets = options.insets;
    }
}

- (UIInterfaceOrientationMask)supportedOrientations {
    NSArray *orientationsArray = [self.orientation isKindOfClass:[NSString class]]
                                     ? @[ self.orientation ]
                                     : self.orientation;
    NSUInteger supportedOrientationsMask = 0;
    if (!orientationsArray || [self.orientation isEqual:@"default"]) {
        return [[UIApplication sharedApplication]
            supportedInterfaceOrientationsForWindow:[[UIApplication sharedApplication] keyWindow]];
    } else {
        for (NSString *orientation in orientationsArray) {
            if ([orientation isEqualToString:@"all"]) {
                supportedOrientationsMask = UIInterfaceOrientationMaskAll;
                break;
            }
            if ([orientation isEqualToString:@"landscape"]) {
                supportedOrientationsMask =
                    (supportedOrientationsMask | UIInterfaceOrientationMaskLandscape);
            }
            if ([orientation isEqualToString:@"portrait"]) {
                supportedOrientationsMask =
                    (supportedOrientationsMask | UIInterfaceOrientationMaskPortrait);
            }
            if ([orientation isEqualToString:@"upsideDown"]) {
                supportedOrientationsMask =
                    (supportedOrientationsMask | UIInterfaceOrientationMaskPortraitUpsideDown);
            }
        }
    }

    return supportedOrientationsMask;
}

@end
