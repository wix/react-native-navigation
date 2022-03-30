#import "RNNButtonBuilder.h"
#import "RNNFontAttributesCreator.h"

@implementation RNNButtonBuilder {
    RNNReactComponentRegistry *_componentRegistry;
    RNNIconCreator *_iconCreator;
}

- (instancetype)initWithComponentRegistry:(id)componentRegistry {
    self = [super init];
    _componentRegistry = componentRegistry;
    _iconCreator = [[RNNIconCreator alloc] initWithIconDrawer:RNNIconDrawer.new];
    return self;
}

- (RNNUIBarButtonItem *)build:(RNNButtonOptions *)button
            parentComponentId:(NSString *)parentComponentId
                      onPress:(RNNButtonPressCallback)onPress {
    [self assertButtonId:button];

    if (button.component.hasValue) {
        RNNReactButtonView *view =
            [_componentRegistry createComponentIfNotExists:button.component
                                         parentComponentId:parentComponentId
                                             componentType:RNNComponentTypeTopBarButton
                                       reactViewReadyBlock:nil];
        return [[RNNUIBarButtonItem alloc] initWithCustomView:view
                                                buttonOptions:button
                                                      onPress:onPress];
    } else if (button.shouldCreateCustomView) {
        return [[RNNUIBarButtonItem alloc] initCustomIcon:button
                                              iconCreator:_iconCreator
                                                  onPress:onPress];
    } else if (button.sfSymbol.hasValue) {
        return [[RNNUIBarButtonItem alloc] initWithSFSymbol:button onPress:onPress];
    } else if (button.icon.hasValue) {
        return [[RNNUIBarButtonItem alloc] initWithIcon:button onPress:onPress];
    } else if (button.text.hasValue) {
        return [[RNNUIBarButtonItem alloc] initWithTitle:button onPress:onPress];
    } else if (button.systemItem.hasValue) {
        return [[RNNUIBarButtonItem alloc] initWithSystemItem:button onPress:onPress];
    } else
        return nil;
}

- (void)assertButtonId:(RNNButtonOptions *)button {
    if (!button.identifier.hasValue) {
        @throw [NSException
            exceptionWithName:@"NSInvalidArgumentException"
                       reason:[@"button id is not specified "
                                  stringByAppendingString:[button.text withDefault:@""]]
                     userInfo:nil];
    }
}

@end
