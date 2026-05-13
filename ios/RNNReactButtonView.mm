#import "RNNReactButtonView.h"
#import <React/RCTSurface.h>

@implementation RNNReactButtonView {
    NSLayoutConstraint *_widthConstraint;
    NSLayoutConstraint *_heightConstraint;
    BOOL _didCenter;
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
            _didCenter = NO;
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

- (void)didMoveToWindow {
    [super didMoveToWindow];
    if (@available(iOS 26.0, *)) {
        if (![self designRequiresCompatibility] && self.window) {
            [self syncButtonBackground];
        }
    }
}

- (void)layoutSubviews {
    [super layoutSubviews];
    if (@available(iOS 26.0, *)) {
        if ([self designRequiresCompatibility]) return;
        if (!_didCenter && self.superview && self.frame.size.width > 0) {
            CGFloat wrapperWidth = self.superview.bounds.size.width;
            CGFloat selfWidth = self.frame.size.width;
            if (wrapperWidth > selfWidth) {
                _didCenter = YES;
                CGFloat tx = (wrapperWidth - selfWidth) / 2.0;
                self.layer.affineTransform = CGAffineTransformMakeTranslation(tx, 0);
            }
        }
        [self syncButtonBackground];
    }
}

- (void)syncButtonBackground {
    if (!_buttonBackgroundColor) return;

    UIView *target = self.superview.superview.superview;
    if (!target || target.bounds.size.height <= 0) return;

    target.backgroundColor = _buttonBackgroundColor;
    target.layer.cornerRadius = target.bounds.size.height / 2.0;
    target.clipsToBounds = YES;
}

- (NSString *)componentType {
	return ComponentTypeButton;
}

@end
