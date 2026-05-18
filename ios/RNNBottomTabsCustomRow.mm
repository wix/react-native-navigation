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
@end

@implementation RNNBottomTabsCustomRow

- (instancetype)initWithFrame:(CGRect)frame {
    self = [super initWithFrame:frame];
    if (self) {
        _cells = [NSMutableArray new];
        self.backgroundColor = UIColor.clearColor;
    }
    return self;
}

- (void)setItemViews:(NSArray<RNNCustomTabBarItemView *> *)itemViews {
    for (RNNBottomTabsCustomRowCell *cell in self.cells) {
        [cell removeFromSuperview];
    }
    [self.cells removeAllObjects];

    for (NSUInteger i = 0; i < itemViews.count; i++) {
        RNNBottomTabsCustomRowCell *cell = [[RNNBottomTabsCustomRowCell alloc] init];
        cell.index = i;
        cell.itemView = itemViews[i];
        [cell addTarget:self
                 action:@selector(handleCellTap:)
       forControlEvents:UIControlEventTouchUpInside];
        [self addSubview:cell];
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
    if (self.cells.count == 0) {
        return;
    }
    // Inset by the bottom safe area so cell content stays above the home
    // indicator. Without this the React cells render under the system
    // home-indicator overlay (visually bleeds past the bar; Detox
    // visibility checks fail at the view's center).
    UIEdgeInsets safe = self.safeAreaInsets;
    CGRect content =
        UIEdgeInsetsInsetRect(self.bounds, UIEdgeInsetsMake(0, 0, safe.bottom, 0));
    CGFloat width = content.size.width / (CGFloat)self.cells.count;
    CGFloat height = content.size.height;
    for (NSUInteger i = 0; i < self.cells.count; i++) {
        CGFloat x = floor((CGFloat)i * width);
        CGFloat nextX = floor((CGFloat)(i + 1) * width);
        self.cells[i].frame = CGRectMake(x, content.origin.y, nextX - x, height);
    }
}

- (void)safeAreaInsetsDidChange {
    [super safeAreaInsetsDidChange];
    [self setNeedsLayout];
}

@end
