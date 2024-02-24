#import "RNNRectTransition.h"

@interface RNNPathTransition : RNNRectTransition

@property(nonatomic) CGFloat fromCornerRadius;
@property(nonatomic) CGFloat toCornerRadius;

- (instancetype)initWithView:(UIView *)view
                    fromPath:(CGRect)fromPath
                      toPath:(CGRect)toPath
            fromCornerRadius:(CGFloat)fromCornerRadius
              toCornerRadius:(CGFloat)toCornerRadius
                  startDelay:(NSTimeInterval)startDelay
                    duration:(NSTimeInterval)duration
                interpolator:(id<RNNInterpolatorProtocol>)interpolator;

@end
