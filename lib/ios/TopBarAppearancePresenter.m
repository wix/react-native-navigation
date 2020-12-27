#import "TopBarAppearancePresenter.h"
#import "RNNFontAttributesCreator.h"
#import "UIViewController+LayoutProtocol.h"

@interface TopBarAppearancePresenter ()

@end

@implementation TopBarAppearancePresenter

@synthesize borderColor = _borderColor;
@synthesize scrollEdgeBorderColor = _scrollEdgeBorderColor;

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
    [self setBorderColor:[options.borderColor getWithDefaultValue:nil]];
    [self showBorder:![options.noBorder getWithDefaultValue:NO]];
    [self setScrollEdgeBorderColor:[options.scrollEdgeAppearance.borderColor
                                       getWithDefaultValue:nil]];
    [self showScrollEdgeBorder:![options.scrollEdgeAppearance.noBorder getWithDefaultValue:NO]];
    [self setBackButtonOptions:options.backButton];
    if ([options.scrollEdgeAppearance.active getWithDefaultValue:NO]) {
        [self updateScrollEdgeAppearance];
    }
}

- (void)applyOptionsBeforePopping:(RNNTopBarOptions *)options {
}

- (void)mergeOptions:(RNNTopBarOptions *)options withDefault:(RNNTopBarOptions *)defaultOptions {
    [super mergeOptions:options withDefault:defaultOptions];

    if (options.borderColor.hasValue) {
        [self setBorderColor:options.borderColor.get];
    }

    if (options.scrollEdgeAppearance.borderColor.hasValue) {
        [self setScrollEdgeBorderColor:options.scrollEdgeAppearance.borderColor.get];
    }

    if (options.scrollEdgeAppearance.noBorder.hasValue) {
        [self showScrollEdgeBorder:options.scrollEdgeAppearance.noBorder.get];
    }
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

- (void)showBorder:(BOOL)showBorder {
    _showBorder = showBorder;
    [self updateBorder];
}

- (void)showScrollEdgeBorder:(BOOL)showScrollEdgeBorder {
    _showScrollEdgeBorder = showScrollEdgeBorder;
    [self updateScrollEdgeBorder];
}

- (void)setBorderColor:(UIColor *)borderColor {
    _borderColor = borderColor;
    [self updateBorder];
}

- (UIColor *)borderColor {
    return _borderColor ?: [[UINavigationBarAppearance new] shadowColor];
}

- (UIColor *)scrollEdgeBorderColor {
    return _scrollEdgeBorderColor ?: [[UINavigationBarAppearance new] shadowColor];
}

- (void)updateBorder {
    self.getAppearance.shadowColor = self.showBorder ? self.borderColor : nil;
}

- (void)updateScrollEdgeBorder {
    self.getScrollEdgeAppearance.shadowColor =
        self.scrollEdgeBorderColor ? self.scrollEdgeBorderColor : nil;
}

- (void)setBackIndicatorImage:(UIImage *)image withColor:(UIColor *)color {
    [self.getAppearance setBackIndicatorImage:image transitionMaskImage:image];
}

- (void)setTitleAttributes:(RNNTitleOptions *)titleOptions {
    NSString *fontFamily = [titleOptions.fontFamily getWithDefaultValue:nil];
    NSString *fontWeight = [titleOptions.fontWeight getWithDefaultValue:nil];
    NSNumber *fontSize = [titleOptions.fontSize getWithDefaultValue:nil];
    UIColor *fontColor = [titleOptions.color getWithDefaultValue:nil];

    NSDictionary *titleTextAttributes =
        [RNNFontAttributesCreator createFromDictionary:self.getAppearance.titleTextAttributes
                                            fontFamily:fontFamily
                                              fontSize:fontSize
                                            fontWeight:fontWeight
                                                 color:fontColor];

    self.getAppearance.titleTextAttributes = titleTextAttributes;
    self.getScrollEdgeAppearance.titleTextAttributes = titleTextAttributes;
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
                                            fontWeight:fontWeight
                                                 color:fontColor];
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
