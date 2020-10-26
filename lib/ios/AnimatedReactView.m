#import "AnimatedReactView.h"
#import "UIView+Utils.h"
#import <React/UIView+React.h>

@implementation AnimatedReactView {
    UIView *_originalParent;
    CGRect _originalFrame;
    CGFloat _originalCornerRadius;
    CGRect _originalLayoutBounds;
    CATransform3D _originalTransform;
    UIView *_toElement;
    UIColor *_fromColor;
    NSInteger _zIndex;
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
    _originalFrame = _reactView.frame;
    _originalTransform = element.layer.transform;
    _originalLayoutBounds = element.layer.bounds;
    self.contentMode = element.contentMode;
    self.frame = self.location.fromFrame;
    _originalParent = _reactView.superview;
    _originalCornerRadius = element.layer.cornerRadius;
    _reactView.frame = self.bounds;
    _reactView.layer.transform = CATransform3DIdentity;
    _reactView.layer.cornerRadius = self.location.fromCornerRadius;
    [self addSubview:_reactView];
}

- (void)reset {
    _reactView.frame = _originalFrame;
    _reactView.layer.cornerRadius = _originalCornerRadius;
    _reactView.bounds = _originalLayoutBounds;
    _reactView.layer.bounds = _originalLayoutBounds;
    _reactView.layer.transform = _originalTransform;
    [_originalParent addSubview:_reactView];
    _toElement.hidden = NO;
    _reactView.backgroundColor = _fromColor;
    [self removeFromSuperview];
}

- (void)layoutSubviews {
    [super layoutSubviews];
    _reactView.frame = self.bounds;
}

@end
