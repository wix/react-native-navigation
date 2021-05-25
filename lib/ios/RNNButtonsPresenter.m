#import "RNNButtonsPresenter.h"
#import "NSArray+utils.h"
#import "RNNButtonBuilder.h"

@interface RNNButtonsPresenter ()
@property(weak, nonatomic) UIViewController<RNNLayoutProtocol> *viewController;
@property(strong, nonatomic) RNNReactComponentRegistry *componentRegistry;
@property(strong, nonatomic) RNNEventEmitter *eventEmitter;
@property(strong, nonatomic) RNNButtonBuilder *buttonBuilder;
@end

@implementation RNNButtonsPresenter

- (instancetype)initWithComponentRegistry:(RNNReactComponentRegistry *)componentRegistry
                             eventEmitter:(RNNEventEmitter *)eventEmitter {
    self = [super init];
    self.componentRegistry = componentRegistry;
    self.buttonBuilder =
        [[RNNButtonBuilder alloc] initWithComponentRegistry:self.componentRegistry];
    self.eventEmitter = eventEmitter;
    return self;
}

- (void)bindViewController:(UIViewController<RNNLayoutProtocol> *)viewController {
    _viewController = viewController;
}

- (void)applyLeftButtons:(NSArray<RNNButtonOptions *> *)leftButtons
            defaultColor:(Color *)defaultColor
    defaultDisabledColor:(Color *)defaultDisabledColor
                animated:(BOOL)animated {
    [self setButtons:leftButtons
                        side:@"left"
                    animated:animated
                defaultColor:defaultColor
        defaultDisabledColor:defaultDisabledColor];
}

- (void)applyRightButtons:(NSArray<RNNButtonOptions *> *)rightButtons
             defaultColor:(Color *)defaultColor
     defaultDisabledColor:(Color *)defaultDisabledColor
                 animated:(BOOL)animated {
    [self setButtons:rightButtons
                        side:@"right"
                    animated:animated
                defaultColor:defaultColor
        defaultDisabledColor:defaultDisabledColor];
}

- (void)applyLeftButtonsColor:(UIColor *)color {
    for (RNNUIBarButtonItem *button in self.viewController.navigationItem.leftBarButtonItems) {
        [button applyColor:color];
    }
}

- (void)applyRightButtonsColor:(UIColor *)color {
    for (RNNUIBarButtonItem *button in self.viewController.navigationItem.rightBarButtonItems) {
        [button applyColor:color];
    }
}

- (void)setButtons:(NSArray<RNNButtonOptions *> *)buttons
                    side:(NSString *)side
                animated:(BOOL)animated
            defaultColor:(Color *)defaultColor
    defaultDisabledColor:(Color *)defaultDisabledColor {
    NSMutableArray *barButtonItems = [NSMutableArray new];
    for (RNNButtonOptions *button in buttons) {
        RNNUIBarButtonItem *barButtonItem = [_buttonBuilder
                        build:[button withDefaultColor:defaultColor
                                         disabledColor:defaultDisabledColor]
            parentComponentId:_viewController.layoutInfo.componentId
                      onPress:^(NSString *buttonId) {
                        [self.eventEmitter
                            sendOnNavigationButtonPressed:self.viewController.layoutInfo.componentId
                                                 buttonId:buttonId];
                      }];
        if (barButtonItem)
            [barButtonItems addObject:barButtonItem];
    }

    if ([side isEqualToString:@"left"]) {
        [self replaceCurrentButtons:self.viewController.navigationItem.leftBarButtonItems
                        withButtons:barButtonItems];
        [self.viewController.navigationItem setLeftBarButtonItems:barButtonItems animated:animated];
    }

    if ([side isEqualToString:@"right"]) {
        [self replaceCurrentButtons:self.viewController.navigationItem.rightBarButtonItems
                        withButtons:barButtonItems];
        [self.viewController.navigationItem setRightBarButtonItems:barButtonItems
                                                          animated:animated];
    }
}

- (NSArray *)currentButtons {
    NSMutableArray *currentButtons = [NSMutableArray new];
    [currentButtons addObjectsFromArray:self.viewController.navigationItem.leftBarButtonItems];
    [currentButtons addObjectsFromArray:self.viewController.navigationItem.rightBarButtonItems];
    return currentButtons;
}

- (void)componentWillAppear {
    for (UIBarButtonItem *barButtonItem in [self currentButtons]) {
        if ([self isRNNUIBarButton:barButtonItem]) {
            [(RNNUIBarButtonItem *)barButtonItem notifyWillAppear];
        }
    }
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

- (BOOL)isRNNUIBarButton:(UIBarButtonItem *)barButtonItem {
    return [barButtonItem isKindOfClass:[RNNUIBarButtonItem class]];
}

- (void)replaceCurrentButtons:(NSArray<UIBarButtonItem *> *)oldButtons
                  withButtons:(NSArray<UIBarButtonItem *> *)newButtons {
    NSArray<UIBarButtonItem *> *removedButtons = [oldButtons difference:newButtons
                                                       withPropertyName:@"customView"];
    NSArray<UIBarButtonItem *> *addedButtons = [newButtons difference:oldButtons
                                                     withPropertyName:@"customView"];

    for (UIBarButtonItem *buttonItem in removedButtons) {
        RNNReactView *reactView = buttonItem.customView;
        if ([reactView isKindOfClass:[RNNReactView class]]) {
            [reactView componentDidDisappear];
            [_componentRegistry removeChildComponent:reactView.componentId];
        }
    }

    for (RNNUIBarButtonItem *barButtonItem in addedButtons) {
        if ([self isRNNUIBarButton:barButtonItem]) {
            [(RNNUIBarButtonItem *)barButtonItem notifyWillAppear];
            [(RNNUIBarButtonItem *)barButtonItem notifyDidAppear];
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
