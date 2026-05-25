#import "TopBarAppearancePresenter.h"
#import "RNNUIBarButtonItem.h"
#import "RNNReactButtonView.h"
#import "RNNReactTitleView.h"
#import "RNNFontAttributesCreator.h"
#import "UIColor+RNNUtils.h"
#import "UIImage+utils.h"
#import "UIViewController+LayoutProtocol.h"

@interface TopBarAppearancePresenter ()

- (void)applyBackgroundToNavigationItem:(UINavigationItem *)item topBarOptions:(RNNTopBarOptions *)options;
- (void)applyBackgroundToAppearance:(UINavigationBarAppearance *)appearance
                   withOpaqueColor:(UIColor *)color
                        transparent:(BOOL)transparent
                        translucent:(BOOL)translucent;
@end

static void RNNApplyIOS26SolidColorToAppearance(UINavigationBarAppearance *appearance, UIColor *color) {
    if (appearance.backgroundEffect == nil && appearance.backgroundImage != nil && color &&
        CGColorEqualToColor(appearance.backgroundColor.CGColor, color.CGColor)) {
        return;
    }
    appearance.backgroundEffect = nil;
    appearance.shadowColor = nil;
    appearance.backgroundColor = color;
    appearance.backgroundImage = [UIImage imageWithSize:CGSizeMake(1, 1) color:color];
}

@implementation TopBarAppearancePresenter

@synthesize borderColor = _borderColor;
@synthesize scrollEdgeBorderColor = _scrollEdgeBorderColor;

+ (void)applyBackgroundToNavigationItem:(UINavigationItem *)item
                                  color:(UIColor *)color
                             transparent:(BOOL)transparent
                              translucent:(BOOL)translucent {
    if (!item.standardAppearance) {
        item.standardAppearance = [UINavigationBarAppearance new];
    }
    if (!item.scrollEdgeAppearance) {
        item.scrollEdgeAppearance = [UINavigationBarAppearance new];
    }

    if (@available(iOS 26.0, *)) {
        if (transparent || color.isTransparent) {
            [item.standardAppearance configureWithTransparentBackground];
            item.standardAppearance.backgroundEffect = nil;
            item.standardAppearance.backgroundColor = UIColor.clearColor;
            item.standardAppearance.backgroundImage = nil;
            [item.scrollEdgeAppearance configureWithTransparentBackground];
            item.scrollEdgeAppearance.backgroundEffect = nil;
            item.scrollEdgeAppearance.backgroundColor = UIColor.clearColor;
            item.scrollEdgeAppearance.backgroundImage = nil;
            return;
        }
        if (color) {
            RNNApplyIOS26SolidColorToAppearance(item.standardAppearance, color);
            RNNApplyIOS26SolidColorToAppearance(item.scrollEdgeAppearance, color);
            return;
        }
    }

    if (transparent || color.isTransparent) {
        [item.standardAppearance configureWithTransparentBackground];
        item.standardAppearance.backgroundColor = UIColor.clearColor;
        [item.scrollEdgeAppearance configureWithTransparentBackground];
        item.scrollEdgeAppearance.backgroundColor = UIColor.clearColor;
        return;
    }
    if (color) {
        [item.standardAppearance configureWithOpaqueBackground];
        item.standardAppearance.backgroundColor = color;
        [item.scrollEdgeAppearance configureWithOpaqueBackground];
        item.scrollEdgeAppearance.backgroundColor = color;
        return;
    }
    if (translucent) {
        [item.standardAppearance configureWithDefaultBackground];
        [item.scrollEdgeAppearance configureWithDefaultBackground];
    } else {
        [item.standardAppearance configureWithOpaqueBackground];
        [item.scrollEdgeAppearance configureWithOpaqueBackground];
    }
}

