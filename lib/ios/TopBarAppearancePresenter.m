#import "TopBarAppearancePresenter.h"
#import "RNNFontAttributesCreator.h"

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
        [self.currentChildAppearance configureWithTransparentBackground];
    } else if (self.backgroundColor) {
        [self.currentChildAppearance setBackgroundColor:self.backgroundColor];
    } else if (self.translucent) {
        [self.currentChildAppearance configureWithDefaultBackground];
    } else {
        [self.currentChildAppearance configureWithOpaqueBackground];
    }
}

- (void)showBorder:(BOOL)showBorder {
    UIColor* shadowColor = showBorder ? [[UINavigationBarAppearance new] shadowColor] : nil;
    self.currentChildAppearance.shadowColor = shadowColor;
}

- (void)setBackIndicatorImage:(UIImage *)image withColor:(UIColor *)color {
    [self.currentChildAppearance setBackIndicatorImage:image transitionMaskImage:image];
}

- (void)setTitleAttributes:(RNNTitleOptions *)titleOptions {
    NSString* fontFamily = [titleOptions.fontFamily getWithDefaultValue:nil];
    NSString* fontWeight = [titleOptions.fontWeight getWithDefaultValue:nil];
    NSNumber* fontSize = [titleOptions.fontSize getWithDefaultValue:nil];
    UIColor* fontColor = [titleOptions.color getWithDefaultValue:nil];
    
    self.currentChildAppearance.titleTextAttributes = [RNNFontAttributesCreator createFromDictionary:self.currentChildAppearance.titleTextAttributes fontFamily:fontFamily fontSize:fontSize defaultFontSize:nil fontWeight:fontWeight color:fontColor defaultColor:nil];
}

- (void)setLargeTitleAttributes:(RNNLargeTitleOptions *)largeTitleOptions {
    NSString* fontFamily = [largeTitleOptions.fontFamily getWithDefaultValue:nil];
    NSString* fontWeight = [largeTitleOptions.fontWeight getWithDefaultValue:nil];
    NSNumber* fontSize = [largeTitleOptions.fontSize getWithDefaultValue:nil];
    UIColor* fontColor = [largeTitleOptions.color getWithDefaultValue:nil];
    
    self.currentChildAppearance.largeTitleTextAttributes = [RNNFontAttributesCreator createFromDictionary:self.currentChildAppearance.largeTitleTextAttributes fontFamily:fontFamily fontSize:fontSize defaultFontSize:nil fontWeight:fontWeight color:fontColor defaultColor:nil];
}

- (UINavigationBarAppearance *)currentChildAppearance {
    if (!self.boundViewController.childViewControllers.lastObject.navigationItem.standardAppearance) {
        self.boundViewController.childViewControllers.lastObject.navigationItem.standardAppearance = [UINavigationBarAppearance new];
    }
    
    return self.boundViewController.childViewControllers.lastObject.navigationItem.standardAppearance;
}

@end
