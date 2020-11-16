#import "AnimatedTextView.h"
#import "ElementBaseTransition.h"

@interface TextStorageTransition : ElementBaseTransition

- (instancetype)initWithView:(UIView *)view
                        from:(NSTextStorage *)from
                          to:(NSTextStorage *)to
                  startDelay:(NSTimeInterval)startDelay
                    duration:(NSTimeInterval)duration
                interpolator:(id<Interpolator>)interpolator;

@property(nonatomic, strong) AnimatedTextView *view;

@property(nonatomic, readonly, strong) NSTextStorage *from;
@property(nonatomic, readonly, strong) NSTextStorage *to;

@end
