#import "AnimatedReactView.h"
#import "UIView+Utils.h"
#import <React/UIView+React.h>

@implementation AnimatedReactView {
    UIView *_originalParent;
    CGRect _originalFrame;
    CGFloat _originalCornerRadius;
    CGRect _originalLayoutBounds;
    CGPoint _originalCenter;
    CATransform3D _originalTransform;
    UIView *_toElement;
    UIColor *_fromColor;
    NSInteger _zIndex;
    UIViewContentMode _originalContentMode;
    SharedElementTransitionOptions *_transitionOptions;
}

- (instancetype)initElement:(UIView *)element
                  toElement:(UIView *)toElement
          transitionOptions:(SharedElementTransitionOptions *)transitionOptions {
    self.location = [[RNNViewLocation alloc] initWithFromElement:element toElement:toElement];
    self = [super initWithFrame:self.location.fromFrame];
    _transitionOptions = transitionOptions;
    _toElement = toElement;
    _toElement.hidden = YES;
    _fromColor = element.backgroundColor;
    _zIndex = toElement.reactZIndex;
    [self hijackReactElement:element];

    return self;
}

- (void)setBackgroundColor:(UIColor *)backgroundColor {
    [super setBackgroundColor:backgroundColor];
    _reactView.backgroundColor = backgroundColor;
}

- (void)setCornerRadius:(CGFloat)cornerRadius {
    [super setCornerRadius:cornerRadius];
    [_reactView setCornerRadius:cornerRadius];
}

- (NSNumber *)reactZIndex {
    return @(_zIndex);
}

- (void)hijackReactElement:(UIView *)element {
    _reactView = element;
    _originalContentMode = _reactView.contentMode;
    _originalFrame = _reactView.frame;
    _originalTransform = element.layer.transform;
    _originalParent = _reactView.superview;
    _originalCenter = _reactView.center;
    _originalCornerRadius = element.layer.cornerRadius;
    _originalTransform = _reactView.layer.transform;
    _originalLayoutBounds = _reactView.bounds;
    _reactView.layer.transform = CATransform3DIdentity;
    _reactView.layer.cornerRadius = self.location.fromCornerRadius;

    [self addSubview:_reactView];
}

- (void)layoutSubviews {
    [super layoutSubviews];
    self.reactView.bounds = self.bounds;
    self.reactView.center = CGPointMake(self.bounds.size.width / 2, self.bounds.size.height / 2);
}

- (void)reset {
    _reactView.center = _originalCenter;
    _reactView.layer.cornerRadius = _originalCornerRadius;
    _reactView.bounds = _originalLayoutBounds;
    _reactView.layer.transform = _originalTransform;
    _reactView.contentMode = _originalContentMode;
    [_originalParent insertSubview:_reactView atIndex:self.location.index];
    _toElement.hidden = NO;
    _reactView.backgroundColor = _fromColor;
    [self removeFromSuperview];
}

- (NSArray<id<DisplayLinkAnimation>> *)extraAnimations {
    return @[];
}

@end
