#import "RNNReactTitleView.h"

static const CGFloat kTitleViewDefaultHeight = 44.0;

@implementation RNNReactTitleView {
    BOOL _fillParent;
    CGFloat _expectedHeight;
    BOOL _intrinsicSizeLocked;
}

- (NSString *)componentType {
    return ComponentTypeTitle;
}

- (CGSize)intrinsicContentSize {
    if (_fillParent) {
        return CGSizeMake(UILayoutFittingExpandedSize.width,
                          _expectedHeight > 0 ? _expectedHeight : kTitleViewDefaultHeight);
    }
    return [super intrinsicContentSize];
}

- (CGSize)sizeThatFits:(CGSize)size {
    if (_fillParent) {
        return size;
    }
    return [super sizeThatFits:size];
}

- (void)setAlignment:(NSString *)alignment inFrame:(CGRect)frame {
    if ([alignment isEqualToString:@"fill"]) {
        _fillParent = YES;
        _expectedHeight = frame.size.height > 0 ? frame.size.height : kTitleViewDefaultHeight;
        self.frame = frame;
        self.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleLeftMargin |
                                UIViewAutoresizingFlexibleRightMargin | UIViewAutoresizingFlexibleHeight;
        self.sizeFlexibility = RCTRootViewSizeFlexibilityNone;
    } else {
        _fillParent = NO;
        self.autoresizingMask = UIViewAutoresizingNone;
        self.sizeFlexibility = RCTRootViewSizeFlexibilityWidthAndHeight;
        __weak RNNReactTitleView *weakSelf = self;
        [self setRootViewDidChangeIntrinsicSize:^(CGSize intrinsicSize) {
            RNNReactTitleView *strongSelf = weakSelf;
            if (!strongSelf) {
                return;
            }
            if (intrinsicSize.width <= 0 || intrinsicSize.height <= 0) {
                return;
            }
            if (@available(iOS 26.0, *)) {
                if (strongSelf->_intrinsicSizeLocked) {
                    return;
                }
                strongSelf->_intrinsicSizeLocked = YES;
            }
            [strongSelf setFrame:CGRectMake(0, 0, intrinsicSize.width, intrinsicSize.height)];
        }];
    }
}

- (void)layoutSubviews {
    [super layoutSubviews];
    if (_fillParent && self.bounds.size.height > 0 && _expectedHeight != self.bounds.size.height) {
        _expectedHeight = self.bounds.size.height;
        [self invalidateIntrinsicContentSize];
    }
}

- (void)setRootViewDidChangeIntrinsicSize:(void (^)(CGSize))rootViewDidChangeIntrinsicSize {
    _rootViewDidChangeIntrinsicSize = rootViewDidChangeIntrinsicSize;
    self.delegate = self;
}

- (void)rootViewDidChangeIntrinsicSize:(RCTRootView *)rootView {
    if (_rootViewDidChangeIntrinsicSize) {
        _rootViewDidChangeIntrinsicSize(rootView.intrinsicContentSize);
    }
}

@end
