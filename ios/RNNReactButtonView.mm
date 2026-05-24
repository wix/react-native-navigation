#import "RNNReactButtonView.h"
#import <React/RCTSurface.h>

static const CGFloat kMinBarButtonSlotSize = 44.0;

@implementation RNNReactButtonView {
    NSLayoutConstraint *_widthConstraint;
    NSLayoutConstraint *_heightConstraint;
    BOOL _didCenter;
    CGFloat _lastReportedWidth;
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
            _heightConstraint = [self.heightAnchor constraintEqualToConstant:kMinBarButtonSlotSize];
            _widthConstraint.priority = UILayoutPriorityDefaultHigh;
            _heightConstraint.priority = UILayoutPriorityDefaultHigh;
            _widthConstraint.active = YES;
            _heightConstraint.active = YES;
            _didCenter = NO;
            self.delegate = self;
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

- (void)handleIntrinsicSizeChange:(CGSize)intrinsicSize {
    if (intrinsicSize.width <= 0 || intrinsicSize.height <= 0) {
        return;
    }

    CGFloat width = MAX(intrinsicSize.width, kMinBarButtonSlotSize);
  // Keep internal constraints stable: height is always the 44pt bar slot.
    if (_widthConstraint && _heightConstraint) {
        _widthConstraint.constant = width;
        _heightConstraint.constant = kMinBarButtonSlotSize;
    }

  // Only notify the bar item when width grows beyond the initial 44pt reservation.
    if (self.intrinsicSizeDidChangeHandler && width > kMinBarButtonSlotSize &&
        width > _lastReportedWidth) {
        _lastReportedWidth = width;
        self.intrinsicSizeDidChangeHandler(intrinsicSize);
    }

    [self setNeedsLayout];
}

- (void)didMountComponentsWithRootTag:(NSInteger)rootTag {
    if (self.surface.rootTag == rootTag) {
        [super didMountComponentsWithRootTag:rootTag];
        [self sizeToFit];
    }
}

#ifdef RCT_NEW_ARCH_ENABLED
- (void)surface:(RCTSurface *)surface didChangeIntrinsicSize:(CGSize)intrinsicSize {
    [self handleIntrinsicSizeChange:intrinsicSize];
}
#else
- (void)rootViewDidChangeIntrinsicSize:(RCTRootView *)rootView {
    [self handleIntrinsicSizeChange:rootView.intrinsicContentSize];
    [rootView setNeedsUpdateConstraints];
    [rootView updateConstraintsIfNeeded];
}
#endif

- (void)layoutSubviews {
    [super layoutSubviews];
    if (@available(iOS 26.0, *)) {
        if ([self designRequiresCompatibility]) {
            return;
        }
        if (!_didCenter && self.superview && self.frame.size.width > 0) {
            CGFloat wrapperWidth = self.superview.bounds.size.width;
            CGFloat selfWidth = self.frame.size.width;
            if (wrapperWidth > selfWidth + 0.5) {
                _didCenter = YES;
                CGFloat tx = (wrapperWidth - selfWidth) / 2.0;
                self.layer.affineTransform = CGAffineTransformMakeTranslation(tx, 0);
            }
        }
    }
}

- (NSString *)componentType {
	return ComponentTypeButton;
}

@end
