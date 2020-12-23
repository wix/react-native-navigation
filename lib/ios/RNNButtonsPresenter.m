#import "RNNButtonsPresenter.h"
#import "NSArray+utils.h"
#import "RNNButtonBuilder.h"
#import "RNNFontAttributesCreator.h"
#import "RNNUIBarButtonItem.h"
#import "UIImage+tint.h"
#import "UIViewController+LayoutProtocol.h"
#import <React/RCTConvert.h>

@interface RNNButtonsPresenter ()
@property(weak, nonatomic) UIViewController<RNNLayoutProtocol> *viewController;
@property(strong, nonatomic) RNNReactComponentRegistry *componentRegistry;
@property(strong, nonatomic) RNNButtonBuilder *buttonBuilder;
@end

@implementation RNNButtonsPresenter

- (instancetype)initWithViewController:(UIViewController<RNNLayoutProtocol> *)viewController
                     componentRegistry:(id)componentRegistry {
    self = [super init];

    self.viewController = viewController;
    self.componentRegistry = componentRegistry;
    self.buttonBuilder = [[RNNButtonBuilder alloc] initWithViewController:viewController
                                                        componentRegistry:componentRegistry];
    return self;
}

- (void)applyLeftButtons:(NSArray<RNNButtonOptions *> *)leftButtons
      defaultButtonStyle:(RNNButtonOptions *)defaultButtonStyle {
    [self setButtons:leftButtons side:@"left" animated:NO defaultStyle:defaultButtonStyle];
}

- (void)applyRightButtons:(NSArray<RNNButtonOptions *> *)rightButtons
       defaultButtonStyle:(RNNButtonOptions *)defaultButtonStyle {
    [self setButtons:rightButtons side:@"right" animated:NO defaultStyle:defaultButtonStyle];
}

- (void)setButtons:(NSArray<RNNButtonOptions *> *)buttons
              side:(NSString *)side
          animated:(BOOL)animated
      defaultStyle:(RNNButtonOptions *)defaultStyle {
    NSMutableArray *barButtonItems = [NSMutableArray new];
    for (RNNButtonOptions *button in buttons) {
        RNNUIBarButtonItem *barButtonItem =
            [_buttonBuilder build:[button withDefault:defaultStyle]];
        if (barButtonItem)
            [barButtonItems addObject:barButtonItem];
    }

    if ([side isEqualToString:@"left"]) {
        [self clearPreviousButtonViews:barButtonItems
                            oldButtons:self.viewController.navigationItem.leftBarButtonItems];
        [self.viewController.navigationItem setLeftBarButtonItems:barButtonItems animated:animated];
    }

    if ([side isEqualToString:@"right"]) {
        [self clearPreviousButtonViews:barButtonItems
                            oldButtons:self.viewController.navigationItem.rightBarButtonItems];
        [self.viewController.navigationItem setRightBarButtonItems:barButtonItems
                                                          animated:animated];
    }

    [self notifyButtonsDidAppear:barButtonItems];
}

- (NSArray *)currentButtons {
    NSMutableArray *currentButtons = [NSMutableArray new];
    [currentButtons addObjectsFromArray:self.viewController.navigationItem.leftBarButtonItems];
    [currentButtons addObjectsFromArray:self.viewController.navigationItem.rightBarButtonItems];
    return currentButtons;
}

- (void)componentDidAppear {
    for (UIBarButtonItem *barButtonItem in [self currentButtons]) {
        if ([self isRNNUIBarButton:barButtonItem]) {
            [(RNNUIBarButtonItem *)barButtonItem notifyDidAppear];
        }
    }
}

- (void)componentDidDisappear {
    for (UIBarButtonItem *barButtonItem in [self currentButtons]) {
        if ([self isRNNUIBarButton:barButtonItem]) {
            [(RNNUIBarButtonItem *)barButtonItem notifyDidDisappear];
        }
    }
}

- (void)notifyButtonsDidAppear:(NSArray *)barButtonItems {
    for (UIBarButtonItem *barButtonItem in barButtonItems) {
        if ([self isRNNUIBarButton:barButtonItem]) {
            [(RNNUIBarButtonItem *)barButtonItem notifyDidAppear];
        }
    }
}

- (BOOL)isRNNUIBarButton:(UIBarButtonItem *)barButtonItem {
    return [barButtonItem isKindOfClass:[RNNUIBarButtonItem class]];
}

- (void)clearPreviousButtonViews:(NSArray<UIBarButtonItem *> *)newButtons
                      oldButtons:(NSArray<UIBarButtonItem *> *)oldButtons {
    NSArray<UIBarButtonItem *> *removedButtons = [oldButtons difference:newButtons
                                                       withPropertyName:@"customView"];

    for (UIBarButtonItem *buttonItem in removedButtons) {
        RNNReactView *reactView = buttonItem.customView;
        if ([reactView isKindOfClass:[RNNReactView class]]) {
            [reactView componentDidDisappear];
            [_componentRegistry removeChildComponent:reactView.componentId];
        }
    }
}

- (UIEdgeInsets)leftButtonInsets:(RNNInsetsOptions *)insets {
    return [insets edgeInsetsWithDefault:UIEdgeInsetsZero];
}

- (UIEdgeInsets)rightButtonInsets:(RNNInsetsOptions *)insets {
    return [insets edgeInsetsWithDefault:UIEdgeInsetsZero];
}

@end
