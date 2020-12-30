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

- (void)setMenu:(UIMenu *)menu {
    if ([_options.enableMenu getWithDefaultValue:YES]) {
        super.menu = menu;
    }
}

- (UIMenu *)menu {
    return super.menu;
}

@end
