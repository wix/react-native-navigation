#import "RNNIconCreator.h"
#import "UIImage+utils.h"

@implementation RNNIconCreator {
    RNNIconDrawer *_iconDrawer;
}

- (instancetype)initWithIconDrawer:(RNNIconDrawer *)iconDrawer {
    self = [super init];
    _iconDrawer = iconDrawer;
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
    UIColor *tintColor = [buttonOptions.color withDefault:nil];

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
    UIImage *iconImage = buttonOptions.icon.get;
    CGSize iconSize = [self resolveIconSize:buttonOptions];
    CGFloat cornerRadius = [buttonOptions.iconBackground.cornerRadius withDefault:@(0)].floatValue;

    return [[_iconDrawer draw:iconImage
                   imageColor:tintColor
              backgroundColor:backgroundColor
                         size:iconSize
                 cornerRadius:cornerRadius] imageWithInsets:buttonOptions.iconInsets.UIEdgeInsets];
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
