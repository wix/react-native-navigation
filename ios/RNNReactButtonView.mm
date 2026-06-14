#import "RNNReactButtonView.h"
#import <React/RCTSurface.h>

static const CGFloat kMinBarButtonSlotSize = 44.0;

@implementation RNNReactButtonView {
    NSLayoutConstraint *_widthConstraint;
    NSLayoutConstraint *_heightConstraint;
    BOOL _didCenterHorizontally;
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
            self.sizeFlexibility = RCTRootViewSizeFlexibilityWidth;
            self.translatesAutoresizingMaskIntoConstraints = NO;
            _widthConstraint = [self.widthAnchor constraintEqualToConstant:0];
            _heightConstraint = [self.heightAnchor constraintEqualToConstant:kMinBarButtonSlotSize];
            _widthConstraint.priority = UILayoutPriorityDefaultHigh;
            _heightConstraint.priority = UILayoutPriorityDefaultHigh;
            _widthConstraint.active = YES;
            _heightConstraint.active = YES;
            _didCenterHorizontally = NO;
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

- (UIView *)surfaceHostView {
#ifdef RCT_NEW_ARCH_ENABLED
    UIView *surfaceView = (UIView *)self.surface.view;
    if (surfaceView != nil && surfaceView.superview == self) {
        return surfaceView;
    }
#endif
    return self.subviews.firstObject;
}

- (void)accumulateLeafBoundsInContainer:(UIView *)container unionRect:(CGRect *)unionRect {
    for (UIView *subview in container.subviews) {
        if (subview.hidden || subview.alpha < 0.01) {
            continue;
        }
        if (subview.subviews.count == 0) {
            CGRect rect = [subview convertRect:subview.bounds toView:self];
            if (rect.size.width > 0 && rect.size.height > 0) {
                *unionRect = CGRectIsNull(*unionRect) ? rect : CGRectUnion(*unionRect, rect);
            }
        } else {
            [self accumulateLeafBoundsInContainer:subview unionRect:unionRect];
        }
    }
}

- (CGRect)visibleLeafContentBounds {
    CGRect unionRect = CGRectNull;
    UIView *surfaceView = [self surfaceHostView];
    if (surfaceView) {
        [self accumulateLeafBoundsInContainer:surfaceView unionRect:&unionRect];
    }
    if (CGRectIsNull(unionRect)) {
        [self accumulateLeafBoundsInContainer:self unionRect:&unionRect];
    }
    return CGRectIsNull(unionRect) ? CGRectZero : unionRect;
}

- (CGSize)measuredSurfaceContentSize {
    CGSize intrinsic = CGSizeZero;
#ifdef RCT_NEW_ARCH_ENABLED
    intrinsic = self.surface.intrinsicSize;
#else
    intrinsic = self.intrinsicContentSize;
#endif
    if (intrinsic.width <= 0 || intrinsic.height <= 0) {
        return CGSizeZero;
    }

    CGFloat maxWidth = self.bounds.size.width > 0 ? self.bounds.size.width : CGFLOAT_MAX;
    CGSize fit = [self sizeThatFits:CGSizeMake(maxWidth, CGFLOAT_MAX)];
    if (fit.height > 0 && fit.height < intrinsic.height - 0.5) {
        intrinsic.height = fit.height;
    }
    return intrinsic;
}

- (void)centerSurfaceContentIfNeeded {
    if (self.bounds.size.width <= 0 || self.bounds.size.height <= 0) {
        return;
    }

    UIView *surfaceView = [self surfaceHostView];
    if (!surfaceView || surfaceView == self) {
        return;
    }

    CGRect surfaceFrame = surfaceView.frame;
    if (surfaceFrame.size.width <= 0) {
        surfaceFrame.size.width = self.bounds.size.width;
    }
    if (surfaceFrame.size.height <= 0) {
        surfaceFrame.size.height = self.bounds.size.height;
    }
    surfaceView.frame = CGRectMake(0, 0, surfaceFrame.size.width, surfaceFrame.size.height);

    CGRect contentBounds = [self visibleLeafContentBounds];
    if (!CGRectIsEmpty(contentBounds)) {
        CGFloat targetY = (self.bounds.size.height - contentBounds.size.height) / 2.0;
        CGFloat deltaY = targetY - contentBounds.origin.y;
        if (fabs(deltaY) > 0.5) {
            surfaceView.frame = CGRectOffset(surfaceView.frame, 0, deltaY);
        }
        return;
    }

    CGSize contentSize = [self measuredSurfaceContentSize];
    if (contentSize.height > 0 && self.bounds.size.height > contentSize.height + 0.5) {
        CGFloat ty = (self.bounds.size.height - contentSize.height) / 2.0;
        surfaceView.frame = CGRectMake(0, ty, self.bounds.size.width, contentSize.height);
    }
}

- (void)handleIntrinsicSizeChange:(CGSize)intrinsicSize {
    if (intrinsicSize.width <= 0 || intrinsicSize.height <= 0) {
        return;
    }

    CGFloat width = MAX(intrinsicSize.width, kMinBarButtonSlotSize);
    if (_widthConstraint && _heightConstraint) {
        _widthConstraint.constant = width;
        _heightConstraint.constant = kMinBarButtonSlotSize;
    }

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
        [self setNeedsLayout];
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

        [self centerSurfaceContentIfNeeded];

        if (!_didCenterHorizontally && self.superview && self.frame.size.width > 0) {
            CGFloat wrapperWidth = self.superview.bounds.size.width;
            CGFloat selfWidth = self.frame.size.width;
            if (wrapperWidth > selfWidth + 0.5) {
                _didCenterHorizontally = YES;
                CGFloat tx = (wrapperWidth - selfWidth) / 2.0;
                CGAffineTransform transform = self.layer.affineTransform;
                self.layer.affineTransform = CGAffineTransformMakeTranslation(tx, transform.ty);
            }
        }
    }
}

- (NSString *)componentType {
	return ComponentTypeButton;
}

@end
