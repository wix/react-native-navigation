#import "TopBarAppearancePresenter.h"
#import "RNNFontAttributesCreator.h"

@interface TopBarAppearancePresenter ()

@end


@implementation TopBarAppearancePresenter {
    UINavigationBarAppearance* _appearance;
}

- (instancetype)initWithNavigationController:(UINavigationController *)boundNavigationController {
    self = [super initWithNavigationController:boundNavigationController];
    _appearance = boundNavigationController.navigationBar.standardAppearance ?: [UINavigationBarAppearance new];
    boundNavigationController.navigationBar.standardAppearance = _appearance;
    return self;
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
        [_appearance configureWithTransparentBackground];
    } else if (self.backgroundColor) {
        [_appearance setBackgroundColor:self.backgroundColor];
    } else if (self.translucent) {
        [_appearance configureWithDefaultBackground];
    } else {
        [_appearance configureWithOpaqueBackground];
    }
}

- (void)showBorder:(BOOL)showBorder {
    UIColor* shadowColor = showBorder ? [[UINavigationBarAppearance new] shadowColor] : nil;
    _appearance.shadowColor = shadowColor;
}

- (void)setBackIndicatorImage:(UIImage *)image withColor:(UIColor *)color {
    [_appearance setBackIndicatorImage:image transitionMaskImage:image];
}

- (void)setTitleAttributes:(RNNTitleOptions *)titleOptions {
    NSString* fontFamily = [titleOptions.fontFamily getWithDefaultValue:nil];
    NSString* fontWeight = [titleOptions.fontWeight getWithDefaultValue:nil];
    NSNumber* fontSize = [titleOptions.fontSize getWithDefaultValue:nil];
    UIColor* fontColor = [titleOptions.color getWithDefaultValue:nil];
    
    _appearance.titleTextAttributes = [RNNFontAttributesCreator createFromDictionary:_appearance.titleTextAttributes fontFamily:fontFamily fontSize:fontSize defaultFontSize:nil fontWeight:fontWeight color:fontColor defaultColor:nil];
}

- (void)setLargeTitleAttributes:(RNNLargeTitleOptions *)largeTitleOptions {
    NSString* fontFamily = [largeTitleOptions.fontFamily getWithDefaultValue:nil];
    NSString* fontWeight = [largeTitleOptions.fontWeight getWithDefaultValue:nil];
    NSNumber* fontSize = [largeTitleOptions.fontSize getWithDefaultValue:nil];
    UIColor* fontColor = [largeTitleOptions.color getWithDefaultValue:nil];
    
    _appearance.largeTitleTextAttributes = [RNNFontAttributesCreator createFromDictionary:_appearance.largeTitleTextAttributes fontFamily:fontFamily fontSize:fontSize defaultFontSize:nil fontWeight:fontWeight color:fontColor defaultColor:nil];
}

@end
