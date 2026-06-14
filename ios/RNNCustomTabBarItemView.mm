#import "RNNCustomTabBarItemView.h"

@interface RNNCustomTabBarItemView ()

@property(nonatomic, readwrite, strong) RNNReactView *reactView;
@property(nonatomic, assign) NSUInteger tabIndex;
@property(nonatomic, assign) BOOL isSelected;
@property(nonatomic, copy) NSString *badge;

@end

@implementation RNNCustomTabBarItemView

- (instancetype)initWithReactView:(RNNReactView *)reactView
                         tabIndex:(NSUInteger)tabIndex
                         selected:(BOOL)selected
                            badge:(NSString *)badge {
    self = [super initWithFrame:CGRectZero];
    if (self) {
        _reactView = reactView;
        _tabIndex = tabIndex;
        _isSelected = selected;
        _badge = [badge copy];

        self.backgroundColor = [UIColor clearColor];
        self.userInteractionEnabled = NO;

        _reactView.backgroundColor = [UIColor clearColor];
        _reactView.userInteractionEnabled = NO;
        [self addSubview:_reactView];

        [self updateProps];
    }
    return self;
}

- (void)layoutSubviews {
    [super layoutSubviews];
    self.reactView.frame = self.bounds;
}

- (CGSize)sizeThatFits:(CGSize)size {
    return size;
}

- (void)setSelected:(BOOL)selected {
    if (self.isSelected == selected) {
        return;
    }
    self.isSelected = selected;
    [self updateProps];
}

- (void)setBadge:(NSString *)badge {
    if (badge == _badge || [badge isEqualToString:_badge]) {
        return;
    }
    _badge = [badge copy];
    [self updateProps];
}

- (void)updateProps {
    NSMutableDictionary *props = [NSMutableDictionary dictionary];
#ifdef RCT_NEW_ARCH_ENABLED
    // RNNReactView's `properties` setter is a no-op on Fabric (it writes to
    // the auto-synthesized ivar instead of the underlying surface). Update
    // the React tree by writing directly to the surface so prop changes
    // propagate to JS.
    [props addEntriesFromDictionary:(self.reactView.surface.properties ?: @{})];
#else
    [props addEntriesFromDictionary:(self.reactView.appProperties ?: @{})];
#endif
    props[@"tabIndex"] = @(self.tabIndex);
    props[@"selected"] = @(self.isSelected);
    props[@"badge"] = self.badge ?: [NSNull null];
#ifdef RCT_NEW_ARCH_ENABLED
    self.reactView.surface.properties = props;
#else
    self.reactView.appProperties = props;
#endif
}

@end
