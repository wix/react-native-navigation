#import "RNNUIBarBackButtonItem.h"

@interface RNNUIBarBackButtonItem ()

@end

@implementation RNNUIBarBackButtonItem {
    RNNBackButtonOptions *_options;
}

- (instancetype)initWithOptions:(RNNBackButtonOptions *)options {
    self = [super init];
    _options = options;
    return self;
}

#if __IPHONE_OS_VERSION_MAX_ALLOWED >= 140000
- (void)setMenu:(UIMenu *)menu {
    if ([_options.enableMenu withDefault:YES]) {
        super.menu = menu;
    }
}

- (UIMenu *)menu {
    return super.menu;
}
#endif

@end
