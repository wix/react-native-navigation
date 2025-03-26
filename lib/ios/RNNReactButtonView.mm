#import "RNNReactButtonView.h"

@implementation RNNReactButtonView {
    BOOL _didAppearOnce;
}

- (instancetype)initWithHost:(RCTHost *)host
                  moduleName:(NSString *)moduleName
           initialProperties:(NSDictionary *)initialProperties
                eventEmitter:(RNNEventEmitter *)eventEmitter
             sizeMeasureMode:(RCTSurfaceSizeMeasureMode)sizeMeasureMode
         reactViewReadyBlock:(RNNReactViewReadyCompletionBlock)reactViewReadyBlock {
    self = [super initWithHost:host moduleName:moduleName initialProperties:initialProperties eventEmitter:eventEmitter sizeMeasureMode:convertToSurfaceSizeMeasureMode(RCTRootViewSizeFlexibilityWidthAndHeight) reactViewReadyBlock:reactViewReadyBlock];
    [host.surfacePresenter addObserver:self];
    self.backgroundColor = UIColor.clearColor;

    return self;
}

- (void)willMountComponentsWithRootTag:(NSInteger)rootTag {
    if (self.surface.rootTag == rootTag) {
        [super willMountComponentsWithRootTag:rootTag];
        if (!_didAppearOnce) {
            [self componentWillAppear];
        }
    }
}

- (void)didMountComponentsWithRootTag:(NSInteger)rootTag {
    if (self.surface.rootTag == rootTag) {
        [super didMountComponentsWithRootTag:rootTag];
        [self sizeToFit];
        if (!_didAppearOnce) {
            [self componentDidAppear];
            _didAppearOnce = YES;
        }
    }
}

- (NSString *)componentType {
	return ComponentTypeButton;
}

@end
