#import "TopBarAppearancePresenter.h"
#import "RNNFontAttributesCreator.h"
#import "UIViewController+LayoutProtocol.h"

@interface TopBarAppearancePresenter ()

@end

@implementation TopBarAppearancePresenter

- (void)applyOptions:(RNNTopBarOptions *)options {
    [self setTranslucent:[options.background.translucent getWithDefaultValue:NO]];
    [self setScrollEdgeTranslucent:[options.scrollEdgeAppearance.background.translucent
                                       getWithDefaultValue:[options.background.translucent
                                                               getWithDefaultValue:NO]]];
    [self setBackgroundColor:[options.background.color getWithDefaultValue:nil]];
    [self setScrollEdgeAppearanceColor:[options.scrollEdgeAppearance.background.color
                                           getWithDefaultValue:nil]];
    [self setTitleAttributes:options.title];
    [self setLargeTitleAttributes:options.largeTitle];
    [self showBorder:![options.noBorder getWithDefaultValue:NO
    ]:[options.borderColor getWithDefaultValue:nil]];
    [self setBackButtonOptions:options.backButton];
    if ([options.scrollEdgeAppearance.active getWithDefaultValue:NO]) {
        [self updateScrollEdgeAppearance];
    }
}

- (void)applyOptionsBeforePopping:(RNNTopBarOptions *)options {
}

- (void)setTranslucent:(BOOL)translucent {
    [super setTranslucent:translucent];
    [self updateBackgroundAppearance];
}

- (void)setScrollEdgeTranslucent:(BOOL)translucent {
    [super setScrollEdgeTranslucent:translucent];
}

- (void)setTransparent:(BOOL)transparent {
    [self updateBackgroundAppearance];
}

- (void)updateScrollEdgeAppearance {
    if (self.scrollEdgeTransparent) {
        [self.getScrollEdgeAppearance configureWithTransparentBackground];
    } else if (self.scrollEdgeAppearanceColor) {
        [self.getScrollEdgeAppearance configureWithOpaqueBackground];
        [self.getScrollEdgeAppearance setBackgroundColor:self.scrollEdgeAppearanceColor];
    } else if (self.scrollEdgeTranslucent) {
        [self.getScrollEdgeAppearance configureWithDefaultBackground];
    } else {
        [self.getScrollEdgeAppearance configureWithOpaqueBackground];
        if (self.backgroundColor) {
            [self.getScrollEdgeAppearance setBackgroundColor:self.backgroundColor];
        }
    }
}

- (void)updateBackgroundAppearance {
    if (self.transparent) {
        [self.getAppearance configureWithTransparentBackground];
        [self.getScrollEdgeAppearance configureWithTransparentBackground];
    } else if (self.backgroundColor) {
        [self.getAppearance configureWithOpaqueBackground];
        [self.getScrollEdgeAppearance configureWithOpaqueBackground];
        [self.getAppearance setBackgroundColor:self.backgroundColor];
        [self.getScrollEdgeAppearance setBackgroundColor:self.backgroundColor];
    } else if (self.translucent) {
        [self.getAppearance configureWithDefaultBackground];
        [self.getScrollEdgeAppearance configureWithDefaultBackground];
    } else {
        [self.getAppearance configureWithOpaqueBackground];
        [self.getScrollEdgeAppearance configureWithOpaqueBackground];
    }
}

- (void)showBorder:(BOOL)showBorder:(UIColor *)borderColor {
    UIColor *shadowColor =
        borderColor ? borderColor : [[UINavigationBarAppearance new] shadowColor];
    self.getAppearance.shadowColor = showBorder ? shadowColor : nil;
    self.getScrollEdgeAppearance.shadowColor = showBorder ? shadowColor : nil;
}

- (void)setBackIndicatorImage:(UIImage *)image withColor:(UIColor *)color {
    [self.getAppearance setBackIndicatorImage:image transitionMaskImage:image];
}

- (void)setTitleAttributes:(RNNTitleOptions *)titleOptions {
    NSString *fontFamily = [titleOptions.fontFamily getWithDefaultValue:nil];
    NSString *fontWeight = [titleOptions.fontWeight getWithDefaultValue:nil];
    NSNumber *fontSize = [titleOptions.fontSize getWithDefaultValue:nil];
    UIColor *fontColor = [titleOptions.color getWithDefaultValue:nil];

    self.getAppearance.titleTextAttributes =
        [RNNFontAttributesCreator createFromDictionary:self.getAppearance.titleTextAttributes
                                            fontFamily:fontFamily
                                              fontSize:fontSize
                                       defaultFontSize:nil
                                            fontWeight:fontWeight
                                                 color:fontColor
                                          defaultColor:nil];
}

- (void)setLargeTitleAttributes:(RNNLargeTitleOptions *)largeTitleOptions {
    NSString *fontFamily = [largeTitleOptions.fontFamily getWithDefaultValue:nil];
    NSString *fontWeight = [largeTitleOptions.fontWeight getWithDefaultValue:nil];
    NSNumber *fontSize = [largeTitleOptions.fontSize getWithDefaultValue:nil];
    UIColor *fontColor = [largeTitleOptions.color getWithDefaultValue:nil];
    NSDictionary *largeTitleTextAttributes =
        [RNNFontAttributesCreator createFromDictionary:self.getAppearance.largeTitleTextAttributes
                                            fontFamily:fontFamily
                                              fontSize:fontSize
                                       defaultFontSize:nil
                                            fontWeight:fontWeight
                                                 color:fontColor
                                          defaultColor:nil];
    self.getAppearance.largeTitleTextAttributes = largeTitleTextAttributes;
    self.getScrollEdgeAppearance.largeTitleTextAttributes = largeTitleTextAttributes;
}

- (UINavigationBarAppearance *)getAppearance {
    return self.currentNavigationItem.standardAppearance;
}

- (UINavigationBarAppearance *)getScrollEdgeAppearance {
    return self.currentNavigationItem.scrollEdgeAppearance;
}

@end
