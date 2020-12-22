#import "RNNButtonsPresenter.h"
#import "NSArray+utils.h"
#import "RNNFontAttributesCreator.h"
#import "RNNUIBarButtonItem.h"
#import "UIImage+tint.h"
#import "UIViewController+LayoutProtocol.h"
#import <React/RCTConvert.h>

#define BUTTON_WIDTH @(40)
#define BUTTON_HEIGHT @(40)

@interface RNNButtonsPresenter ()
@property(weak, nonatomic) UIViewController<RNNLayoutProtocol> *viewController;
@property(strong, nonatomic) RNNReactComponentRegistry *componentRegistry;
@end

@implementation RNNButtonsPresenter

- (instancetype)initWithViewController:(UIViewController<RNNLayoutProtocol> *)viewController
                     componentRegistry:(id)componentRegistry {
    self = [super init];

    self.viewController = viewController;
    self.componentRegistry = componentRegistry;

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
        RNNUIBarButtonItem *barButtonItem = [self buildButton:[button withDefault:defaultStyle]];
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

- (RNNUIBarButtonItem *)buildButton:(RNNButtonOptions *)button {
    UIColor *color = [button.color getWithDefaultValue:nil];
    UIColor *disabledColor = [button.disabledColor getWithDefaultValue:nil];
    UIImage *iconImage = [button.icon getWithDefaultValue:nil];

    if (!button.identifier.hasValue) {
        @throw [NSException
            exceptionWithName:@"NSInvalidArgumentException"
                       reason:[@"button id is not specified "
                                  stringByAppendingString:[button.text getWithDefaultValue:@""]]
                     userInfo:nil];
    }

    if (button.color.hasValue) {
        iconImage = [iconImage withTintColor:button.color.get];
    }

    RNNUIBarButtonItem *barButtonItem;
    if (button.component.hasValue) {
        RNNReactButtonView *view = [_componentRegistry
            createComponentIfNotExists:button.component
                     parentComponentId:self.viewController.layoutInfo.componentId
                         componentType:RNNComponentTypeTopBarButton
                   reactViewReadyBlock:nil];
        barButtonItem = [[RNNUIBarButtonItem alloc] init:button.identifier.get withCustomView:view];
    } else if (button.shouldCreateCustomView) {
        barButtonItem = [[RNNUIBarButtonItem alloc]
                       init:button.identifier.get
                       icon:iconImage
                       size:CGSizeMake(
                                [button.iconBackground.width getWithDefaultValue:BUTTON_WIDTH]
                                    .floatValue,
                                [button.iconBackground.height getWithDefaultValue:BUTTON_HEIGHT]
                                    .floatValue)
            backgroundColor:[button.iconBackground.color getWithDefaultValue:nil]
               cornerRadius:[button.iconBackground.cornerRadius getWithDefaultValue:@(0)].floatValue
                     insets:button.iconInsets.UIEdgeInsets];
    } else if (iconImage) {
        barButtonItem = [[RNNUIBarButtonItem alloc] init:button.identifier.get icon:iconImage];
    } else if (button.text.hasValue) {
        barButtonItem = [[RNNUIBarButtonItem alloc] init:button.identifier.get
                                               withTitle:button.text.get];
    } else if (button.systemItem.hasValue) {
        barButtonItem = [[RNNUIBarButtonItem alloc] init:button.identifier.get
                                          withSystemItem:button.systemItem.get];
    } else {
        return nil;
    }

    barButtonItem.accessibilityLabel = [button.accessibilityLabel getWithDefaultValue:nil];
    barButtonItem.target = self.viewController;
    barButtonItem.enabled = [button.enabled getWithDefaultValue:YES];
    barButtonItem.action = @selector(onButtonPress:);
    NSMutableDictionary *textAttributes = [NSMutableDictionary
        dictionaryWithDictionary:[RNNFontAttributesCreator
                                     createWithFontFamily:[button.fontFamily
                                                              getWithDefaultValue:nil]
                                                 fontSize:[button.fontSize
                                                              getWithDefaultValue:@(17)]
                                               fontWeight:[button.fontWeight
                                                              getWithDefaultValue:nil]
                                                    color:nil]];
    NSMutableDictionary *disabledTextAttributes = [NSMutableDictionary
        dictionaryWithDictionary:[RNNFontAttributesCreator
                                     createWithFontFamily:[button.fontFamily
                                                              getWithDefaultValue:nil]
                                                 fontSize:[button.fontSize
                                                              getWithDefaultValue:@(17)]
                                               fontWeight:[button.fontWeight
                                                              getWithDefaultValue:nil]
                                                    color:nil]];

    if (![button.enabled getWithDefaultValue:YES] && button.disabledColor.hasValue) {
        color = button.disabledColor.get;
        [disabledTextAttributes setObject:disabledColor forKey:NSForegroundColorAttributeName];
    }

    if (color) {
        [textAttributes setObject:color forKey:NSForegroundColorAttributeName];
        [barButtonItem setImage:[[iconImage withTintColor:color]
                                    imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal]];
        barButtonItem.tintColor = color;
    }

    [barButtonItem setTitleTextAttributes:textAttributes forState:UIControlStateNormal];
    [barButtonItem setTitleTextAttributes:textAttributes forState:UIControlStateHighlighted];
    [barButtonItem setTitleTextAttributes:disabledTextAttributes forState:UIControlStateDisabled];

    barButtonItem.accessibilityIdentifier = [button.testID getWithDefaultValue:nil];

    return barButtonItem;
}

- (UIEdgeInsets)leftButtonInsets:(RNNInsetsOptions *)insets {
    return [insets edgeInsetsWithDefault:UIEdgeInsetsZero];
}

- (UIEdgeInsets)rightButtonInsets:(RNNInsetsOptions *)insets {
    return [insets edgeInsetsWithDefault:UIEdgeInsetsZero];
}

@end
