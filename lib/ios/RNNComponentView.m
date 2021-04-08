#import "RNNComponentView.h"
#import "RCTHelpers.h"

@implementation RNNComponentView {
    BOOL _observeLayerChange;
}

- (void)layoutSubviews {
    [super layoutSubviews];
#ifdef DEBUG
    [self removeYellowBox];
#endif
}

#ifdef DEBUG
- (void)removeYellowBox {
    if (self.subviews.count > 0 && self.subviews.firstObject.subviews.count > 0) {
        if (!_observeLayerChange) {
            [self.subviews.firstObject.subviews.firstObject.layer
                addObserver:self
                 forKeyPath:@"sublayers"
                    options:NSKeyValueObservingOptionNew
                    context:nil];
            _observeLayerChange = YES;
        }

        for (UIView *view in self.subviews.firstObject.subviews.firstObject.subviews) {
            if ([view.accessibilityLabel isEqualToString:@"! Yellow Box"]) {
                view.layer.opacity = 0;
                view.layer.zPosition = -100;
            }
        }
    }
}

- (void)observeValueForKeyPath:(NSString *)keyPath
                      ofObject:(id)object
                        change:(NSDictionary *)change
                       context:(void *)context {
    if ([keyPath isEqualToString:@"sublayers"]) {
        if (((CALayer *)object).sublayers.count > 1) {
            ((CALayer *)object).sublayers.lastObject.opacity = 0;
            ((CALayer *)object).sublayers.lastObject.zPosition = -100;
        }
    }
}

- (void)dealloc {
    if (_observeLayerChange) {
        [self.subviews.firstObject.subviews.firstObject.layer removeObserver:self
                                                                  forKeyPath:@"sublayers"];
    }
}

#endif

@end
