#import "RNNBottomTabsCustomRow.h"

@interface RNNBottomTabsCustomRowCell : UIControl
@property(nonatomic, strong, nullable) RNNCustomTabBarItemView *itemView;
@property(nonatomic, assign) NSUInteger index;
@end

@implementation RNNBottomTabsCustomRowCell

- (void)setItemView:(RNNCustomTabBarItemView *)itemView {
    if (_itemView == itemView) {
        return;
    }
    [_itemView removeFromSuperview];
    _itemView = itemView;
    if (itemView) {
        itemView.translatesAutoresizingMaskIntoConstraints = YES;
        itemView.autoresizingMask =
            UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight;
        itemView.frame = self.bounds;
        [self addSubview:itemView];
    }
}

- (void)layoutSubviews {
    [super layoutSubviews];
    self.itemView.frame = self.bounds;
}

@end

@interface RNNBottomTabsCustomRow ()
@property(nonatomic, strong) NSMutableArray<RNNBottomTabsCustomRowCell *> *cells;
@property(nonatomic, strong) UIVisualEffectView *backgroundEffectView;
@end

@implementation RNNBottomTabsCustomRow

- (instancetype)initWithFrame:(CGRect)frame {
    self = [super initWithFrame:frame];
    if (self) {
        _cells = [NSMutableArray new];
        self.backgroundColor = UIColor.clearColor;

        UIVisualEffect *effect = [RNNBottomTabsCustomRow defaultBackgroundEffect];
        _backgroundEffectView = [[UIVisualEffectView alloc] initWithEffect:effect];
        _backgroundEffectView.clipsToBounds = YES;
        if (@available(iOS 13.0, *)) {
            _backgroundEffectView.layer.cornerCurve = kCACornerCurveContinuous;
        }
        // On iOS 26 we float the pill (rounded corners). Pre-26 we run
        // edge-to-edge to match legacy tab bars.
        if (@available(iOS 26.0, *)) {
            _backgroundEffectView.layer.cornerRadius = 28.0;
        }
        [self addSubview:_backgroundEffectView];
    }
    return self;
}

// `UIGlassEffect` is a new visual effect introduced in iOS 26. Reference it
// at runtime so this file still compiles against older SDKs and so we get a
// usable fallback on older OS versions.
+ (UIVisualEffect *)defaultBackgroundEffect {
    Class glassClass = NSClassFromString(@"UIGlassEffect");
    if (glassClass) {
        UIVisualEffect *glass = [[glassClass alloc] init];
        if (glass) {
            return glass;
        }
    }
    if (@available(iOS 13.0, *)) {
        return [UIBlurEffect effectWithStyle:UIBlurEffectStyleSystemChromeMaterial];
    }
    return [UIBlurEffect effectWithStyle:UIBlurEffectStyleLight];
}

- (void)setItemViews:(NSArray<RNNCustomTabBarItemView *> *)itemViews {
    for (RNNBottomTabsCustomRowCell *cell in self.cells) {
        [cell removeFromSuperview];
    }
    [self.cells removeAllObjects];

    UIView *cellContainer = self.backgroundEffectView.contentView;
    for (NSUInteger i = 0; i < itemViews.count; i++) {
        RNNBottomTabsCustomRowCell *cell = [[RNNBottomTabsCustomRowCell alloc] init];
        cell.index = i;
        cell.itemView = itemViews[i];
        [cell addTarget:self
                 action:@selector(handleCellTap:)
       forControlEvents:UIControlEventTouchUpInside];
        [cellContainer addSubview:cell];
        [self.cells addObject:cell];
    }
    [self setNeedsLayout];
}

- (void)setSelectedIndex:(NSUInteger)selectedIndex {
    for (NSUInteger i = 0; i < self.cells.count; i++) {
        RNNCustomTabBarItemView *itemView = self.cells[i].itemView;
        [itemView setSelected:(i == selectedIndex)];
    }
}

- (void)handleCellTap:(RNNBottomTabsCustomRowCell *)cell {
    if (self.onTapAtIndex) {
        self.onTapAtIndex(cell.index);
    }
}

- (void)layoutSubviews {
    [super layoutSubviews];

    // Inset by the bottom safe area so cell content stays above the home
    // indicator. Without this the React cells render under the system
    // home-indicator overlay (Detox visibility checks fail at the view's
    // center on iPhones with a home indicator).
    UIEdgeInsets safe = self.safeAreaInsets;
    CGRect content =
        UIEdgeInsetsInsetRect(self.bounds, UIEdgeInsetsMake(0, 0, safe.bottom, 0));

    // iOS 26 floating-pill inset so the glass effect sits centered with
    // breathing room from the screen edges, matching native iOS 26 tab bars.
    // Horizontal-only — the cells need the full content height to fit an
    // icon + label without cramping.
    if (@available(iOS 26.0, *)) {
        CGFloat horizontalMargin = 16.0;
        content = CGRectInset(content, horizontalMargin, 0);
    }

    self.backgroundEffectView.frame = content;

    if (self.cells.count == 0) {
        return;
    }

    // Cells are subviews of the effect view's contentView (so the rounded
    // mask clips them), so their frames are relative to the effect view's
    // bounds.
    CGFloat totalWidth = content.size.width;
    CGFloat totalHeight = content.size.height;
    CGFloat width = totalWidth / (CGFloat)self.cells.count;
    for (NSUInteger i = 0; i < self.cells.count; i++) {
        CGFloat x = floor((CGFloat)i * width);
        CGFloat nextX = floor((CGFloat)(i + 1) * width);
        self.cells[i].frame = CGRectMake(x, 0, nextX - x, totalHeight);
    }
}

- (void)safeAreaInsetsDidChange {
    [super safeAreaInsetsDidChange];
    [self setNeedsLayout];
}

@end
