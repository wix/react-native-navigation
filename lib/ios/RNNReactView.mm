#import "RNNReactView.h"
#import "RNNAppDelegate.h"
#import <React/RCTRootContentView.h>

#ifdef RCT_NEW_ARCH_ENABLED
#import <React/RCTFabricSurface.h>
#import <React/RCTSurfacePresenterStub.h>
#import <React-RuntimeApple/ReactCommon/RCTHost.h>
#endif

static RCTSurfaceSizeMeasureMode convertToSurfaceSizeMeasureMode(RCTRootViewSizeFlexibility sizeFlexibility)
{
  switch (sizeFlexibility) {
    case RCTRootViewSizeFlexibilityWidthAndHeight:
      return RCTSurfaceSizeMeasureModeWidthUndefined | RCTSurfaceSizeMeasureModeHeightUndefined;
    case RCTRootViewSizeFlexibilityWidth:
      return RCTSurfaceSizeMeasureModeWidthUndefined | RCTSurfaceSizeMeasureModeHeightExact;
    case RCTRootViewSizeFlexibilityHeight:
      return RCTSurfaceSizeMeasureModeWidthExact | RCTSurfaceSizeMeasureModeHeightUndefined;
    case RCTRootViewSizeFlexibilityNone:
      return RCTSurfaceSizeMeasureModeWidthExact | RCTSurfaceSizeMeasureModeHeightExact;
  }
}

static RCTRootViewSizeFlexibility convertToRootViewSizeFlexibility(RCTSurfaceSizeMeasureMode sizeMeasureMode)
{
  switch (sizeMeasureMode) {
    case RCTSurfaceSizeMeasureModeWidthUndefined | RCTSurfaceSizeMeasureModeHeightUndefined:
      return RCTRootViewSizeFlexibilityWidthAndHeight;
    case RCTSurfaceSizeMeasureModeWidthUndefined | RCTSurfaceSizeMeasureModeHeightExact:
      return RCTRootViewSizeFlexibilityWidth;
    case RCTSurfaceSizeMeasureModeWidthExact | RCTSurfaceSizeMeasureModeHeightUndefined:
      return RCTRootViewSizeFlexibilityHeight;
    case RCTSurfaceSizeMeasureModeWidthExact | RCTSurfaceSizeMeasureModeHeightExact:
    default:
      return RCTRootViewSizeFlexibilityNone;
  }
}

@implementation RNNReactView {
    BOOL _isAppeared;
}

#ifdef RCT_NEW_ARCH_ENABLED
- (instancetype)initWithBridge:(RCTBridge *)bridge
                    moduleName:(NSString *)moduleName
             initialProperties:(NSDictionary *)initialProperties
                  eventEmitter:(RNNEventEmitter *)eventEmitter
               sizeMeasureMode:(RCTSurfaceSizeMeasureMode)sizeMeasureMode
           reactViewReadyBlock:(RNNReactViewReadyCompletionBlock)reactViewReadyBlock {
  RCTFabricSurface *surface = [[RCTFabricSurface alloc] initWithSurfacePresenter:(RCTSurfacePresenter *)bridge.surfacePresenter
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
    RCTFabricSurface *surface = [host createSurfaceWithModuleName:moduleName initialProperties: initialProperties];
    
    self = [super initWithSurface:surface sizeMeasureMode:sizeMeasureMode];
               
    _reactViewReadyBlock = reactViewReadyBlock;
    _eventEmitter = eventEmitter;
               
    return self;
}
#endif

#pragma mark - RCTSurfaceDelegate

- (void)surface:(__unused RCTSurface *)surface didChangeStage:(RCTSurfaceStage)stage
{
  RCTExecuteOnMainQueue(^{
      [super surface:surface didChangeStage:stage];
      [self reactViewReady];
  });
}
    
#ifndef RCT_NEW_ARCH_ENABLED
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
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}

- (void)componentWillAppear {
    if (!_isAppeared) {
        [_eventEmitter sendComponentWillAppear:self.componentId
                                 componentName:self.moduleName
                                 componentType:self.componentType];
    }
}

- (void)componentDidAppear {
    if (!_isAppeared) {
        [_eventEmitter sendComponentDidAppear:self.componentId
                                componentName:self.moduleName
                                componentType:self.componentType];
    }

    _isAppeared = YES;
}

- (void)componentDidDisappear {
    if (_isAppeared) {
        [_eventEmitter sendComponentDidDisappear:self.componentId
                                   componentName:self.moduleName
                                   componentType:self.componentType];
    }

    _isAppeared = NO;
}

- (void)invalidate {
    [((RCTRootContentView *)self.contentView) invalidate];
}

- (NSDictionary *)appProperties {
    @synchronized (self) {
#ifdef RCT_NEW_ARCH_ENABLED
        return self.surface.properties;
#else
        return self.appProperties;
#endif
    }
}
    
- (void)setAppProperties:(NSDictionary *)newValue
{
    @synchronized (self)
    {
#ifdef RCT_NEW_ARCH_ENABLED
        self.surface.properties = newValue;
#else
        self.appProperties = newValue;
#endif
    }
}
    
- (id<RCTSurfaceDelegate>)delegate {
    @synchronized (self) {
#ifdef RCT_NEW_ARCH_ENABLED
        return self.surface.delegate;
#else
        return self.appProperties;
#endif
    }
}
        
- (void)setDelegate:(id<RCTSurfaceDelegate>)newValue
{
    @synchronized (self)
    {
#ifdef RCT_NEW_ARCH_ENABLED
        self.surface.delegate = newValue;
#else
        self.delegate = delegate;
#endif
    }
}

- (NSString *)moduleName {
#ifdef RCT_NEW_ARCH_ENABLED
    return self.surface.moduleName;
#else
    return self.moduleName;
#endif
}
    
- (UIView *)view
{
#ifdef RCT_NEW_ARCH_ENABLED
    return (UIView *)super.surface.view;
#else
    return self.view;
#endif
}

- (UIView *)contentView
{
    return self;
}
    
- (RCTRootViewSizeFlexibility)sizeFlexibility
{
    return convertToRootViewSizeFlexibility(super.sizeMeasureMode);
}

- (void)setSizeFlexibility:(RCTRootViewSizeFlexibility)sizeFlexibility
{
    super.sizeMeasureMode = convertToSurfaceSizeMeasureMode(sizeFlexibility);
}
    
- (NSString *)componentId {
    return self.appProperties[@"componentId"];
}

- (NSString *)componentType {
    @throw [NSException exceptionWithName:@"componentType not implemented"
                                   reason:@"Should always subclass RNNReactView"
                                 userInfo:nil];
}

@end
