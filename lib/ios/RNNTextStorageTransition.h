#import "RNNAnimatedTextView.h"
#import "RNNElementBaseTransition.h"

@interface RNNTextStorageTransition : RNNElementBaseTransition

- (instancetype)initWithView:(UIView *)view
                        from:(NSTextStorage *)from
                          to:(NSTextStorage *)to
                  startDelay:(NSTimeInterval)startDelay
                    duration:(NSTimeInterval)duration
                interpolator:(id<RNNInterpolatorProtocol>)interpolator;

@property(nonatomic, strong) RNNAnimatedTextView *view;

@property(nonatomic, readonly, strong) NSTextStorage *from;
@property(nonatomic, readonly, strong) NSTextStorage *to;

@end
