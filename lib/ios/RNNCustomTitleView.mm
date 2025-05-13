#import "RNNCustomTitleView.h"

@interface RNNCustomTitleView ()

@property(nonatomic, strong) RNNReactView *subView;
@property(nonatomic, strong) NSString *alignment;

@end

@implementation RNNCustomTitleView

- (instancetype)initWithFrame:(CGRect)frame
                      subView:(RNNReactView *)subView
                    alignment:(NSString *)alignment {
    self = [super init];

    if (self) {
        self.subView = subView;
        self.alignment = alignment;

        self.backgroundColor = [UIColor clearColor];
        self.subView.backgroundColor = [UIColor clearColor];

        if ([alignment isEqualToString:@"fill"]) {
            self.frame = frame;
            subView.sizeFlexibility = RCTRootViewSizeFlexibilityNone;
        } else {
            self.subView.delegate = self;
            subView.sizeFlexibility = RCTRootViewSizeFlexibilityWidthAndHeight;
        }

        [self addSubview:subView];
    }

    return self;
}

- (CGSize)sizeThatFits:(CGSize)size {
    if ([self.alignment isEqualToString:@"fill"]) {
        return size;
    }
    return [super sizeThatFits:size];
}

- (void)layoutSubviews {
    [super layoutSubviews];
    if ([self.alignment isEqualToString:@"fill"]) {
        [self.subView setFrame:self.frame];
    }
}

- (NSString *)alignment {
    return _alignment ? _alignment : @"center";
}

#ifdef RCT_NEW_ARCH_ENABLED
- (void)surface:(RCTSurface *)surface didChangeIntrinsicSize:(CGSize)intrinsicSize {
    if ([self.alignment isEqualToString:@"center"]) {
        [self setFrame:CGRectMake(self.frame.origin.x, self.frame.origin.y,
                                  self.subView.intrinsicContentSize.width,
                                  self.subView.intrinsicContentSize.height)];
        [self.subView setFrame:CGRectMake(0, 0, surface.intrinsicSize.width,
                                          surface.intrinsicSize.height)];
    }
}

#else
- (void)rootViewDidChangeIntrinsicSize:(RCTRootView *)rootView {
    if ([self.alignment isEqualToString:@"center"]) {
        [self setFrame:CGRectMake(self.frame.origin.x, self.frame.origin.y,
                                  self.subView.intrinsicContentSize.width,
                                  self.subView.intrinsicContentSize.height)];
        [self.subView setFrame:CGRectMake(0, 0, rootView.intrinsicContentSize.width,
                                          rootView.intrinsicContentSize.height)];
    }
}
#endif

@end
