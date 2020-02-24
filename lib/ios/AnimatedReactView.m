#import "AnimatedReactView.h"
#import <React/UIView+React.h>

@implementation AnimatedReactView {
    UIView* _originalParent;
    CGRect _originalFrame;
    UIView* _toElement;
    UIColor* _fromColor;
    NSInteger _zIndex;
    SharedElementTransitionOptions* _transitionOptions;
}

- (instancetype)initElement:(UIView *)element toElement:(UIView *)toElement transitionOptions:(SharedElementTransitionOptions *)transitionOptions {
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

- (NSNumber *)reactZIndex {
    return @(_zIndex);
}

- (void)hijackReactElement:(UIView *)element {
    _reactView = element;
    _originalFrame = _reactView.frame;
    self.frame = self.location.fromFrame;
    _originalParent = _reactView.superview;
    _reactView.frame = self.bounds;
    [self addSubview:_reactView];
}

- (void)reset {
    _reactView.frame = _originalFrame;
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
