#import "RNNStatusBarOptions.h"
#import "UIViewController+RNNOptions.h"

@implementation RNNStatusBarOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super initWithDict:dict];

    self.blur = [BoolParser parse:dict key:@"blur"];
    self.hideWithTopBar = [BoolParser parse:dict key:@"hideWithTopBar"];
    self.visible = [BoolParser parse:dict key:@"visible"];
    self.animate = [BoolParser parse:dict key:@"animate"];
    self.style = [TextParser parse:dict key:@"style"];

    return self;
}

- (void)mergeOptions:(RNNStatusBarOptions *)options {
    if (options.blur.hasValue)
        self.blur = options.blur;
    if (options.hideWithTopBar.hasValue)
        self.hideWithTopBar = options.hideWithTopBar;
    if (options.visible.hasValue)
        self.visible = options.visible;
    if (options.animate.hasValue)
        self.animate = options.animate;
    if (options.style.hasValue)
        self.style = options.style;
}

@end
