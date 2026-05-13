#import "RNNReactButtonView.h"
#import <React/RCTSurface.h>

@implementation RNNReactButtonView {
    NSLayoutConstraint *_widthConstraint;
    NSLayoutConstraint *_heightConstraint;
}

- (instancetype)initWithHost:(RCTHost *)host
                  moduleName:(NSString *)moduleName
           initialProperties:(NSDictionary *)initialProperties
                eventEmitter:(RNNEventEmitter *)eventEmitter
             sizeMeasureMode:(RCTSurfaceSizeMeasureMode)sizeMeasureMode
         reactViewReadyBlock:(RNNReactViewReadyCompletionBlock)reactViewReadyBlock {
    self = [super initWithHost:host moduleName:moduleName initialProperties:initialProperties eventEmitter:eventEmitter sizeMeasureMode:convertToSurfaceSizeMeasureMode(RCTRootViewSizeFlexibilityWidthAndHeight) reactViewReadyBlock:reactViewReadyBlock];
    [host.surfacePresenter addObserver:self];
    self.backgroundColor = [UIColor clearColor];

    if (@available(iOS 26.0, *)) {
        if (![self designRequiresCompatibility]) {
            self.translatesAutoresizingMaskIntoConstraints = NO;
            _widthConstraint = [self.widthAnchor constraintEqualToConstant:0];
            _heightConstraint = [self.heightAnchor constraintEqualToConstant:0];
            _widthConstraint.priority = UILayoutPriorityDefaultHigh;
            _heightConstraint.priority = UILayoutPriorityDefaultHigh;
            _widthConstraint.active = YES;
            _heightConstraint.active = YES;
        }
    }

    return self;
}

- (BOOL)designRequiresCompatibility {
    static BOOL checked = NO;
    static BOOL result = NO;
    if (!checked) {
        checked = YES;
        result = [[[NSBundle mainBundle] objectForInfoDictionaryKey:@"UIDesignRequiresCompatibility"] boolValue];
    }
    return result;
}

- (void)didMountComponentsWithRootTag:(NSInteger)rootTag {
    if (self.surface.rootTag == rootTag) {
        [super didMountComponentsWithRootTag:rootTag];
        [self sizeToFit];
        if (@available(iOS 26.0, *)) {
            if (![self designRequiresCompatibility]) {
                [self updateConstraintsToFitSize];
            }
        }
    }
}

- (void)updateConstraintsToFitSize {
    CGSize size = self.frame.size;
    if (size.width > 0 && size.height > 0) {
        _widthConstraint.constant = size.width;
        _heightConstraint.constant = size.height;
    }
}

- (NSString *)componentType {
	return ComponentTypeButton;
}

@end
