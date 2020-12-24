#import "RNNButtonBuilder.h"
#import "RNNFontAttributesCreator.h"

@interface RNNButtonBuilder ()
@property(weak, nonatomic) UIViewController<RNNLayoutProtocol> *viewController;
@property(strong, nonatomic) RNNReactComponentRegistry *componentRegistry;
@end

@implementation RNNButtonBuilder

- (instancetype)initWithComponentRegistry:(id)componentRegistry {
    self = [super init];
    self.componentRegistry = componentRegistry;
    return self;
}

- (void)bindViewController:(UIViewController<RNNLayoutProtocol> *)viewController {
    self.viewController = viewController;
}

- (void)assertButtonId:(RNNButtonOptions *)button {
    if (!button.identifier.hasValue) {
        @throw [NSException
            exceptionWithName:@"NSInvalidArgumentException"
                       reason:[@"button id is not specified "
                                  stringByAppendingString:[button.text getWithDefaultValue:@""]]
                     userInfo:nil];
    }
}

- (RNNUIBarButtonItem *)build:(RNNButtonOptions *)button onPress:(RNNButtonPressCallback)onPress {
    [self assertButtonId:button];

    if (button.component.hasValue) {
        RNNReactButtonView *view = [_componentRegistry
            createComponentIfNotExists:button.component
                     parentComponentId:self.viewController.layoutInfo.componentId
                         componentType:RNNComponentTypeTopBarButton
                   reactViewReadyBlock:nil];
        return [[RNNUIBarButtonItem alloc] initWithCustomView:view
                                                buttonOptions:button
                                                      onPress:onPress];
    } else if (button.shouldCreateCustomView) {
        return [[RNNUIBarButtonItem alloc] initCustomIcon:button onPress:onPress];
    } else if (button.icon.hasValue) {
        return [[RNNUIBarButtonItem alloc] initWithIcon:button onPress:onPress];
    } else if (button.text.hasValue) {
        return [[RNNUIBarButtonItem alloc] initWithTitle:button onPress:onPress];
    } else if (button.systemItem.hasValue) {
        return [[RNNUIBarButtonItem alloc] initWithSystemItem:button onPress:onPress];
    } else
        return nil;
}

@end
