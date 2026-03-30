#import "RNNDynamicIconCreator.h"

@implementation RNNDynamicIconCreator

- (UIImage *)createIcon:(RNNButtonOptions *)buttonOptions
              tintColor:(UIColor *)tintColor
        backgroundColor:(UIColor *)backgroundColor API_AVAILABLE(ios(13)) {
    UIImage *iconImage = buttonOptions.icon.get;
    CGSize iconSize = [self resolveIconSize:buttonOptions];
    CGFloat cornerRadius = [buttonOptions.iconBackground.cornerRadius withDefault:@(0)].floatValue;
    UIColor *lightColor = [tintColor
        resolvedColorWithTraitCollection:
            [UITraitCollection traitCollectionWithUserInterfaceStyle:UIUserInterfaceStyleLight]];
    UIColor *darkColor = [tintColor
        resolvedColorWithTraitCollection:
            [UITraitCollection traitCollectionWithUserInterfaceStyle:UIUserInterfaceStyleDark]];
    UIColor *lightBackgroundColor = [backgroundColor
        resolvedColorWithTraitCollection:
            [UITraitCollection traitCollectionWithUserInterfaceStyle:UIUserInterfaceStyleLight]];
    UIColor *darkBackgroundColor = [backgroundColor
        resolvedColorWithTraitCollection:
            [UITraitCollection traitCollectionWithUserInterfaceStyle:UIUserInterfaceStyleDark]];
    UIImage *darkImage =
        [[self.iconDrawer draw:iconImage
                    imageColor:darkColor
               backgroundColor:darkBackgroundColor
                          size:iconSize
                  cornerRadius:cornerRadius] imageWithInsets:buttonOptions.iconInsets.UIEdgeInsets];
    UIImage *lightImage =
        [[self.iconDrawer draw:iconImage
                    imageColor:lightColor
               backgroundColor:lightBackgroundColor
                          size:iconSize
                  cornerRadius:cornerRadius] imageWithInsets:buttonOptions.iconInsets.UIEdgeInsets];
    [lightImage.imageAsset
              registerImage:darkImage
        withTraitCollection:[UITraitCollection
                                traitCollectionWithUserInterfaceStyle:UIUserInterfaceStyleDark]];

    return lightImage;
}

@end
