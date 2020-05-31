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
    }  else {
        [self.getAppearance configureWithOpaqueBackground];
        [self.getScrollEdgeAppearance configureWithOpaqueBackground];
    }
}

- (void)showBorder:(BOOL)showBorder {
    UIColor* shadowColor = showBorder ? [[UINavigationBarAppearance new] shadowColor] : nil;
    self.getAppearance.shadowColor = shadowColor;
}

- (void)setBackIndicatorImage:(UIImage *)image withColor:(UIColor *)color {
    [self.getAppearance setBackIndicatorImage:image transitionMaskImage:image];
}

- (void)setTitleAttributes:(RNNTitleOptions *)titleOptions {
    NSString* fontFamily = [titleOptions.fontFamily getWithDefaultValue:nil];
    NSString* fontWeight = [titleOptions.fontWeight getWithDefaultValue:nil];
    NSNumber* fontSize = [titleOptions.fontSize getWithDefaultValue:nil];
    UIColor* fontColor = [titleOptions.color getWithDefaultValue:nil];
    
    self.getAppearance.titleTextAttributes = [RNNFontAttributesCreator createFromDictionary:self.getAppearance.titleTextAttributes fontFamily:fontFamily fontSize:fontSize defaultFontSize:nil fontWeight:fontWeight color:fontColor defaultColor:nil];
}

- (void)setLargeTitleAttributes:(RNNLargeTitleOptions *)largeTitleOptions {
    NSString* fontFamily = [largeTitleOptions.fontFamily getWithDefaultValue:nil];
    NSString* fontWeight = [largeTitleOptions.fontWeight getWithDefaultValue:nil];
    NSNumber* fontSize = [largeTitleOptions.fontSize getWithDefaultValue:nil];
    UIColor* fontColor = [largeTitleOptions.color getWithDefaultValue:nil];
    
    self.getAppearance.largeTitleTextAttributes = [RNNFontAttributesCreator createFromDictionary:self.getAppearance.largeTitleTextAttributes fontFamily:fontFamily fontSize:fontSize defaultFontSize:nil fontWeight:fontWeight color:fontColor defaultColor:nil];
}

- (UINavigationBarAppearance *)getAppearance {
    return self.currentNavigationItem.standardAppearance;
}

- (UINavigationBarAppearance *)getScrollEdgeAppearance {
    return self.currentNavigationItem.scrollEdgeAppearance;
}

@end