- (void)applyBackgroundToNavigationItem:(UINavigationItem *)item topBarOptions:(RNNTopBarOptions *)options {
    UIColor *color = [options.background.color withDefault:nil];
    BOOL transparent = color.isTransparent;
    BOOL translucent = [options.background.translucent withDefault:NO];

    [TopBarAppearancePresenter applyBackgroundToNavigationItem:item
                                                           color:color
                                                      transparent:transparent
                                                       translucent:translucent];

    if ([options.scrollEdgeAppearance.active withDefault:NO]) {
        UIColor *scrollEdgeColor = [options.scrollEdgeAppearance.background.color withDefault:nil];
        BOOL scrollEdgeTransparent = scrollEdgeColor.isTransparent;
        BOOL scrollEdgeTranslucent =
            [options.scrollEdgeAppearance.background.translucent withDefault:translucent];
        UIColor *resolvedScrollEdgeColor = scrollEdgeColor ?: color;
        if (@available(iOS 26.0, *)) {
            if (scrollEdgeTransparent || resolvedScrollEdgeColor.isTransparent) {
                [item.scrollEdgeAppearance configureWithTransparentBackground];
                item.scrollEdgeAppearance.backgroundEffect = nil;
                item.scrollEdgeAppearance.backgroundColor = UIColor.clearColor;
                item.scrollEdgeAppearance.backgroundImage = nil;
            } else if (resolvedScrollEdgeColor) {
                RNNApplyIOS26SolidColorToAppearance(item.scrollEdgeAppearance, resolvedScrollEdgeColor);
            }
        } else {
            [self applyBackgroundToAppearance:item.scrollEdgeAppearance
                                withOpaqueColor:resolvedScrollEdgeColor
                                     transparent:scrollEdgeTransparent
                                      translucent:scrollEdgeTranslucent];
        }
    }
}

- (void)applyTransitionBackgroundToNavigationItem:(UINavigationItem *)item
                                      topBarOptions:(RNNTopBarOptions *)options {
    [self applyBackgroundToNavigationItem:item topBarOptions:options];
    if (@available(iOS 26.0, *)) {
        self.navigationController.navigationBar.translucent = NO;
    }
}

- (void)applyBackgroundForTransitionToViewController:(UIViewController *)viewController
                                       topBarOptions:(RNNTopBarOptions *)options {
    if (@available(iOS 26.0, *)) {
        [self applyTransitionBackgroundToNavigationItem:viewController.navigationItem
                                          topBarOptions:options];
    }
}

+ (void)suppressSharedBackgroundForNavigationItem:(UINavigationItem *)item {
    if (@available(iOS 26.0, *)) {
        void (^suppress)(NSArray<UIBarButtonItem *> *) = ^(NSArray<UIBarButtonItem *> *items) {
            for (UIBarButtonItem *barButton in items) {
                barButton.hidesSharedBackground = YES;
                barButton.sharesBackground = NO;
            }
        };
        suppress(item.leftBarButtonItems);
        suppress(item.rightBarButtonItems);
    }
}

+ (void)applySharedBackgroundSettingsForNavigationItem:(UINavigationItem *)item {
    if (@available(iOS 26.0, *)) {
        void (^apply)(NSArray<UIBarButtonItem *> *) = ^(NSArray<UIBarButtonItem *> *items) {
            for (UIBarButtonItem *barButton in items) {
                if ([barButton isKindOfClass:[RNNUIBarButtonItem class]]) {
                    RNNUIBarButtonItem *rnnButton = (RNNUIBarButtonItem *)barButton;
                    BOOL hidesSharedBackground = [rnnButton resolvesHidesSharedBackground];
                    barButton.hidesSharedBackground = hidesSharedBackground;
                    barButton.sharesBackground = NO;
                }
            }
        };
        apply(item.leftBarButtonItems);
        apply(item.rightBarButtonItems);
    }
}

+ (void)flushDeferredBarButtonLayoutsForNavigationItem:(UINavigationItem *)item {
    if (@available(iOS 26.0, *)) {
        void (^flush)(NSArray<UIBarButtonItem *> *) = ^(NSArray<UIBarButtonItem *> *items) {
            for (UIBarButtonItem *barButton in items) {
                if ([barButton isKindOfClass:[RNNUIBarButtonItem class]]) {
                    [(RNNUIBarButtonItem *)barButton flushDeferredNavigationBarLayout];
                }
            }
        };
        flush(item.leftBarButtonItems);
        flush(item.rightBarButtonItems);
    }
}

