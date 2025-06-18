#import "RNNReactView.h"
#import "RNNAppDelegate.h"
#import <React/RCTRootContentView.h>

#ifdef RCT_NEW_ARCH_ENABLED
#import <React-RuntimeApple/ReactCommon/RCTHost.h>
#import <React/RCTFabricSurface.h>
#import <React/RCTSurfacePresenter.h>

#endif

@implementation RNNReactView {
    BOOL _isMounted;
    BOOL _pendingWillAppear;
    BOOL _pendingDidAppear;
    BOOL _didAppear;
    BOOL _willAppear;
}

#ifdef RCT_NEW_ARCH_ENABLED
- (instancetype)initWithBridge:(RCTBridge *)bridge
                    moduleName:(NSString *)moduleName
             initialProperties:(NSDictionary *)initialProperties
                  eventEmitter:(RNNEventEmitter *)eventEmitter
               sizeMeasureMode:(RCTSurfaceSizeMeasureMode)sizeMeasureMode
           reactViewReadyBlock:(RNNReactViewReadyCompletionBlock)reactViewReadyBlock {
    RCTFabricSurface *surface = [[RCTFabricSurface alloc]
        initWithSurfacePresenter:(RCTSurfacePresenter *)bridge.surfacePresenter
                      moduleName:moduleName
               initialProperties:initialProperties];
    self = [super initWithSurface:surface sizeMeasureMode:sizeMeasureMode];
#else
- (instancetype)initWithBridge:(RCTBridge *)bridge
                    moduleName:(NSString *)moduleName
             initialProperties:(NSDictionary *)initialProperties
                  eventEmitter:(RNNEventEmitter *)eventEmitter
           reactViewReadyBlock:(RNNReactViewReadyCompletionBlock)reactViewReadyBlock {
    self = [super initWithBridge:bridge moduleName:moduleName initialProperties:initialProperties];
#endif

    _reactViewReadyBlock = reactViewReadyBlock;
    _eventEmitter = eventEmitter;

#ifdef RCT_NEW_ARCH_ENABLED
    [surface start];
#else
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(contentDidAppear:)
                                                 name:RCTContentDidAppearNotification
                                               object:nil];
#endif

    return self;
}

#ifdef RCT_NEW_ARCH_ENABLED
- (instancetype)initWithHost:(RCTHost *)host
                  moduleName:(NSString *)moduleName
           initialProperties:(NSDictionary *)initialProperties
                eventEmitter:(RNNEventEmitter *)eventEmitter
             sizeMeasureMode:(RCTSurfaceSizeMeasureMode)sizeMeasureMode
         reactViewReadyBlock:(RNNReactViewReadyCompletionBlock)reactViewReadyBlock {
    
    RCTFabricSurface *surface = [host createSurfaceWithModuleName:moduleName
                                                initialProperties:initialProperties];
    [host.surfacePresenter addObserver:self];
    self = [super initWithSurface:surface sizeMeasureMode:sizeMeasureMode];
    
    _reactViewReadyBlock = reactViewReadyBlock;
    _eventEmitter = eventEmitter;
    
    return self;
}
#endif

#ifdef RCT_NEW_ARCH_ENABLED
#pragma mark - RCTSurfaceDelegate
- (void)surface:(__unused RCTSurface *)surface didChangeStage:(RCTSurfaceStage)stage {
    RCTExecuteOnMainQueue(^{
      [super surface:surface didChangeStage:stage];
      [self reactViewReady];
    });
}
#else

- (void)contentDidAppear:(NSNotification *)notification {
    RNNReactView *appearedView = notification.object;
    if ([appearedView.appProperties[@"componentId"] isEqual:self.componentId]) {
        [self reactViewReady];
    }
}
#endif
    
- (void)reactViewReady {
    if (_reactViewReadyBlock) {
        _reactViewReadyBlock();
        _reactViewReadyBlock = nil;
    }
#ifndef RCT_NEW_ARCH_ENABLED
    [[NSNotificationCenter defaultCenter] removeObserver:self];
#endif
}

#pragma mark - RNNComponentProtocol
- (void)componentWillAppear {
    if (!_isMounted) {
        _pendingWillAppear = YES;
        return;
    }
    
    _pendingWillAppear = NO;
    
    if (!_willAppear) {
        [_eventEmitter sendComponentWillAppear:self.componentId
                                 componentName:self.moduleName
                                 componentType:self.componentType];
        _willAppear = YES;
    }
}

- (void)componentDidAppear {
    if (!_isMounted) {
        _pendingDidAppear = YES;
        return;
    }
    
    _pendingDidAppear = NO;
    
    if (!_didAppear) {
        [_eventEmitter sendComponentDidAppear:self.componentId
                                componentName:self.moduleName
                                componentType:self.componentType];
        _didAppear = YES;
    }
}

- (void)componentDidDisappear {
    [_eventEmitter sendComponentDidDisappear:self.componentId
                               componentName:self.moduleName
                               componentType:self.componentType];
    _willAppear = NO;
    _didAppear = NO;
}
    
- (NSString *)componentId {
    return self.appProperties[@"componentId"];
}

- (NSString *)componentType {
    @throw [NSException exceptionWithName:@"componentType not implemented"
                                   reason:@"Should always subclass RNNReactView"
                                 userInfo:nil];
}
#pragma mark -
    
    
#ifdef RCT_NEW_ARCH_ENABLED
    
#pragma mark - RCTSurfacePresenterObserver
- (void)willMountComponentsWithRootTag:(NSInteger)rootTag {
    if (self.surface.rootTag == rootTag) {
        _isMounted = YES;
        
        if (_pendingWillAppear) {
            [self componentWillAppear];
        }
    }
}

- (void)didMountComponentsWithRootTag:(NSInteger)rootTag {
    if (self.surface.rootTag == rootTag) {
        _isMounted = YES;
        
        if (_pendingDidAppear) {
            [self componentDidAppear];
        }
    }
}
#pragma mark -
            
- (NSDictionary *)appProperties {
    @synchronized(self) {
        return self.surface.properties;
    }
}

- (void)setAppProperties:(NSDictionary *)newValue {
    @synchronized(self) {
        self.surface.properties = newValue;
    }
}

// TODO: Remove delegate in bridgeful
- (id<RCTSurfaceDelegate>)delegate {
    return self.surface.delegate;
}

- (void)setDelegate:(id<RCTSurfaceDelegate>)newValue {
    @synchronized(self) {
        self.surface.delegate = newValue;
    }
}

- (NSString *)moduleName {
    return self.surface.moduleName;
}

- (UIView *)view {
    return (UIView *)super.surface.view;
}

- (UIView *)contentView {
    return self;
}

- (RCTRootViewSizeFlexibility)sizeFlexibility {
    return convertToRootViewSizeFlexibility(super.sizeMeasureMode);
}

- (void)setSizeFlexibility:(RCTRootViewSizeFlexibility)sizeFlexibility {
    super.sizeMeasureMode = convertToSurfaceSizeMeasureMode(sizeFlexibility);
}
            
#endif
            
@end
