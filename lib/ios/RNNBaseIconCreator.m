#import "RNNBaseIconCreator.h"
#import "UIImage+utils.h"

@implementation RNNBaseIconCreator

- (instancetype)initWithIconDrawer:(RNNIconDrawer *)iconDrawer {
    self = [super init];
    self.iconDrawer = iconDrawer;
    return self;
}

- (UIImage *)create:(RNNButtonOptions *)buttonOptions {
    if (buttonOptions.isEnabled)
        return [self createEnabledIcon:buttonOptions];
    else
        return [self createDisabledIcon:buttonOptions];
}

- (UIImage *)createEnabledIcon:(RNNButtonOptions *)buttonOptions {
    UIColor *backgroundColor = [buttonOptions.iconBackground.color withDefault:UIColor.clearColor];
    UIColor *tintColor = buttonOptions.resolveColor;

    return [self createIcon:buttonOptions tintColor:tintColor backgroundColor:backgroundColor];
}

- (UIImage *)createDisabledIcon:(RNNButtonOptions *)buttonOptions {
    UIColor *backgroundColor = [self resolveDisabledBackgroundColor:buttonOptions];
    UIColor *tintColor = [self resolveDisabledIconColor:buttonOptions];

    return [self createIcon:buttonOptions tintColor:tintColor backgroundColor:backgroundColor];
}

- (UIColor *)resolveDisabledIconColor:(RNNButtonOptions *)buttonOptions {
    if (![buttonOptions.enabled withDefault:YES] && buttonOptions.disabledColor.hasValue)
        return buttonOptions.disabledColor.get;
    else
        return [buttonOptions.color withDefault:nil];
}

- (UIColor *)resolveDisabledBackgroundColor:(RNNButtonOptions *)buttonOptions {
    if (![buttonOptions.enabled withDefault:YES] &&
        buttonOptions.iconBackground.disabledColor.hasValue)
        return buttonOptions.iconBackground.disabledColor.get;
    else
        return [buttonOptions.iconBackground.color withDefault:nil];
}

- (UIImage *)createIcon:(RNNButtonOptions *)buttonOptions
              tintColor:(UIColor *)tintColor
        backgroundColor:(UIColor *)backgroundColor {
    @throw @"createIcon should be implemented by subclass";
    return nil;
}

- (CGSize)resolveIconSize:(RNNButtonOptions *)buttonOptions {
    CGFloat width =
        [buttonOptions.iconBackground.width withDefault:@(buttonOptions.icon.get.size.width)]
            .floatValue;
    CGFloat height =
        [buttonOptions.iconBackground.height withDefault:@(buttonOptions.icon.get.size.height)]
            .floatValue;
    return CGSizeMake(width, height);
}

@end
