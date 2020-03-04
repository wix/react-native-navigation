#import "TopBarAppearancePresenter.h"
#import "RNNFontAttributesCreator.h"
#import "UIViewController+LayoutProtocol.h"

@interface TopBarAppearancePresenter ()

@end


@implementation TopBarAppearancePresenter

- (void)applyOptions:(RNNTopBarOptions *)options {
    [self setTranslucent:[options.background.translucent getWithDefaultValue:NO]];
    [self setBackgroundColor:[options.background.color getWithDefaultValue:nil]];
    [self setTitleAttributes:options.title];
    [self setLargeTitleAttributes:options.largeTitle];
    [self showBorder:![options.noBorder getWithDefaultValue:NO]];
    [self setBackButtonOptions:options.backButton];
}

- (void)applyOptionsBeforePopping:(RNNTopBarOptions *)options {
    [self setBackgroundColor:[options.background.color getWithDefaultValue:nil]];
}

- (void)setTranslucent:(BOOL)translucent {
    [super setTranslucent:translucent];
    [self updateBackgroundAppearance];
}

- (void)setTransparent:(BOOL)transparent {
    [self updateBackgroundAppearance];
}

- (void)updateBackgroundAppearance {
    if (self.transparent) {
        [self.getCurrentNavigationItem configureWithTransparentBackground];
    } else if (self.backgroundColor) {
        [self.getCurrentNavigationItem setBackgroundColor:self.backgroundColor];
    } else if (self.translucent) {
        [self.getCurrentNavigationItem configureWithDefaultBackground];
    } else {
        [self.getCurrentNavigationItem configureWithOpaqueBackground];
    }
}

- (void)showBorder:(BOOL)showBorder {
    UIColor* shadowColor = showBorder ? [[UINavigationBarAppearance new] shadowColor] : nil;
    self.getCurrentNavigationItem.shadowColor = shadowColor;
}

- (void)setBackIndicatorImage:(UIImage *)image withColor:(UIColor *)color {
    [self.getCurrentNavigationItem setBackIndicatorImage:image transitionMaskImage:image];
}

- (void)setTitleAttributes:(RNNTitleOptions *)titleOptions {
    NSString* fontFamily = [titleOptions.fontFamily getWithDefaultValue:nil];
    NSString* fontWeight = [titleOptions.fontWeight getWithDefaultValue:nil];
    NSNumber* fontSize = [titleOptions.fontSize getWithDefaultValue:nil];
    UIColor* fontColor = [titleOptions.color getWithDefaultValue:nil];
    
    self.getCurrentNavigationItem.titleTextAttributes = [RNNFontAttributesCreator createFromDictionary:self.getCurrentNavigationItem.titleTextAttributes fontFamily:fontFamily fontSize:fontSize defaultFontSize:nil fontWeight:fontWeight color:fontColor defaultColor:nil];
}

- (void)setLargeTitleAttributes:(RNNLargeTitleOptions *)largeTitleOptions {
    NSString* fontFamily = [largeTitleOptions.fontFamily getWithDefaultValue:nil];
    NSString* fontWeight = [largeTitleOptions.fontWeight getWithDefaultValue:nil];
    NSNumber* fontSize = [largeTitleOptions.fontSize getWithDefaultValue:nil];
    UIColor* fontColor = [largeTitleOptions.color getWithDefaultValue:nil];
    
    self.getCurrentNavigationItem.largeTitleTextAttributes = [RNNFontAttributesCreator createFromDictionary:self.getCurrentNavigationItem.largeTitleTextAttributes fontFamily:fontFamily fontSize:fontSize defaultFontSize:nil fontWeight:fontWeight color:fontColor defaultColor:nil];
}

- (UINavigationBarAppearance *)getCurrentNavigationItem {
    return self.currentNavigationItem.standardAppearance;
}

@end
