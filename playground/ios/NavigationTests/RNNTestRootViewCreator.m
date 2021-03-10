#import "RNNTestRootViewCreator.h"
#import <OCMock/OCMock.h>

@interface RNNMockReactView : UIView <RNNComponentProtocol>
@end

@implementation RNNMockReactView {
    NSString *_componentName;
    NSString *_componentId;
    RNNComponentType _componentType;
}

- (instancetype)initWithName:(NSString *)name componentId:(NSString *)componentId {
    self = [self initWithFrame:UIScreen.mainScreen.bounds];
    _componentName = name;
    _componentId = componentId;
    return self;
}

- (NSString *)componentId {
    return _componentId;
}

- (NSString *)componentType {
    return ComponentTypeScreen;
}

- (void)componentWillAppear {
}

- (void)componentDidAppear {
}

- (void)componentDidDisappear {
}

- (void)setPassThroughTouches:(BOOL)passThroughTouches {
}

- (void)invalidate {
}

@end

@implementation RNNTestRootViewCreator

- (RNNMockReactView *)createRootView:(NSString *)name
                          rootViewId:(NSString *)rootViewId
                              ofType:(RNNComponentType)componentType
                 reactViewReadyBlock:(RNNReactViewReadyCompletionBlock)reactViewReadyBlock {
    RNNMockReactView *view = [[RNNMockReactView alloc] initWithName:name componentId:rootViewId];
    UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, 200, 200)];
    label.textAlignment = NSTextAlignmentCenter;
    label.center = [view convertPoint:view.center fromView:view.superview];
    ;
    label.text = rootViewId;
    [view addSubview:label];
    view.tag = [rootViewId intValue];
    view.backgroundColor = UIColor.redColor;
    return view;
}

@end
