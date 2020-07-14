#import "RNNReactView.h"
#import <React/RCTRootContentView.h>

@implementation RNNReactView {
    BOOL _isAppeared;
}

- (instancetype)initWithBridge:(RCTBridge *)bridge moduleName:(NSString *)moduleName initialProperties:(NSDictionary *)initialProperties eventEmitter:(RNNEventEmitter *)eventEmitter reactViewReadyBlock:(RNNReactViewReadyCompletionBlock)reactViewReadyBlock {
	self = [super initWithBridge:bridge moduleName:moduleName initialProperties:initialProperties];
	[[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(contentDidAppear:) name:RCTContentDidAppearNotification object:nil];
	 _reactViewReadyBlock = reactViewReadyBlock;
    _eventEmitter = eventEmitter;
    
	return self;
}

- (void)contentDidAppear:(NSNotification *)notification {
	RNNReactView* appearedView = notification.object;
	 if ([appearedView.appProperties[@"componentId"] isEqual:self.componentId]) {
         [self reactViewReady];
	 }
}

- (void)reactViewReady {
    if (_reactViewReadyBlock) {
        _reactViewReadyBlock();
        _reactViewReadyBlock = nil;
    }
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}


- (void)componentDidAppear {
    if (!_isAppeared) {
        [_eventEmitter sendComponentDidAppear:self.componentId componentName:self.moduleName componentType:self.componentType];
    }
    
    _isAppeared = YES;
}

- (void)componentDidDisappear {
    if (_isAppeared) {
        [_eventEmitter sendComponentDidDisappear:self.componentId componentName:self.moduleName componentType:self.componentType];
    }
    
    _isAppeared = NO;
}

- (void)invalidate {
    [((RCTRootContentView *)self.contentView) invalidate];
}

- (NSString *)componentId {
	return self.appProperties[@"componentId"];
}

- (NSString *)componentType {
    @throw [NSException exceptionWithName:@"componentType not implemented" reason:@"Should always subclass RNNReactView" userInfo:nil];
}

@end
