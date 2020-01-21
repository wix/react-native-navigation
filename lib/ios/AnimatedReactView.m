#import "AnimatedReactView.h"

@implementation AnimatedReactView {
    UIView* _originalParent;
    CGRect _originalFrame;
    UIView* _toElement;
    UIColor* _fromColor;
    UIColor* _toColor;
    SharedElementTransitionOptions* _transitionOptions;
}

- (instancetype)initElement:(UIView *)element toElement:(UIView *)toElement transitionOptions:(SharedElementTransitionOptions *)transitionOptions {
    self.location = [[RNNViewLocation alloc] initWithFromElement:element toElement:toElement];
    self = [super initWithFrame:self.location.fromFrame];
    _transitionOptions = transitionOptions;
    _toElement = toElement;
    _toElement.hidden = YES;
    _fromColor = element.backgroundColor;
    _toColor = toElement.backgroundColor;
    [self hijackReactElement:element];
    
    return self;
}

- (void)hijackReactElement:(UIView *)element {
    _reactView = element;
    _originalFrame = _reactView.frame;
    self.frame = self.location.fromFrame;
    _reactView.backgroundColor = _fromColor;
    _originalParent = _reactView.superview;
    _reactView.frame = self.bounds;
    [self addSubview:_reactView];
}

- (CGAffineTransform)animateWithProgress:(CGFloat)p {
    self.frame = [RNNInterpolator fromRect:self.location.fromFrame toRect:self.location.toFrame precent:p interpolation:self.interpolation];
    _reactView.backgroundColor = [RNNInterpolator fromColor:_fromColor toColor:_toColor precent:p];
    return CGAffineTransformIdentity;
}

- (void)end {
    _reactView.frame = _originalFrame;
    [_originalParent addSubview:_reactView];
    _toElement.hidden = NO;
    _reactView.backgroundColor = _fromColor;
    [self removeFromSuperview];
}

- (NSTimeInterval)duration {
    return [_transitionOptions.duration getWithDefaultValue:0.7];
}

- (NSTimeInterval)startDelay {
    return [_transitionOptions.startDelay getWithDefaultValue:0.0];
}

- (RNNInterpolationOptions)interpolation {
    return [RCTConvert RNNInterpolationOptions:[_transitionOptions.interpolation getWithDefaultValue:@"accelerateDecelerate"]];
}

- (void)layoutSubviews {
    [super layoutSubviews];
    _reactView.frame = self.bounds;
}

@end
