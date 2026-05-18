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
@property(nonatomic, strong) UIView *backgroundColorView;
@property(nonatomic, strong) RNNBottomTabsCustomRowOptions *options;
@end

@implementation RNNBottomTabsCustomRow

- (instancetype)initWithFrame:(CGRect)frame {
    self = [super initWithFrame:frame];
    if (self) {
        _cells = [NSMutableArray new];
        self.backgroundColor = UIColor.clearColor;

        // Solid background layer (only made visible if user sets backgroundColor).
        _backgroundColorView = [[UIView alloc] init];
        _backgroundColorView.hidden = YES;
        _backgroundColorView.clipsToBounds = YES;
        if (@available(iOS 13.0, *)) {
            _backgroundColorView.layer.cornerCurve = kCACornerCurveContinuous;
        }
        [self addSubview:_backgroundColorView];

        // Visual effect (blur / glass) layer. Default depends on iOS version.
        UIVisualEffect *effect = [RNNBottomTabsCustomRow defaultBackgroundEffect];
        _backgroundEffectView = [[UIVisualEffectView alloc] initWithEffect:effect];
        _backgroundEffectView.clipsToBounds = YES;
        if (@available(iOS 13.0, *)) {
            _backgroundEffectView.layer.cornerCurve = kCACornerCurveContinuous;
        }
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

+ (UIVisualEffect *)blurBackgroundEffect {
    if (@available(iOS 13.0, *)) {
        return [UIBlurEffect effectWithStyle:UIBlurEffectStyleSystemChromeMaterial];
    }
    return [UIBlurEffect effectWithStyle:UIBlurEffectStyleLight];
}

- (void)applyOptions:(RNNBottomTabsCustomRowOptions *)options {
    self.options = options;

    UIColor *solidColor =
        options.backgroundColor.hasValue ? options.backgroundColor.get : nil;
    NSString *effectName =
        options.backgroundEffect.hasValue ? options.backgroundEffect.get : nil;

    BOOL useSolidColor = solidColor != nil;
    self.backgroundColorView.hidden = !useSolidColor;
    if (useSolidColor) {
        self.backgroundColorView.backgroundColor = solidColor;
    }

    // Effect view stays unless explicitly disabled or overridden by solid color.
    BOOL hideEffect = useSolidColor || [effectName isEqualToString:@"none"];
    self.backgroundEffectView.hidden = hideEffect;
    if (!hideEffect) {
        if ([effectName isEqualToString:@"blur"]) {
            self.backgroundEffectView.effect = [RNNBottomTabsCustomRow blurBackgroundEffect];
        } else if ([effectName isEqualToString:@"glass"]) {
            self.backgroundEffectView.effect = [RNNBottomTabsCustomRow defaultBackgroundEffect];
        }
    }

    [self setNeedsLayout];
}

- (CGFloat)effectiveCornerRadius {
    if (self.options.cornerRadius.hasValue) {
        return [self.options.cornerRadius.get doubleValue];
    }
    if (@available(iOS 26.0, *)) {
        return 28.0;
    }
    return 0.0;
}

- (CGFloat)effectiveHorizontalMargin {
    if (self.options.horizontalMargin.hasValue) {
        return [self.options.horizontalMargin.get doubleValue];
    }
    if (@available(iOS 26.0, *)) {
        return 16.0;
    }
    return 0.0;
}

- (CGFloat)effectiveBottomMargin {
    if (self.options.bottomMargin.hasValue) {
        return [self.options.bottomMargin.get doubleValue];
    }
    return 0.0;
}

- (CGFloat)desiredRowHeightForNativeTabBarHeight:(CGFloat)nativeTabBarHeight
                                       safeBottom:(CGFloat)safeBottom {
    CGFloat defaultContentHeight = MAX(0, nativeTabBarHeight - safeBottom);
    CGFloat contentHeight = defaultContentHeight;
    if (@available(iOS 26.0, *)) {
        contentHeight += 18.0;  // default extra for iOS 26 floating bar look
    }
    if (self.options.height.hasValue) {
        contentHeight = [self.options.height.get doubleValue];
    }
    return contentHeight + safeBottom + [self effectiveBottomMargin];
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
    // indicator, plus any user-supplied bottom margin.
    UIEdgeInsets safe = self.safeAreaInsets;
    CGFloat bottomInset = safe.bottom + [self effectiveBottomMargin];
    CGRect content =
        UIEdgeInsetsInsetRect(self.bounds, UIEdgeInsetsMake(0, 0, bottomInset, 0));

    CGFloat horizontalMargin = [self effectiveHorizontalMargin];
    if (horizontalMargin > 0) {
        content = CGRectInset(content, horizontalMargin, 0);
    }

    CGFloat cornerRadius = [self effectiveCornerRadius];
    self.backgroundEffectView.layer.cornerRadius = cornerRadius;
    self.backgroundColorView.layer.cornerRadius = cornerRadius;

    self.backgroundEffectView.frame = content;
    self.backgroundColorView.frame = content;

    if (self.cells.count == 0) {
        return;
    }

    // Cells are subviews of the effect view's contentView (so the rounded
    // mask clips them); their frames are relative to the effect view's
    // bounds. The solid color layer sits behind and doesn't host cells.
    UIView *cellContainer = self.backgroundColorView.hidden
                                ? self.backgroundEffectView.contentView
                                : self.backgroundColorView;
    // Make sure cells live in the visible container.
    if (self.cells.firstObject.superview != cellContainer) {
        for (RNNBottomTabsCustomRowCell *cell in self.cells) {
            [cell removeFromSuperview];
            [cellContainer addSubview:cell];
        }
    }

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
