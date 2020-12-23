#import "RNNButtonBuilder.h"
#import "RNNFontAttributesCreator.h"
#import "UIImage+tint.h"

#define BUTTON_WIDTH @(40)
#define BUTTON_HEIGHT @(40)

@interface RNNButtonBuilder ()
@property(weak, nonatomic) UIViewController<RNNLayoutProtocol> *viewController;
@property(strong, nonatomic) RNNReactComponentRegistry *componentRegistry;
@end

@implementation RNNButtonBuilder

- (instancetype)initWithViewController:(UIViewController *)viewController
                     componentRegistry:(id)componentRegistry {
    self = [super init];
    _viewController = viewController;
    _componentRegistry = componentRegistry;
    return self;
}

- (RNNUIBarButtonItem *)build:(RNNButtonOptions *)button {
    if (!button.identifier.hasValue) {
        @throw [NSException
            exceptionWithName:@"NSInvalidArgumentException"
                       reason:[@"button id is not specified "
                                  stringByAppendingString:[button.text getWithDefaultValue:@""]]
                     userInfo:nil];
    }

    RNNUIBarButtonItem *barButtonItem = [self buildBarButtonItem:button];
    [self applyTitleTextAttributes:button barButtonItem:barButtonItem];
    [self applyDisabledTitleTextAttributes:button barButtonItem:barButtonItem];
    [self applyAdditionalFields:button barButtonItem:barButtonItem];

    return barButtonItem;
}

- (void)applyAdditionalFields:(RNNButtonOptions *)button
                barButtonItem:(RNNUIBarButtonItem *)barButtonItem {
    barButtonItem.accessibilityLabel = [button.accessibilityLabel getWithDefaultValue:nil];
    barButtonItem.target = self.viewController;
    barButtonItem.enabled = [button.enabled getWithDefaultValue:YES];
    barButtonItem.action = @selector(onButtonPress:);
    barButtonItem.accessibilityIdentifier = [button.testID getWithDefaultValue:nil];
}

- (void)applyTitleTextAttributes:(RNNButtonOptions *)button
                   barButtonItem:(RNNUIBarButtonItem *)barButtonItem {
    NSMutableDictionary *textAttributes = [NSMutableDictionary
        dictionaryWithDictionary:[RNNFontAttributesCreator
                                     createWithFontFamily:[button.fontFamily
                                                              getWithDefaultValue:nil]
                                                 fontSize:[button.fontSize
                                                              getWithDefaultValue:@(17)]
                                               fontWeight:[button.fontWeight
                                                              getWithDefaultValue:nil]
                                                    color:[self resolveTintColor:button]]];

    [barButtonItem setTitleTextAttributes:textAttributes forState:UIControlStateNormal];
    [barButtonItem setTitleTextAttributes:textAttributes forState:UIControlStateHighlighted];
}

- (void)applyDisabledTitleTextAttributes:(RNNButtonOptions *)button
                           barButtonItem:(RNNUIBarButtonItem *)barButtonItem {
    NSMutableDictionary *disabledTextAttributes = [NSMutableDictionary
        dictionaryWithDictionary:[RNNFontAttributesCreator
                                     createWithFontFamily:[button.fontFamily
                                                              getWithDefaultValue:nil]
                                                 fontSize:[button.fontSize
                                                              getWithDefaultValue:@(17)]
                                               fontWeight:[button.fontWeight
                                                              getWithDefaultValue:nil]
                                                    color:[button.disabledColor
                                                              getWithDefaultValue:nil]]];

    [barButtonItem setTitleTextAttributes:disabledTextAttributes forState:UIControlStateDisabled];
}

- (RNNUIBarButtonItem *)buildBarButtonItem:(RNNButtonOptions *)button {
    if (button.component.hasValue) {
        return [self buildComponent:button];
    } else if (button.shouldCreateCustomView) {
        return [self buildCustomView:button];
    } else if (button.icon.hasValue) {
        return [[RNNUIBarButtonItem alloc] init:button.identifier.get
                                           icon:button.icon.get
                                      tintColor:[self resolveTintColor:button]];
    } else if (button.text.hasValue) {
        return [[RNNUIBarButtonItem alloc] init:button.identifier.get withTitle:button.text.get];
    } else if (button.systemItem.hasValue) {
        return [[RNNUIBarButtonItem alloc] init:button.identifier.get
                                 withSystemItem:button.systemItem.get];
    } else
        return nil;
}

- (RNNUIBarButtonItem *)buildComponent:(RNNButtonOptions *)button {
    RNNReactButtonView *view =
        [_componentRegistry createComponentIfNotExists:button.component
                                     parentComponentId:self.viewController.layoutInfo.componentId
                                         componentType:RNNComponentTypeTopBarButton
                                   reactViewReadyBlock:nil];
    return [[RNNUIBarButtonItem alloc] init:button.identifier.get withCustomView:view];
}

- (RNNUIBarButtonItem *)buildCustomView:(RNNButtonOptions *)button {
    return [[RNNUIBarButtonItem alloc]
                   init:button.identifier.get
                   icon:button.icon.get
                   size:CGSizeMake([button.iconBackground.width getWithDefaultValue:BUTTON_WIDTH]
                                       .floatValue,
                                   [button.iconBackground.height getWithDefaultValue:BUTTON_HEIGHT]
                                       .floatValue)
        backgroundColor:[button.iconBackground.color getWithDefaultValue:nil]
           cornerRadius:[button.iconBackground.cornerRadius getWithDefaultValue:@(0)].floatValue
                 insets:button.iconInsets.UIEdgeInsets
              tintColor:[self resolveTintColor:button]];
}

- (UIColor *)resolveTintColor:(RNNButtonOptions *)button {
    if (![button.enabled getWithDefaultValue:YES] && button.disabledColor.hasValue)
        return button.disabledColor.get;
    else
        return [button.color getWithDefaultValue:nil];
}

@end
