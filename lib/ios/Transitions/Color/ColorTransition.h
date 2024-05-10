#import "ElementBaseTransition.h"
#import "FloatTransition.h"

@interface ColorTransition : ElementBaseTransition

- (instancetype)initWithView:(UIView *)view
                        from:(UIColor *)from
                          to:(UIColor *)to
                  startDelay:(NSTimeInterval)startDelay
                    duration:(NSTimeInterval)duration
                interpolator:(id<Interpolator>)interpolator;

@property(nonatomic, readonly, strong) UIColor *from;
@property(nonatomic, readonly, strong) UIColor *to;

@end
