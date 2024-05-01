#import "RNNReactTitleView.h"

@implementation RNNReactTitleView {
    BOOL _fillParent;
}

- (NSString *)componentType {
    return ComponentTypeTitle;
}

- (CGSize)intrinsicContentSize {
    if (_fillParent) {
        return UILayoutFittingExpandedSize;
    } else {
        return [super intrinsicContentSize];
    }
}

- (void)setAlignment:(NSString *)alignment inFrame:(CGRect)frame {
    if ([alignment isEqualToString:@"fill"]) {
        _fillParent = YES;
        self.translatesAutoresizingMaskIntoConstraints = NO;
        self.sizeFlexibility = RCTRootViewSizeFlexibilityNone;
    } else {
        self.sizeFlexibility = RCTRootViewSizeFlexibilityWidthAndHeight;
        __weak RNNReactView *weakSelf = self;
        [self setRootViewDidChangeIntrinsicSize:^(CGSize intrinsicSize) {
          [weakSelf setFrame:CGRectMake(0, 0, intrinsicSize.width, intrinsicSize.height)];
        }];
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