+ (void)relayoutBarContentForNavigationItem:(UINavigationItem *)item
                              navigationBar:(UINavigationBar *)navigationBar {
    if (@available(iOS 26.0, *)) {
        if (!navigationBar || CGRectIsEmpty(navigationBar.bounds)) {
            return;
        }

        if ([item.titleView isKindOfClass:[RNNReactTitleView class]]) {
            RNNReactTitleView *titleView = (RNNReactTitleView *)item.titleView;
            [titleView setAlignment:@"fill" inFrame:navigationBar.bounds];
            [titleView setNeedsLayout];
            [titleView layoutIfNeeded];
        }

        void (^relayoutCustomButtons)(NSArray<UIBarButtonItem *> *) = ^(NSArray<UIBarButtonItem *> *items) {
            for (UIBarButtonItem *barButton in items) {
                if ([barButton.customView isKindOfClass:[RNNReactButtonView class]]) {
                    RNNReactButtonView *buttonView = (RNNReactButtonView *)barButton.customView;
                    buttonView.layer.affineTransform = CGAffineTransformIdentity;
                    [buttonView setNeedsLayout];
                }
            }
        };
        relayoutCustomButtons(item.leftBarButtonItems);
        relayoutCustomButtons(item.rightBarButtonItems);

        [navigationBar setNeedsLayout];
        [navigationBar layoutIfNeeded];
    }
}

- (void)finalizeNavigationBarAfterTransitionForViewController:(UIViewController *)viewController {
    if (@available(iOS 26.0, *)) {
        self.navigationController.navigationBar.translucent = NO;
    }
}

- (void)applyBackgroundToAppearance:(UINavigationBarAppearance *)appearance
                   withOpaqueColor:(UIColor *)color
                        transparent:(BOOL)transparent
                        translucent:(BOOL)translucent {
    if (transparent || color.isTransparent) {
        [appearance configureWithTransparentBackground];
        if (@available(iOS 26.0, *)) {
            appearance.backgroundEffect = nil;
            appearance.backgroundColor = UIColor.clearColor;
            appearance.backgroundImage = nil;
        }
        return;
    }

    if (color) {
        if (@available(iOS 26.0, *)) {
            RNNApplyIOS26SolidColorToAppearance(appearance, color);
        } else {
            [appearance configureWithOpaqueBackground];
            appearance.backgroundColor = color;
        }
        return;
    }

    if (translucent) {
        [appearance configureWithDefaultBackground];
    } else {
        [appearance configureWithOpaqueBackground];
    }
}

- (void)applyOptions:(RNNTopBarOptions *)options {
    [self setTranslucent:[options.background.translucent withDefault:NO]];
    [self
        setScrollEdgeTranslucent:[options.scrollEdgeAppearance.background.translucent
                                     withDefault:[options.background.translucent withDefault:NO]]];
    [self setBackgroundColor:[options.background.color withDefault:nil]];
    [self setScrollEdgeAppearanceColor:[options.scrollEdgeAppearance.background.color
                                           withDefault:nil]];
    
    [self setTitleAttributes:options.title];
    [self setLargeTitleAttributes:options.largeTitle];
    if (options.scrollEdgeAppearance.title.hasValue) {
        [self setScrollEdgeTitleAttributes:options.scrollEdgeAppearance.title];
    } else {
        [self setScrollEdgeTitleAttributes:options.title];
    }
    
    [self setBorderColor:[options.borderColor withDefault:nil]];
    [self showBorder:![options.noBorder withDefault:NO]];
    [self setBackButtonOptions:options.backButton];
    if ([options.scrollEdgeAppearance.active withDefault:NO]) {
        [self updateScrollEdgeAppearance];
    }
    [self setScrollEdgeBorderColor:[options.scrollEdgeAppearance.borderColor withDefault:nil]];
    [self showScrollEdgeBorder:![options.scrollEdgeAppearance.noBorder withDefault:NO]];
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
        [self showScrollEdgeBorder:!options.scrollEdgeAppearance.noBorder.get];
    }

    if (options.scrollEdgeAppearance.title.hasValue) {
        [self setScrollEdgeTitleAttributes:defaultOptions.scrollEdgeAppearance.title];
    } else if (options.title.hasValue && !defaultOptions.scrollEdgeAppearance.title.hasValue) {
        [self setScrollEdgeTitleAttributes:defaultOptions.title];
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
    UIColor *color = self.scrollEdgeAppearanceColor ?: self.backgroundColor;
    [self applyBackgroundToAppearance:self.getScrollEdgeAppearance
                        withOpaqueColor:color
                             transparent:self.scrollEdgeTransparent
                              translucent:self.scrollEdgeTranslucent];
}

