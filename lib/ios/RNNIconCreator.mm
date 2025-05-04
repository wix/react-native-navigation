#import "RNNIconCreator.h"

@implementation RNNIconCreator

- (UIImage *)createIcon:(RNNButtonOptions *)buttonOptions
              tintColor:(UIColor *)tintColor
        backgroundColor:(UIColor *)backgroundColor {
    UIImage *iconImage = buttonOptions.icon.get;
    CGSize iconSize = [self resolveIconSize:buttonOptions];
    CGFloat cornerRadius = [buttonOptions.iconBackground.cornerRadius withDefault:@(0)].floatValue;
    return
        [[self.iconDrawer draw:iconImage
                    imageColor:tintColor
               backgroundColor:backgroundColor
                          size:iconSize
                  cornerRadius:cornerRadius] imageWithInsets:buttonOptions.iconInsets.UIEdgeInsets];
}

@end