- (void)updateBackgroundAppearance {
    if (@available(iOS 26.0, *)) {
        UINavigationItem *item = self.currentNavigationItem;
        [TopBarAppearancePresenter applyBackgroundToNavigationItem:item
                                                             color:self.backgroundColor
                                                        transparent:self.transparent
                                                         translucent:self.translucent];
        self.navigationController.navigationBar.translucent =
            self.translucent || self.transparent || !self.backgroundColor;
        return;
    }

    [self applyBackgroundToAppearance:self.getAppearance
                        withOpaqueColor:self.backgroundColor
                             transparent:self.transparent
                              translucent:self.translucent];
    [self applyBackgroundToAppearance:self.getScrollEdgeAppearance
                        withOpaqueColor:self.backgroundColor
                             transparent:self.transparent
                              translucent:self.translucent];
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
        self.showScrollEdgeBorder ? self.scrollEdgeBorderColor : nil;
}

- (void)setBackIndicatorImage:(UIImage *)image withColor:(UIColor *)color {
    [self.getAppearance setBackIndicatorImage:image transitionMaskImage:image];
    [self.getScrollEdgeAppearance setBackIndicatorImage:image transitionMaskImage:image];
}

- (void)setTitleAttributes:(RNNTitleOptions *)titleOptions {
    NSString *fontFamily = [titleOptions.fontFamily withDefault:nil];
    NSString *fontWeight = [titleOptions.fontWeight withDefault:nil];
    NSNumber *fontSize = [titleOptions.fontSize withDefault:nil];
    UIColor *fontColor = [titleOptions.color withDefault:nil];

    NSDictionary *titleTextAttributes =
        [RNNFontAttributesCreator createFromDictionary:self.getAppearance.titleTextAttributes
                                            fontFamily:fontFamily
                                              fontSize:fontSize
                                            fontWeight:fontWeight
                                                 color:fontColor
                                              centered:YES];

    // Let's not forget to set our UINavigationBarAppearance's lineBreakMode to TruncatingTail!
    id attrib = titleTextAttributes[NSParagraphStyleAttributeName];
    if ([attrib isKindOfClass:[NSMutableParagraphStyle class]]) {
        ((NSMutableParagraphStyle*)titleTextAttributes[NSParagraphStyleAttributeName]).lineBreakMode = NSLineBreakByTruncatingTail;
    }
    
    self.getAppearance.titleTextAttributes = titleTextAttributes;
}

- (void)setScrollEdgeTitleAttributes:(RNNTitleOptions *)titleOptions {
    NSString *fontFamily = [titleOptions.fontFamily withDefault:nil];
    NSString *fontWeight = [titleOptions.fontWeight withDefault:nil];
    NSNumber *fontSize = [titleOptions.fontSize withDefault:nil];
    UIColor *fontColor = [titleOptions.color withDefault:nil];

    NSDictionary *titleTextAttributes =
        [RNNFontAttributesCreator createFromDictionary:self.getScrollEdgeAppearance.titleTextAttributes
                                            fontFamily:fontFamily
                                              fontSize:fontSize
                                            fontWeight:fontWeight
                                                 color:fontColor
                                              centered:YES];

    id attrib = titleTextAttributes[NSParagraphStyleAttributeName];
    if ([attrib isKindOfClass:[NSMutableParagraphStyle class]]) {
        ((NSMutableParagraphStyle *)titleTextAttributes[NSParagraphStyleAttributeName]).lineBreakMode =
            NSLineBreakByTruncatingTail;
    }

    self.getScrollEdgeAppearance.titleTextAttributes = titleTextAttributes;
}

- (void)setLargeTitleAttributes:(RNNLargeTitleOptions *)largeTitleOptions {
    NSString *fontFamily = [largeTitleOptions.fontFamily withDefault:nil];
    NSString *fontWeight = [largeTitleOptions.fontWeight withDefault:nil];
    NSNumber *fontSize = [largeTitleOptions.fontSize withDefault:nil];
    UIColor *fontColor = [largeTitleOptions.color withDefault:nil];
    NSDictionary *largeTitleTextAttributes =
        [RNNFontAttributesCreator createFromDictionary:self.getAppearance.largeTitleTextAttributes
                                            fontFamily:fontFamily
                                              fontSize:fontSize
                                            fontWeight:fontWeight
                                                 color:fontColor
                                              centered:NO];
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
